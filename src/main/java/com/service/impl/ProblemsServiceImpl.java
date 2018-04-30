package com.service.impl;

import com.common.ServerResponse;
import com.core.AccessJudge;
import com.core.Judge;
import com.core.Problem;
import com.dao.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.*;
import com.service.ProblemsService;
import com.util.TableInfo;
import com.vo.ProblemDetailVo;
import com.vo.ProblemsVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service("ProblemsService")
public class ProblemsServiceImpl implements ProblemsService {
    @Autowired
    private ProblemsMapper problemsMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserAcceptMapper userAcceptMapper;
    @Autowired
    private TablesForProblemMapper tablesForProblemMapper;
    @Autowired
    private AnswerForProblemsMapper answerForProblemsMapper;
    @Autowired
    private TypeForProblemsMapper typeForProblemsMapper;
    @Autowired
    private ProblemsForExamMapper problemsForExamMapper;
    @Override
    public PageInfo getProblems(Long testId,Integer pageNum,Integer pageSize,Integer uid) {

        PageHelper.startPage(pageNum,pageSize);
        List<Problems> lists =  problemsMapper.getAllProblems(testId);
        List<ProblemsVo> res = new ArrayList<>();
        for (Problems p : lists)
        {
            ProblemsVo passListVo = new ProblemsVo();
            passListVo.setId(p.getId());
            passListVo.setScore(p.getScore());
            passListVo.setTitle(p.getTitle());
            Admin admin = adminMapper.selectByPrimaryKey(p.getAdmin());
            if (admin != null)
            {
                passListVo.setSourse(admin.getName());
            } else
            {
                passListVo.setSourse(" ");
            }
            Long allCount = userAcceptMapper.allCount(p.getId());
            Long acCount = userAcceptMapper.acCount(p.getId());
            if (allCount != null && acCount != null) {
                DecimalFormat df = new DecimalFormat("#.00");

                if (allCount == 0)
                    passListVo.setCorrect("0%");
                else
                    passListVo.setCorrect(df.format((acCount / (allCount + 0.0)) * 100) + "%");
            }
            if (uid != null)
                if (userAcceptMapper.selectAccept(p.getId(),uid) > 0)
                    passListVo.setAccept(true);
            res.add(passListVo);
        }
        PageInfo pageResult = new PageInfo(lists);
        pageResult.setList(res);
        return pageResult;
    }

    @Override
    public ProblemDetailVo getProblemDetail(Long proId) {
        Problems problem = problemsMapper.selectByPrimaryKey(proId);
        if (problem == null)
            return null;
        ProblemDetailVo problemDetailVo = new ProblemDetailVo();
        problemDetailVo.setDescription(problem.getDescription());
        problemDetailVo.setId(proId);
        problemDetailVo.setScore(problem.getScore());
        problemDetailVo.setTitle(problem.getTitle());
        //problemDetailVo.setTable(TableInfo.getTableInfo("exam"));
        List<TablesForProblem> list = tablesForProblemMapper.selectProblemTables(proId);
        String tableInfo = "";
        if (list != null)
        {
            for (TablesForProblem tablesForProblem : list)
            {
                tableInfo += TableInfo.getTableInfo(tablesForProblem.getUserTableName(),proId);
            }
        }

        //获取答案
        AnswerForProblems answerForProblems = answerForProblemsMapper.selectByPrimaryKey(proId);
        TypeForProblems typeForProblems = typeForProblemsMapper.selectByPrimaryKey(proId);

        tableInfo += "<br>对于上述测试用例，正确输出为：";
        if (answerForProblems == null) {
            tableInfo += "<br>无法获取输出，该题目答案sql为空";
        } else {
            if (typeForProblems != null) {
                if (typeForProblems.getType()) {
                    tableInfo += "<br>非SELECT题型，无输出";
                } else {
                    tableInfo += ("<br>" + new Problem(proId).getAnswer(answerForProblems.getAnswer()));
                }
            }

        }
        problemDetailVo.setTable(tableInfo);
        return problemDetailVo;
    }

    @Override
    public ServerResponse judge(String sql, Long proId) {

        return null;
    }

    @Override
    public ServerResponse add(String json, Admin admin) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String title = jsonObject.getString("title");
            if (checkNull(title)) {
                return ServerResponse.createByErrorMessage("title 不能为空");
            }
            Integer score = jsonObject.getInt("score");
            System.out.println("" + score);
            if (score == null)
                return ServerResponse.createByErrorMessage("score 不能为空");
            String answer = jsonObject.getString("answer");
            if (checkNull(answer)) {
                return ServerResponse.createByErrorMessage("answer 不能为空");
            }
            String des = jsonObject.getString("des");
            if (checkNull(des)) {
                return ServerResponse.createByErrorMessage("des 不能为空");
            }
            int test = jsonObject.getInt("test");
            boolean select = jsonObject.getBoolean("select");

            JSONArray tables = jsonObject.getJSONArray("tables");
            String[] tableNames = new String[tables.length()];
            for (int i = 0; i < tables.length();i++) {
                String tableName = tables.getString(i);
                if (checkNull(tableName))
                    return ServerResponse.createByErrorMessage("操作表名称不能为空");
                tableNames[i] = tableName;
            }

            JSONArray tableSqls = jsonObject.getJSONArray("tablesqls");
            String[] tableSqlsSqls = new String[tableSqls.length()];
            for (int i = 0; i < tableSqls.length();i++) {
                String tableSql = tableSqls.getString(i);
                if (checkNull(tableSql))
                    return ServerResponse.createByErrorMessage("操作表创建语句不能为空");
                tableSqlsSqls[i] = tableSql;
            }

            JSONArray testCases = jsonObject.getJSONArray("testCase");
            String[] testCaseSqls = new String[testCases.length()];
            for (int i = 0; i < testCases.length();i++) {
                String testCase = testCases.getString(i);
                if (checkNull(testCase))
                    return ServerResponse.createByErrorMessage("测试用例不能为空");
                testCaseSqls[i] = testCase;
            }

            Problems problems = new Problems();
            problems.setId(null);
            problems.setTitle(title);
            problems.setAdmin(0);
            problems.setDescription(des);
            problems.setScore(score);
            if (problemsMapper.insert(problems) < 0) {
                return ServerResponse.createByErrorMessage("插入题目至数据库时遇到错误");
            }
            //插入题目至题目数据库成功

            //开始创建题目数据库
            String problemDatabaseName = Judge.getProblemDatabaseName(problems.getId());
            ServerResponse response = Judge.createDatabase(problemDatabaseName);
            if (!response.isSuccess()) {
                problemsMapper.deleteByPrimaryKey(problems.getId());
                return ServerResponse.createByErrorMessage("为题目创建数据库失败 原因:"+response.getMsg());
            }

            //创建测试用例
            for (int testCase = 0; testCase < testCaseSqls.length;testCase++) {
                response = createTables(tableNames,problemDatabaseName,tableSqlsSqls,problems);
                if (!response.isSuccess()) {
                    problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                    if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                        System.out.println("ERROR in drop Database");
                    }
                    return ServerResponse.createByErrorMessage("创建测试用例"+(testCase + 1)+"时，创建表失败 "+response.getMsg());
                }
                List<String> sqls = AccessJudge.getLines(testCaseSqls[testCase]);
                sqls.add(0,"use "+problemDatabaseName);
                response = Judge.execSql(sqls);
                if (!response.isSuccess()) {
                    problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                    if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                        System.out.println("ERROR in drop Database");
                    }
                    return ServerResponse.createByErrorMessage("创建测试用例"+(testCase + 1)+"时，运行测试用例生成sql时失败"+response.getMsg());
                }
                response = copyTables(tableNames,problemDatabaseName,testCase);
                if (!response.isSuccess()) {
                    problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                    if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                        System.out.println("ERROR in drop Database");
                    }
                    return ServerResponse.createByErrorMessage("创建测试用例"+(testCase + 1)+"时，"+response.getMsg());
                }
            }

            //判断answer 是否可以运行
            String[] sqls = {"use "+problemDatabaseName,answer};
            response = Judge.execSql(sqls);
            if (!response.isSuccess()) {
                problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                    System.out.println("ERROR in drop Database");
                }
                return ServerResponse.createByErrorMessage("给出的answer无法运行"+response.getMsg());
            }

            //如果改变表数据 则恢复
            if (!select) {
                response = restoreTables(tableNames,problemDatabaseName,testCaseSqls.length - 1);
                if (!response.isSuccess()) {
                    problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                    if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                        System.out.println("ERROR in drop Database");
                    }
                    return ServerResponse.createByErrorMessage(response.getMsg());
                }
            }


            //插入题目类型
            TypeForProblems typeForProblems = new TypeForProblems();
            typeForProblems.setProId(problems.getId());
            typeForProblems.setType(!select);
            if (typeForProblemsMapper.insert(typeForProblems) < 1) {
                problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                    System.out.println("ERROR in drop Database");
                }
                return ServerResponse.createByErrorMessage("添加题目类型至类型表失败");
            }

            //绑定题目到考试
            ProblemsForExamKey problemsForExamKey = new ProblemsForExamKey();
            problemsForExamKey.setProId(problems.getId().intValue()); // TODO: 2018/4/30 change Integer to Long
            problemsForExamKey.setExamId(test);
            if (problemsForExamMapper.insert(problemsForExamKey) < 1) {
                problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                    System.out.println("ERROR in drop Database");
                }
                return ServerResponse.createByErrorMessage("绑定题目至考试失败");
            }

            //添加操作表至 TablesForProblem
            for (int i = 0; i < tableNames.length;i++) {
                TablesForProblem tablesForProblem = new TablesForProblem();
                tablesForProblem.setAccess(0);
                tablesForProblem.setProId(problems.getId().intValue());// TODO: 2018/4/30 change Integer to Long
                tablesForProblem.setUserTableName(tableNames[i]);
                if (tablesForProblemMapper.insert(tablesForProblem) < 1) {
                    problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                    if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                        System.out.println("ERROR in drop Database");
                    }
                    return ServerResponse.createByErrorMessage("添加题目操作表至数据库失败");
                }
            }

            //添加答案至 answer表

            AnswerForProblems answerForProblems = new AnswerForProblems();
            answerForProblems.setProId(problems.getId());
            answerForProblems.setAnswer(answer);

            if (answerForProblemsMapper.insert(answerForProblems) < 1) {
                problemsMapper.deleteByPrimaryKey(problems.getId());//删除题目
                if (!Judge.dropDatabase(problemDatabaseName).isSuccess()) { //删除题目数据库
                    System.out.println("ERROR in drop Database");
                }
                return ServerResponse.createByErrorMessage("添加答案至答案表失败");
            }

        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        return ServerResponse.createBySuccess();
    }
    private ServerResponse createTables(String[] tableNames,String problemDatabaseName,String[] tableSqlsSqls,Problems problems) {
        ServerResponse response;
        for (int i = 0; i < tableNames.length;i++) {
            response = Judge.dropTable(problemDatabaseName,tableNames[i]);
            if (!response.isSuccess()) {
                return ServerResponse.createByErrorMessage(tableNames[i]+"失败 原因:"+response.getMsg());
            }
            response = Judge.createTableWithSql(problemDatabaseName,tableSqlsSqls[i]);
            if (!response.isSuccess()) {
                return ServerResponse.createByErrorMessage("创建操作表"+tableNames[i]+"失败 原因:"+response.getMsg());
            }
        }
        return ServerResponse.createBySuccess();
    }
    private ServerResponse copyTables(String[] tableNames,String problemDatabaseName,int testCase) {
        ServerResponse response;
        for (String tableName : tableNames) {
            response = Judge.copyTable(problemDatabaseName,tableName,problemDatabaseName,tableName + testCase);
            if (!response.isSuccess()) {
                return ServerResponse.createByErrorMessage("拷贝表发送错误 "+response.getMsg());
            }
        }
        return ServerResponse.createBySuccess();
    }
    private ServerResponse restoreTables(String[] tableNames,String problemDatabaseName,int testCase) {
        ServerResponse response;
        for (String tableName : tableNames) {
            response = Judge.copyTable(problemDatabaseName,tableName + testCase,
                    problemDatabaseName,tableName);
            if (!response.isSuccess()) {
                return ServerResponse.createByErrorMessage("拷贝表发送错误 "+response.getMsg());
            }
        }
        return ServerResponse.createBySuccess();
    }
    private boolean checkNull(String string) {
        if (string == null) {
            return true;
        } else if (string.equals("")) {
            return true;
        }
        return false;
    }
}
