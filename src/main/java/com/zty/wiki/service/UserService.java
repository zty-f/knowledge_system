/**
 * @author: zty
 * @program: wiki
 * @ClassName UserService
 * @description:
 * @create: 2021-11-20 12:24
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zty.wiki.domain.User;
import com.zty.wiki.domain.UserExample;
import com.zty.wiki.exception.BusinessException;
import com.zty.wiki.exception.BusinessExceptionCode;
import com.zty.wiki.mapper.UserMapper;
import com.zty.wiki.req.UserLoginReq;
import com.zty.wiki.req.UserQueryReq;
import com.zty.wiki.req.UserResetPasswordReq;
import com.zty.wiki.req.UserSaveReq;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.resp.UserLoginResp;
import com.zty.wiki.resp.UserQueryResp;
import com.zty.wiki.util.CopyUtil;
import com.zty.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 分页查询
     */
    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }
        // 增加查询支持分页功能
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(example);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal()); //总行数
        LOG.info("总页数：{}", pageInfo.getPages()); //总页数
        // 使用工具类复制列表
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setList(respList);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    public List<UserQueryResp> all() {
        UserExample example = new UserExample();
        List<User> userList = userMapper.selectByExample(example);
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        return respList;
    }

    /**
     * 编辑 保存
     */
    public void save(UserSaveReq req){
      User user = CopyUtil.copy(req,User.class);
      if(ObjectUtils.isEmpty(req.getId())){
          //新增
          if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
              user.setId(snowFlake.nextId());
              userMapper.insert(user);
          }else {
            // 用户名已存在
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
          }
      }else{
          // 更新
          user.setLoginName(null);
          user.setPassword(null);
          userMapper.updateByPrimaryKeySelective(user);
      }
    }

    /**
     * 删除
     */
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(example);
        if(ObjectUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }

    /**
     * 修改密码
     */
    public void resetPassword(UserResetPasswordReq req){
        User user = CopyUtil.copy(req,User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登录
     */
    public UserLoginResp login(UserLoginReq req){
        User userDb = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDb)){
            // 用户名不存在
            LOG.info("用户名不存在，{}",req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if (userDb.getPassword().equals(req.getPassword())){
                // 登录成功
                UserLoginResp userLoginResp=CopyUtil.copy(userDb,UserLoginResp.class);
                return userLoginResp;
            }else {
                // 密码错误
                LOG.info("密码错误，输入密码：{}，数据库密码：{}",req.getPassword(),userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
