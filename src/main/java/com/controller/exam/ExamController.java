package com.controller.exam;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.ExamService;
import com.util.DateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/exam/")
public class ExamController {
    @Autowired
    private ExamService examService;
    @RequestMapping(value = "examNumbers.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAllExamNumber() {
        return examService.getExamNumbers();
    }
    @RequestMapping(value = "exams.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getExams() {
        return examService.getExams();
    }
    @RequestMapping(value = "addExam.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addExam(String name, Integer full, Integer pass, String start, String end, HttpSession session) {
        if (name == null)
            return ServerResponse.createByErrorMessage("name is null");
        if (name.equals(""))
            return ServerResponse.createByErrorMessage("name 未输入");

        if (full == null)
            return ServerResponse.createByErrorMessage("full is null");
        if (pass == null)
            return ServerResponse.createByErrorMessage("pass is null");

        if (start == null)
            return ServerResponse.createByErrorMessage("start is null");
        if (start.equals(""))
            return ServerResponse.createByErrorMessage("start 未输入");

        if (end == null)
            return ServerResponse.createByErrorMessage("end is null");
        if (end.equals(""))
            return ServerResponse.createByErrorMessage("end 未输入");

        Date startDate = null,endDate = null;
        try {
            System.out.println(start);
            System.out.println(end);
            startDate = DateParser.strToDateLong(start);
            endDate = DateParser.strToDateLong(end);
            if (startDate == null || endDate == null)
                return ServerResponse.createByErrorMessage("非法的时间格式");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByErrorMessage("未登录");
        if (!user.isAdmin()) {
            return ServerResponse.createByErrorMessage("无权限");
        }
        return examService.addExam(name,full,pass,startDate,endDate);
    }

}
