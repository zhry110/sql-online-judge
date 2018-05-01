package com.service.impl;

import com.common.ServerResponse;
import com.dao.ExamMapper;
import com.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExamServiceImpl implements ExamService{
    @Autowired
    private ExamMapper examMapper;
    @Override
    public ServerResponse getExamNumbers() {
        return ServerResponse.createBySuccess(examMapper.getExamNumbers());
    }
}
