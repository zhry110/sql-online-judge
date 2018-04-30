package com.service.impl;

import com.common.ServerResponse;
import com.core.Judge;
import com.dao.AnswerForProblemsMapper;
import com.dao.ProblemsMapper;
import com.dao.TablesForProblemMapper;
import com.pojo.AnswerForProblems;
import com.pojo.Problems;
import com.pojo.TablesForProblem;
import com.service.AccessJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AccessJudgeService")
public class AccessJudgeServiceImpl implements AccessJudgeService {
    @Autowired
    private ProblemsMapper problemsMapper;
    @Autowired
    private TablesForProblemMapper tablesForProblemMapper;
    @Autowired
    private AnswerForProblemsMapper answerForProblemsMapper;

    @Override
    public ServerResponse judge(String sql, Integer userId, Long proId) {
        if (userId == null)
            return ServerResponse.createByErrorMessage("ERROR 未知用户ID");
        Problems problems = problemsMapper.selectByPrimaryKey(proId);
        if (problems == null) {
            return ServerResponse.createByErrorMessage("题目不存在");
        }
        List<TablesForProblem> tables = tablesForProblemMapper.selectProblemTables(proId);
        if (tables == null) {
            return ServerResponse.createByErrorMessage("该题目无操作表 无法判定 请联系出题者");
        }
        if (tables.isEmpty()) {
            return ServerResponse.createByErrorMessage("该题目无操作表 无法判定 请联系出题者");
        }
        String[] tableNames = new String[tables.size()];
        for (int i = 0; i < tableNames.length;i++) {
            tableNames[i] = tables.get(i).getUserTableName();
        }

        AnswerForProblems answerForProblems = answerForProblemsMapper.selectByPrimaryKey(proId);

        if (answerForProblems == null)
            return ServerResponse.createByErrorMessage("该题目缺少正确sql 无法判断");
        String answer = answerForProblems.getAnswer();
        if (answer == null) {
            return ServerResponse.createByErrorMessage("该题目缺少正确sql 无法判断");
        }

        //为判定器创建判定环境
        ServerResponse response = prepareJudgeEnvironment(userId);
        if (!response.isSuccess()) {
            return response;
        }
        return Judge.copyTestCaseAndJudge(tableNames,proId,true,sql,answer,userId);
    }
    private ServerResponse prepareJudgeEnvironment(Integer userId){
        ServerResponse response = Judge.createDatabase(Judge.getUserDatabaseName(userId));
        if (!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("系统故障 无法创建数据库");
        }
        response = Judge.createDatabase(Judge.getUserSystemDatabaseName(userId));
        if (!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("系统故障 无法创建数据库");
        }
        return ServerResponse.createBySuccess();
    }



}
