package com.controller.problem;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.JudgeService;
import com.service.ProblemsService;
import com.service.UserPostService;
import com.vo.ProblemDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/problems/")
public class ProblemController {
    @Autowired
    private ProblemsService problemsService;

    @Autowired
    private UserPostService userPostService;

    @Autowired
    private JudgeService judgeService;
    @RequestMapping(value = "problems.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProblems(Long testId, Integer pageSize,Integer pageNum,HttpSession session)
    {
        if (testId == null || pageNum == null || pageSize == null)
            return ServerResponse.createByErrorMessage("null request");
        User user = (User) session.getAttribute(Const.CUR_USER);
        Integer uid = null;
        if (user != null)
            uid = user.getId();
        return ServerResponse.createBySuccess(problemsService.getProblems(testId,pageNum,pageSize,uid));
    }
    @RequestMapping(value = "problem.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProblemDetail(Long proId)
    {
        if (proId == null)
            return ServerResponse.createByErrorMessage("null request");
        ProblemDetailVo problemDetailVo = problemsService.getProblemDetail(proId);
        return ServerResponse.createBySuccess(problemDetailVo);
    }
    @RequestMapping(value = "judge.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse judge(String sql, HttpSession session,Long proId)
    {
        if (sql == null)
            return ServerResponse.createByErrorMessage("null request");
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByErrorMessage("未登录");
        if (proId == null)
            return ServerResponse.createByErrorMessage("题目不可用");
        System.out.println(sql);
        if (user.isJudgeing()) {
            return ServerResponse.createByErrorMessage("你的上一个提交正在判定，请稍等");
        }
        user.setJudgeing(true);
        ServerResponse response = judgeService.doJudge(sql, user.getId(), proId);
        userPostService.postSql(proId,user.getId(),sql,response.isSuccess());
        user.setJudgeing(false);
        return response;
    }
    @RequestMapping(value = "add.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse add(String json, HttpSession session){
        if (json == null)
            return ServerResponse.createByErrorMessage("NULL post");
        System.out.println(json);
        return problemsService.add(json,null);
    }

}
