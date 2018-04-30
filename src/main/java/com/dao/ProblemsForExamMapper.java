package com.dao;

import com.pojo.ProblemsForExamKey;

public interface ProblemsForExamMapper {
    int deleteByPrimaryKey(ProblemsForExamKey key);

    int insert(ProblemsForExamKey record);

    int insertSelective(ProblemsForExamKey record);


}