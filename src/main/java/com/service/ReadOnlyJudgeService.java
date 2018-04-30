package com.service;

import com.common.ServerResponse;

public interface ReadOnlyJudgeService {
    ServerResponse judge(String sql,Integer userId,Long proId);
}
