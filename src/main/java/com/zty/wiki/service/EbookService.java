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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(example);
        ArrayList<EbookResp> respList = new ArrayList<>();

        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;

    }

}
