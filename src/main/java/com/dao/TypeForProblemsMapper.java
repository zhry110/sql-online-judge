package com.dao;

import com.pojo.TypeForProblems;

public interface TypeForProblemsMapper {
    int deleteByPrimaryKey(Long proId);

    int insert(TypeForProblems record);

    int insertSelective(TypeForProblems record);

    TypeForProblems selectByPrimaryKey(Long proId);

    int updateByPrimaryKeySelective(TypeForProblems record);

    int updateByPrimaryKey(TypeForProblems record);
}