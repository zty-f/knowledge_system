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
import com.zty.wiki.domain.Content;
import com.zty.wiki.domain.Doc;
import com.zty.wiki.domain.DocExample;
import com.zty.wiki.exception.BusinessException;
import com.zty.wiki.exception.BusinessExceptionCode;
import com.zty.wiki.mapper.ContentMapper;
import com.zty.wiki.mapper.DocMapper;
import com.zty.wiki.mapper.DocMapperCust;
import com.zty.wiki.req.DocQueryReq;
import com.zty.wiki.req.DocSaveReq;
import com.zty.wiki.resp.DocQueryResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.util.CopyUtil;
import com.zty.wiki.util.RedisUtil;
import com.zty.wiki.util.RequestContext;
import com.zty.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WebSocketService webSocketService;

    //@Resource
    //private RocketMQTemplate rocketMQTemplate;

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
    public List<DocQueryResp> all(Long ebookId) {
        DocExample example = new DocExample();
        example.createCriteria().andEbookIdEqualTo(ebookId);
        example.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(example);
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }

    /**
     * 保存
     */
    @Transactional //事务处理！！！
    public void save(DocSaveReq req){
      Doc doc = CopyUtil.copy(req,Doc.class);
      Content content = CopyUtil.copy(req,Content.class);
      if(ObjectUtils.isEmpty(req.getId())){
          //新增
          doc.setId(snowFlake.nextId());
          doc.setViewCount(0);
          doc.setVoteCount(0);
          docMapper.insert(doc);
          //新增内容
          content.setId(doc.getId());
          contentMapper.insert(content);
      }else{
          // 更新
          docMapper.updateByPrimaryKey(doc);

          //更新内容
          int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
          if(count==0){
              contentMapper.insert(content);
          }
      }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }
    //方法重载
    public void delete(List<String> ids){
        DocExample example = new DocExample();
        DocExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(example);
    }

    /**
     * 查找文档内容
     */
    public String findContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        }else{
            return content.getContent();
        }
    }

    /**
     * 投票
     */
    public void vote(Long id){
        //远程ip+doc.id作为key，24小时不能重复
        String ip = RequestContext.getRemoteAddr();
        if(redisUtil.validateRepeat("DOC_VOTE_"+id+"_"+ip,3600*24)){
            docMapperCust.increaseVoteCount(id);
        }else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        //推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId= MDC.get("LOG_ID");
        webSocketService.sendInfo("【"+docDb.getName()+"】被点赞！",logId);
        //rocketMQTemplate.convertAndSend("VOTE_TOPIC","【"+docDb.getName()+"】被点赞！");
    }
    /**
     * 定时更新电子书信息
     */
    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }
}
