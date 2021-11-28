/**
 * @author: zty
 * @program: wiki
 * @ClassName EbookService
 * @description:
 * @create: 2021-11-20 12:24
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zty.wiki.domain.Ebook;
import com.zty.wiki.domain.EbookExample;
import com.zty.wiki.mapper.EbookMapper;
import com.zty.wiki.req.EbookQueryReq;
import com.zty.wiki.req.EbookSaveReq;
import com.zty.wiki.resp.EbookQueryResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.util.CopyUtil;
import com.zty.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 分页查询
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 增加查询支持分页功能
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(example);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal()); //总行数
        LOG.info("总页数：{}", pageInfo.getPages()); //总页数
        // 使用工具类复制列表
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public List<EbookQueryResp> all() {
        EbookExample example = new EbookExample();
        List<Ebook> ebookList = ebookMapper.selectByExample(example);
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        return respList;
    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req){
      Ebook ebook = CopyUtil.copy(req,Ebook.class);
      if(ObjectUtils.isEmpty(req.getId())){
          //新增
          ebook.setId(snowFlake.nextId());
          ebook.setDocCount(0);
          ebook.setViewCount(0);
          ebook.setVoteCount(0);
          ebookMapper.insert(ebook);
      }else{
          // 更新
          ebookMapper.updateByPrimaryKey(ebook);
      }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }

}
