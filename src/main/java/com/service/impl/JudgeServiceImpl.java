package com.service.impl;

import com.common.ServerResponse;
import com.dao.ProblemsMapper;
import com.pojo.ProblemsWithBLOBs;
import com.service.AccessJudgeService;
import com.service.JudgeService;
import com.service.ReadOnlyJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("JudgeService")
public class JudgeServiceImpl implements JudgeService{
    @Autowired
    private ReadOnlyJudgeService readOnlyJudgeService;
    @Autowired
    private ProblemsMapper problemsMapper;

    @Autowired
    private AccessJudgeService accessJudgeService;
    @Override
    public ServerResponse doJudge(String sql, Integer userId, Long proId) {
        ProblemsWithBLOBs problems = problemsMapper.selectByPrimaryKey(proId);
        if (problems == null)
            return ServerResponse.createByErrorMessage("题目不存在");
        Boolean noSelect = problems.getType();
        if (noSelect == null)
            return ServerResponse.createByErrorMessage("题目类型未知，无法判定，请联系管理员");
        if (!problems.getType()) {
            return readOnlyJudgeService.judge(sql,userId,proId);
        } else {
            return accessJudgeService.judge(sql,userId,proId);
        }
    }
}
