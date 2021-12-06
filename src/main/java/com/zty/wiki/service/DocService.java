/**
 * @author: zty
 * @program: wiki
 * @ClassName DocService
 * @description:
 * @create: 2021-11-20 12:24
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zty.wiki.domain.Doc;
import com.zty.wiki.domain.DocExample;
import com.zty.wiki.mapper.DocMapper;
import com.zty.wiki.req.DocQueryReq;
import com.zty.wiki.req.DocSaveReq;
import com.zty.wiki.resp.DocQueryResp;
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
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 分页查询
     */
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample example = new DocExample();
        example.setOrderByClause("sort asc");
        // 增加查询支持分页功能
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(example);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal()); //总行数
        LOG.info("总页数：{}", pageInfo.getPages()); //总页数
        // 使用工具类复制列表
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    /**
     * 全部查询
     * @return
     */
    public List<DocQueryResp> all() {
        DocExample example = new DocExample();
        example.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(example);
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req){
      Doc doc = CopyUtil.copy(req,Doc.class);
      if(ObjectUtils.isEmpty(req.getId())){
          //新增
          doc.setId(snowFlake.nextId());
          doc.setViewCount(0);
          doc.setVoteCount(0);
          docMapper.insert(doc);
      }else{
          // 更新
          docMapper.updateByPrimaryKey(doc);
      }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

}
