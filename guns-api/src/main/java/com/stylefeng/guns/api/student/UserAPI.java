package com.stylefeng.guns.api.student;

import com.stylefeng.guns.api.student.vo.SubjectInfoVO;
import com.stylefeng.guns.api.student.vo.TeamIntenVO;
import com.stylefeng.guns.api.student.vo.UserInfoModel;
import com.stylefeng.guns.api.student.vo.UserModel;

import java.util.List;

public interface UserAPI {
    //登录
    UserInfoModel login(String account, String password);

    //获取题目列表
    List<SubjectInfoVO> getAllSubjects();

    //我的队伍所选题目
    List<SubjectInfoVO> getSubjectsByTeamId(int teamId);

    //查询题目
    List<SubjectInfoVO> getSubjectsByKeword(String keyword);

    //保存或更新介绍自己
    int saveORUpdateIntroduce(int userId,String introduce);

    //创建团队
    int createTeam(int userId,String code);

    //加入团队
    int joinTeam(int userId,String code);


    //队长选择题目
    int chooseSubjects(int teamId,Object[] suidList);

    //获取队伍的志愿
    List<TeamIntenVO> getTeamIntenByTeamId(int teamId);

    //统计队伍内的人数
    int countTeamMember(int teamId);

    //更新题目已选人数
    int updateHadSelected(Object[] suidList);

    //判断队伍是否已选满了三条题目，选满不可再选
    int countHadSubjetcs(int teamId);

//    boolean register(UserModel userModel);
//
//    boolean checkUsername(String username);
//
//    boolean checkNickname(String nickname);
//
//    UserInfoModel getUserInfo(int uuid);
//
//    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);



}

