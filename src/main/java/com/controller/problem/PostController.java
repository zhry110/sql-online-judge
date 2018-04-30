package com.controller.problem;


import com.common.Const;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/post/")
public class PostController {
    @Autowired
    private UserPostService userPostService;
    @RequestMapping(value = "post.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getPosts(Long proId,HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CUR_USER);
        if (user == null)
            return ServerResponse.createByErrorMessage("未登录");
        Integer uid = user.getId();
        if (proId == null)
            return userPostService.getPosts(uid);
        return userPostService.getPosts(proId,uid);
    }
}
