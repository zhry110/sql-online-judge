package com.dao;

import com.pojo.AnswerForProblems;

public interface AnswerForProblemsMapper {
    int deleteByPrimaryKey(Long proId);

    int insert(AnswerForProblems record);

    int insertSelective(AnswerForProblems record);

    AnswerForProblems selectByPrimaryKey(Long proId);

    int updateByPrimaryKeySelective(AnswerForProblems record);

    int updateByPrimaryKeyWithBLOBs(AnswerForProblems record);
}