package com.service;

import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import com.pojo.Admin;
import com.vo.ProblemDetailVo;

public interface ProblemsService {
    PageInfo getProblems(Long testId,Integer pageNum,Integer pageSize,Integer uid);
    ProblemDetailVo getProblemDetail(Long proId);
    ServerResponse judge(String sql,Long proId);
    ServerResponse add(String json, Admin admin);

    ServerResponse delete(Long proId);
}
