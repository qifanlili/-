package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.student.UserAPI;
import com.stylefeng.guns.api.student.vo.*;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/gra/student/")
@RestController
public class UserController {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    /**
     * 显示所有题目和我的志愿里的题目
     * @return
     */
    @GetMapping(value = "getAllSubjects")
    public ResponseVO getAllSubjects(){
        List<SubjectInfoVO> allSubjects = userAPI.getAllSubjects();
//        List<SubjectInfoVO> subjectsByTeamId = userAPI.getSubjectsByTeamId(teamId);
        if(allSubjects == null || allSubjects.size() <= 0){
            return ResponseVO.serviceFail("暂无题目");
        }
        //转VO
        SubjectListVO subjectListVO = new SubjectListVO();
        subjectListVO.setAllSubjects(allSubjects);
//        subjectListVO.setSubjectsWithTeam(subjectsByTeamId);

        return ResponseVO.success(subjectListVO);
    }

    @GetMapping(value = "searchSubjects")
    public ResponseVO searchSubjects(String keyword){
        List<SubjectInfoVO> subjectsByKeword = userAPI.getSubjectsByKeword(keyword);
        if (subjectsByKeword == null || subjectsByKeword.size() <=0){
            return ResponseVO.serviceFail("暂无相关题目");
        }
        return ResponseVO.success(subjectsByKeword);
    }

    @GetMapping(value = "toIntroduce")
    public ResponseVO toIntroduce(int userId,String introduce){
//        // 获取当前登陆用户
//        String userId = CurrentUser.getCurrentUser();
//        System.out.println("(((((((((((((((((((((((((((((9"+userId);
        if(userId <= 0){
            return ResponseVO.serviceFail("未登录");
        }
        int res = userAPI.saveORUpdateIntroduce(userId, introduce);
        if(res <= 0){
            return ResponseVO.serviceFail("网络中断，请重试");
        }
        return ResponseVO.success("保存成功");
    }

    @GetMapping(value = "creatTeam")
    public ResponseVO creatTeam(int userId){
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        int team = userAPI.createTeam(userId, code);
        if(team == 500 || team <=0 ){
            return ResponseVO.serviceFail("您已在一个队伍中，不能重复加入或创建");
        }
        if(team == 504 || team <=0 ){
            return ResponseVO.serviceFail("创建队伍失败，请重试！");
        }
        return ResponseVO.success("创建队伍成功");

    }

    @GetMapping(value = "joinTeam")
    public ResponseVO joinTeam(int userId,String code){
        int i = userAPI.joinTeam(userId, code);
        if(i == 500 || i <=0 ){
            return ResponseVO.serviceFail("您已在一个队伍中，不能重复加入或创建");
        }
        if(i == 504 || i <=0 ){
            return ResponseVO.serviceFail("加入队伍失败，请重试！");
        }
        return ResponseVO.success("加入队伍成功");
    }

    /**
     * 队伍选择题目
     * @param hashMap
     * @return
     */
    @PostMapping(value = "chooseSubjects")
    @Transactional
    public ResponseVO chooseSubjects(@RequestBody HashMap<String,Object> hashMap){
        int teamId = (Integer) hashMap.get("teamId");
        List<Integer> suidList = (List<Integer>) hashMap.get("suidList");
        Object[] objects = suidList.toArray();
        //判断该队伍是否已经选满了三条题目
        int r = userAPI.countHadSubjetcs(teamId);
        if(r >=3){
            return ResponseVO.serviceFail("您的队伍已经选满了志愿，不能再选！");
        }
        int i = userAPI.chooseSubjects(teamId, objects);
        if(i<=0){
            return ResponseVO.serviceFail("选择题目失败，请刷新重试");
        }
        //更新本题目已选人数
        int j = userAPI.updateHadSelected(objects);
        if(j <=0 ){
            return ResponseVO.serviceFail("选择题目失败，请刷新重试");
        }
        return ResponseVO.success("选择成功");
    }

    /**
     * 获取队伍的志愿
     * @param teamId
     * @return
     */
    @GetMapping(value = "getInten")
    public ResponseVO getInten(int teamId){
        List<TeamIntenVO> teamIntenByTeamId = userAPI.getTeamIntenByTeamId(teamId);
        if(teamIntenByTeamId == null || teamIntenByTeamId.size() <=0){
            return ResponseVO.success("您的队伍尚未选题");
        }
        return ResponseVO.success(teamIntenByTeamId);
    }

    /**
     * 获取队伍内的人数
     * @param teamId
     * @return
     */
    @GetMapping(value = "countTeamMem")
    public ResponseVO countTeamMem(int teamId){
        int i = userAPI.countTeamMember(teamId);
        return ResponseVO.success(1);
    }
//    @RequestMapping(value="register",method = RequestMethod.POST)
//    public ResponseVO register(UserModel userModel){
//        if(userModel.getUsername() == null || userModel.getUsername().trim().length()==0){
//            return ResponseVO.serviceFail("用户名不能为空");
//        }
//        if(userModel.getPassword() == null || userModel.getPassword().trim().length()==0){
//            return ResponseVO.serviceFail("密码不能为空");
//        }
//        if(userModel.getNickname() == null || userModel.getNickname().trim().length()==0){
//            return ResponseVO.serviceFail("昵称不能为空");
//        }
//        // 当返回true的时候，表示用户名可用
//        boolean notExistsUsername = userAPI.checkUsername(userModel.getUsername());
//        boolean notExistsNickname = userAPI.checkNickname(userModel.getNickname());
//        if (notExistsUsername && notExistsNickname){
//            boolean isSuccess = userAPI.register(userModel);
//            if(isSuccess){
//                return ResponseVO.success("注册成功");
//            }else{
//                return ResponseVO.serviceFail("注册失败");
//            }
//        }else{
//            return ResponseVO.serviceFail("用户名或昵称已存在");
//        }
//    }


    @RequestMapping(value="logout",method = RequestMethod.GET)
    public ResponseVO logout(){
        /*
            应用：
                1、前端存储JWT 【七天】 ： JWT的刷新
                2、服务器端会存储活动用户信息【30分钟】
                3、JWT里的userId为key，查找活跃用户
            退出：
                1、前端删除掉JWT
                2、后端服务器删除活跃用户缓存
            现状：
                1、前端删除掉JWT
         */


        return ResponseVO.success("用户退出成功");
    }


//    @RequestMapping(value="getUserInfo",method = RequestMethod.GET)
//    public ResponseVO getUserInfo(){
//        // 获取当前登陆用户
//        String userId = CurrentUser.getCurrentUser();
//        if(userId != null && userId.trim().length()>0){
//            // 将用户ID传入后端进行查询
//            int uuid = Integer.parseInt(userId);
//            UserInfoModel userInfo = userAPI.getUserInfo(uuid);
//            if(userInfo!=null){
//                return ResponseVO.success(userInfo);
//            }else{
//                return ResponseVO.appFail("用户信息查询失败");
//            }
//        }else{
//            return ResponseVO.serviceFail("用户未登陆");
//        }
//    }

//    @RequestMapping(value="updateUserInfo",method = RequestMethod.POST)
//    public ResponseVO updateUserInfo(UserInfoModel userInfoModel){
//        // 获取当前登陆用户
//        String userId = CurrentUser.getCurrentUser();
//        if(userId != null && userId.trim().length()>0){
//            // 将用户ID传入后端进行查询
//            int uuid = Integer.parseInt(userId);
//            // 判断当前登陆人员的ID与修改的结果ID是否一致
//            if(uuid != userInfoModel.getUuid()){
//                return ResponseVO.serviceFail("请修改您个人的信息");
//            }
//
//            UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
//            if(userInfo!=null){
//                return ResponseVO.success(userInfo);
//            }else{
//                return ResponseVO.appFail("用户信息修改失败");
//            }
//        }else{
//            return ResponseVO.serviceFail("用户未登陆");
//        }
//    }

}
