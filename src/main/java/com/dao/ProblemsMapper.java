package com.dao;

import com.pojo.Problems;

import java.util.List;

public interface ProblemsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Problems record);

    int insertSelective(Problems record);

    Problems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Problems record);

    int updateByPrimaryKeyWithBLOBs(Problems record);

    int updateByPrimaryKey(Problems record);

    List<Problems> getAllProblems(Long testId);
}