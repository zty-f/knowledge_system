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
import com.zty.wiki.req.EbookReq;
import com.zty.wiki.resp.EbookResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.util.CopyUtil;
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

    public PageResp<EbookResp> list(EbookReq req) {
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
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public List<EbookResp> all() {
        EbookExample example = new EbookExample();
        List<Ebook> ebookList = ebookMapper.selectByExample(example);
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);
        return respList;
    }

}
