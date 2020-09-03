package com.xss.mapper;

import com.xss.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {


    /*
     * @param
     * @return com.xss.entity.User
     * @desc 查询我关注的人是否关注该文章
     */
    List<User> selectCollectUser(@Param("aid") Long aid, @Param("uid") Long uid);

}
