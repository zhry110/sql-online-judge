package com.service;

import com.common.ServerResponse;

import java.util.Date;

public interface ExamService {
    ServerResponse getExamNumbers();

    ServerResponse getExams();

    ServerResponse addExam(String name, Integer full, Integer pass, Date startDate, Date endDate);
}
