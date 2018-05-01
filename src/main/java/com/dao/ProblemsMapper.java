package com.dao;

import com.pojo.Problems;
import com.pojo.ProblemsWithBLOBs;

import java.util.List;

public interface ProblemsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProblemsWithBLOBs record);

    int insertSelective(ProblemsWithBLOBs record);

    ProblemsWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProblemsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ProblemsWithBLOBs record);

    int updateByPrimaryKey(Problems record);

    List<Problems> getAllProblems(Long testId);
}