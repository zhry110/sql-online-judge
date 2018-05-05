package com.service.impl;

import com.common.ServerResponse;
import com.dao.ExamMapper;
import com.pojo.Exam;
import com.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ExamService")
public class ExamServiceImpl implements ExamService{
    @Autowired
    private ExamMapper examMapper;
    @Override
    public ServerResponse getExamNumbers() {
        return ServerResponse.createBySuccess(examMapper.getExams());
    }

    @Override
    public ServerResponse getExams() {
        List<Exam> exams = examMapper.getExams();
        return ServerResponse.createBySuccess(exams);
    }

    @Override
    public ServerResponse addExam(String name, Integer full, Integer pass, Date startDate, Date endDate) {
        Exam exam = new Exam();
        exam.setName(name);
        exam.setFullScore(full);
        exam.setPassScore(pass);
        exam.setStartTime(startDate);
        exam.setEndTime(endDate);
        if (examMapper.insert(exam) > 0 )
            return ServerResponse.createBySuccess();
        return ServerResponse.createByErrorMessage("插入考试失败");
    }

    @Override
    public ServerResponse getExam(Integer examId) {
        return ServerResponse.createBySuccess(examMapper.selectByPrimaryKey(examId));
    }
}
