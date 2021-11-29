/**
 * @author: zty
 * @program: wiki
 * @ClassName CategoryService
 * @description:
 * @create: 2021-11-20 12:24
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zty.wiki.domain.Category;
import com.zty.wiki.domain.CategoryExample;
import com.zty.wiki.mapper.CategoryMapper;
import com.zty.wiki.req.CategoryQueryReq;
import com.zty.wiki.req.CategorySaveReq;
import com.zty.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 分页查询
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        // 增加查询支持分页功能
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(example);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal()); //总行数
        LOG.info("总页数：{}", pageInfo.getPages()); //总页数
        // 使用工具类复制列表
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    /**
     * 全部查询
     * @return
     */
    public List<CategoryQueryResp> all(CategoryQueryReq req) {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Category> categoryList = categoryMapper.selectByExample(example);
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return respList;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req){
      Category category = CopyUtil.copy(req,Category.class);
      if(ObjectUtils.isEmpty(req.getId())){
          //新增
          category.setId(snowFlake.nextId());
          categoryMapper.insert(category);
      }else{
          // 更新
          categoryMapper.updateByPrimaryKey(category);
      }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

}
