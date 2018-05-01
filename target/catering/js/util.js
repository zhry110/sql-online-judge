var gotoIndex = function (pageNum) {
    window.location.href = "index.html?pageNum="+pageNum;
}
var gotoRank = function (pageNum) {
    window.location.href = "rank.html?pageNum="+pageNum;
}
var gotoExam = function (pageNum) {
    window.location.href = "exam.html?pageNum="+pageNum;
}

var isAdmin = false;

var gotoPage = function (pageNum) {
    window.location.href = "index.html?pageNum="+pageNum;
}
var gotoProblemDetail = function (id) {
    window.location.href = "problem.html?id="+id;
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}

var logout = function () {
    $.get("/user/logout.do?",{},function (data) {
        location.reload();
    });
}

var getUserInfo = function () {
    $.get("/user/userinfo.do?",{},function (data) {
       console.log(data);
       if (data.status == 0)
       {
           $("#menuname").text(data.data.name);
           $("#menu").append("<li><a href=\"self.html\">"+data.data.name+"</a></li> <li class=\"divider\"></li>\n" +
               "                    <li><a style='color: #ff0000' href=\"javascript:logout()\">注销</a></li><li class=\"divider\"></li>\n" +
               "                    <li><a href=\"changepasswd.html\">修改密码</a></li>");
           if (data.data.admin) {
               isAdmin = true;
               $("#nav").append("<li class=\"dropdown\">\n" +
                   "                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">\n" +
                   "                        管理 <b class=\"caret\"></b>\n" +
                   "                    </a>\n" +
                   "                    <ul class=\"dropdown-menu\" id=\"admin_menu\">\n" +
                   "\n" + "<li><a href='addProblem.html'>添加题目</a></li>"+ "<li><a href='addExam.html'>添加考试</a></li>"+
                   "                    </ul>\n" +
                   "                </li>");
           }
       }
       else
       {
           $("#menuname").text("登录");
           $("#menu").append("<li><a href=\"register.html\">注册</a></li>");
           $("#menu").append("<li class=\"divider\"></li>");
           $("#menu").append("<li><a href=\"login.html\">登录</a></li>");
       }
    });
}

getUserInfo();

var getProblemsByPagefunction = function(pageNum) {
    $.get("/problems/problems.do?",{testId:0,pageSize:50,pageNum:pageNum},
        function (data) {
            $("#container").remove();
            var deleteTitle = "";
            var deleteStr = "";
            if (isAdmin) {
                deleteTitle = "<th>操作</th>";

            }
            $("#list").append("<div id=\"container\" style=\"padding-left: 1px;padding-right: 1px\">\n" +
                "        <table class=\"table table-bordered table-striped\" id=\"problems_container\">\n" +
                "            <thead>\n" +
                "            <tr>\n" +
                "                <th>\n" +
                "                    标题\n" +
                "                </th>\n" +
                "                <th>\n" +
                "                    题号\n" +
                "                </th>\n" +
                "\n" +
                "                <th>\n" +
                "                    分值\n" +
                "                </th>\n" +
                "                <th>\n" +
                "                    来源\n" +
                "                </th>\n" +
                "                <th>\n" +
                "                    合格率\n" +
                "                </th>\n" + deleteTitle+
                "            </tr>\n" +
                "            </thead>\n" +
                "            <tbody id = \"problems_wraper\">\n" +
                "\n" +
                "\n" +
                "            </tbody>\n" +
                "\n" +
                "        </table>\n" +
                "        <div align=\"right\" id=\"pages_container\">\n" +
                "            <ul class=\"pagination pagination-lg\" id = \"pages_wraper\">\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>");
            $("#pages_container").append("<ul class=\"pagination pagination-lg\" id = \"pages_wraper\">\n" +
                "        </ul>");
            $("#problems_container").append("<tbody id = \"problems_wraper\">\n" +
                "\n" +
                "\n" +
                "        </tbody>");
            console.log(data);
            for (var i = 0; i < data.data.list.length; i++) {
                if (isAdmin) {
                    deleteStr = "<td><a href='javascript:deleteProblem("+data.data.list[i].id+")'><span class=\"\" style=\"color: rgb(255, 0, 0);\"> x</span></a></td>";
                }
                var acc = "<span class=\"\" style=\"color: rgb(63, 181, 60);\"> &nbsp √</span>";
                if (data.data.list[i].accept == false)
                    acc = "";
                $("#problems_wraper").append("<tr> <td><a href=\"javascript:gotoProblemDetail("+data.data.list[i].id+")\">"+data.data.list[i].title+"</a>"+acc+"</td><td>"+data.data.list[i].id+"" +
                    "</td><td>"+data.data.list[i].score+"</td><td>"+data.data.list[i].sourse+"</td><td>"+data.data.list[i].correct+"</td>"+deleteStr);
            }
            var pages = data.data.pages;
            var curPage = data.data.pageNum;
            var start = parseInt((curPage-1) / 5);
            if (start != 0)
            {
                var jump = curPage - 5;
                if (jump < 1)
                    jump = 1;
                $("#pages_wraper").append(" <li><a href=\"javascript:gotoPage("+jump+")\">&laquo;</a></li>");
            }

            var remain = (curPage - 1) % 5;

            for (var i = 0; i < 5; i++)
            {
                if (((start * 5) + 1 + i) <= pages )
                {
                    if (curPage == (start * 5) + 1 + i)
                        $("#pages_wraper").append(" <li class='active'><a href=\"javascript:gotoPage("+((start * 5) + 1 + i)+")\">"+((start * 5) + 1 + i)+"</a></li>");
                    else
                        $("#pages_wraper").append(" <li><a href=\"javascript:gotoPage("+((start * 5) + 1 + i)+")\">"+((start * 5) + 1 + i)+"</a></li>");
                }

                else
                    $("#pages_wraper").append("<li><a>&nbsp;</a></li>");
            }
            if (((start * 5) + 1 + 4 ) < pages)
            {
                var jump = curPage + 5;
                if (jump > pages)
                    jump = pages;
                $("#pages_wraper").append(" <li><a href=\"javascript:gotoPage("+jump+")\">&raquo;</a></li>");
            }

        });
}

var deleteProblem = function (id) {
    $.get("/problems/delete.do?",{proId:id},function (data) {
        console.log(data);
        if (data.status == 0)
        {
            window.location.reload();
        }
        else
        {
            alert("删除失败"+data.msg);
        }
    });
}


$(function(){
    setInterval("getTime();",1000); //每隔一秒运行一次
})
//取得系统当前时间
function getTime(){
    var nowDate = new Date();
    var year = nowDate.getFullYear();
    var month = nowDate.getMonth()+1;
    month = month>9 ? month : "0" + month;
    var date = nowDate.getDate();
    date = date>9 ? date : "0" + date;
    var hour = nowDate.getHours();
    hour = hour>9 ? hour : "0" + hour;
    var miunte = nowDate.getMinutes();
    miunte = miunte>9 ? miunte : "0" + miunte;
    var second = nowDate.getSeconds();
    second = second>9 ? second : "0" + second;
    $("#showDate").text(hour+":"+miunte+":"+second);
}