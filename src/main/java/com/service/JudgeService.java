package com.service;

import com.common.ServerResponse;

public interface JudgeService {
    ServerResponse doJudge(String sql,Integer userId,Long proId);
}
