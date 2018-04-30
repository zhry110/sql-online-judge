package com.dao;

import com.pojo.User;
import com.pojo.UserAccept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAcceptMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAccept record);

    int insertSelective(UserAccept record);

    UserAccept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAccept record);

    int updateByPrimaryKeyWithBLOBs(UserAccept record);

    int updateByPrimaryKey(UserAccept record);

    Long allCount(Long proId);

    Long acCount(Long proId);

    List<UserAccept> problemAndUserPost(@Param("proId") Long proId, @Param("uid") Integer uid);

    List<UserAccept> userPost(Integer uid);

    int selectAccept(@Param("proId")Long proId,@Param("uid")Integer uid);

    Integer userAcceptCount(Integer uid);
}