package com.service.impl;

import com.common.ServerResponse;
import com.dao.TypeForProblemsMapper;
import com.pojo.TypeForProblems;
import com.service.AccessJudgeService;
import com.service.JudgeService;
import com.service.ReadOnlyJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("JudgeService")
public class JudgeServiceImpl implements JudgeService{
    @Autowired
    private TypeForProblemsMapper typeForProblemsMapper;

    @Autowired
    private ReadOnlyJudgeService readOnlyJudgeService;

    @Autowired
    private AccessJudgeService accessJudgeService;
    @Override
    public ServerResponse doJudge(String sql, Integer userId, Long proId) {
        TypeForProblems typeForProblems = typeForProblemsMapper.selectByPrimaryKey(proId);
        if (typeForProblems == null) {
            return ServerResponse.createByErrorMessage("题目类型未知 无法判定");
        }
        if (!typeForProblems.getType()) {
            return readOnlyJudgeService.judge(sql,userId,proId);
        } else {
            return accessJudgeService.judge(sql,userId,proId);
        }
    }
}
