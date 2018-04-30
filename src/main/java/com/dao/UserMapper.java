package com.dao;

import com.pojo.UidAndAccept;
import com.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String name);

    List<UidAndAccept> rank();

    Integer selfRank(Integer uid);
}