package com.service;

import com.common.ServerResponse;

public interface AccessJudgeService {
    ServerResponse judge(String sql, Integer userId, Long proId);
}
