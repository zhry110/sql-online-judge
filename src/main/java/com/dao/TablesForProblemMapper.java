package com.dao;

import com.pojo.TablesForProblem;
import com.pojo.TablesForProblemKey;

import java.util.List;

public interface TablesForProblemMapper {
    int deleteByPrimaryKey(TablesForProblemKey key);

    int insert(TablesForProblem record);

    int insertSelective(TablesForProblem record);

    TablesForProblem selectByPrimaryKey(TablesForProblemKey key);

    int updateByPrimaryKeySelective(TablesForProblem record);

    int updateByPrimaryKey(TablesForProblem record);

    List<TablesForProblem> selectProblemTables(Long proId);

    int deleteByProId(Long proId);
}