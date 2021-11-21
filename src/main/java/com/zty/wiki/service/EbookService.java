/**
 * @author: zty
 * @program: wiki
 * @ClassName EbookService
 * @description:
 * @create: 2021-11-20 12:24
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.zty.wiki.domain.Ebook;
import com.zty.wiki.domain.EbookExample;
import com.zty.wiki.mapper.EbookMapper;
import com.zty.wiki.req.EbookReq;
import com.zty.wiki.resp.EbookResp;
import com.zty.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(example);
        // 使用工具类复制列表
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);
        return respList;

    }

}
