/**
 * @author: zty
 * @program: wiki
 * @description:
 * @create: 2021-11-20 11:33
 * @Version 1.0
 **/
package com.zty.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {
     void increaseViewCount(@Param("id") Long id);

     void increaseVoteCount(@Param("id") Long id);
}
