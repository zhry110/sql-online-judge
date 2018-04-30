package com.service.impl;

import com.common.ServerResponse;
import com.core.Judge;
import com.dao.AnswerForProblemsMapper;
import com.dao.ProblemsMapper;
import com.dao.TablesForProblemMapper;
import com.pojo.AnswerForProblems;
import com.pojo.Problems;
import com.pojo.TablesForProblem;
import com.service.ReadOnlyJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ReadOnlyJudgeService")
public class ReadOnlyJudgeServiceImpl implements ReadOnlyJudgeService {
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
        if (problems == null)
            return ServerResponse.createByErrorMessage("题目不存在");
        List<TablesForProblem> lists = tablesForProblemMapper.selectProblemTables(proId);
        if (lists.isEmpty())
            return ServerResponse.createByErrorMessage("该题目无测试表");
        String[] tables = new String[lists.size()];
        for (int i = 0; i < tables.length; i++)
            tables[i] = lists.get(i).getUserTableName();

        AnswerForProblems answerForProblems = answerForProblemsMapper.selectByPrimaryKey(proId);
        if (answerForProblems == null)
            return ServerResponse.createByErrorMessage("该题目缺少正确sql 无法判断");
        String answer = answerForProblems.getAnswer();
        if (answer == null)
            return ServerResponse.createByErrorMessage("该题目缺少正确sql 无法判断");

        ServerResponse response = Judge.createDatabase(userId);//创建数据库
        if (response.isSuccess()) {
            ServerResponse serverResponse = Judge.copyTestCaseAndJudge(tables,proId,false,sql,answer,userId);
            return serverResponse;
        } else {
            System.out.println("为用户创建数据库失败");
            return response;
        }
    }
}
