package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.student.UserAPI;
import com.stylefeng.guns.api.student.vo.SubjectInfoVO;
import com.stylefeng.guns.api.student.vo.TeamIntenVO;
import com.stylefeng.guns.api.student.vo.UserInfoModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.modular.user.vo.TeamIntVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI {

    @Autowired
    private GraUserMapper graUserMapper;

    @Autowired
    private GraSubjectMapper graSubjectMapper;

    @Autowired
    private GraUserExpandMapper graUserExpandMapper;

    @Autowired
    private GraTeamMapper graTeamMapper;

    @Autowired
    private GraTeamIntentionMapper graTeamIntentionMapper;


    @Override
    public UserInfoModel login(String username, String password) {
        // 根据登陆账号获取数据库信息
        GraUser UserT = new GraUser();
        UserT.setUsername(username);

        GraUser result = graUserMapper.selectOne(UserT);
        // 获取到的结果，然后与加密以后的密码做匹配
        if(result!=null && result.getId()>0){
//            String md5Password = MD5Util.encrypt(password);
            String md5Password = password;
            if(result.getPassword().equals(md5Password)){
                return do2UserInfo(result);
            }
        }
        return null;
    }

    @Override
    public List<SubjectInfoVO> getAllSubjects() {
        List<GraSubject> allSubjects = graSubjectMapper.getAllSubjects();
        List<SubjectInfoVO> subjectInfoVOList = new ArrayList<>();
        for (GraSubject graSubject:allSubjects){
            subjectInfoVOList.add(do2SubjectInfo(graSubject));
        }
        return subjectInfoVOList;
    }

    @Override
    public List<SubjectInfoVO> getSubjectsByTeamId(int teamId) {
        List<GraSubject> subjectsByTeamId = graSubjectMapper.getSubjectsByTeamId(teamId);
        List<SubjectInfoVO> subjectInfoVOList = new ArrayList<>();
        for (GraSubject graSubject:subjectsByTeamId){
            subjectInfoVOList.add(do2SubjectInfo(graSubject));
        }
        return subjectInfoVOList;
    }

    @Override
    public List<SubjectInfoVO> getSubjectsByKeword(String keyword) {
        List<GraSubject> subjectsByKeyword = graSubjectMapper.getSubjectsByKeyword('%' + keyword + '%');
        List<SubjectInfoVO> subjectInfoVOList = new ArrayList<>();
        for (GraSubject graSubject:subjectsByKeyword){
            subjectInfoVOList.add(do2SubjectInfo(graSubject));
        }
        return subjectInfoVOList;
    }

    @Override
    public int saveORUpdateIntroduce(int userId,String introduce) {
        GraUserExpand graUserExpand = graUserExpandMapper.hadIntroduce(userId);
        if(graUserExpand == null || graUserExpand.getId() <=0){
            int i = graUserExpandMapper.svaeIntroduce(userId, introduce);
            return i;
        }
        int j = graUserExpandMapper.updateIntroduce(userId, introduce);
        return j;
    }

    @Override
    @Transactional
    public int createTeam(int userId,String code) {
        //判断用户是否已加入团队， 若已加入，则不能创建队伍
        Integer i = graTeamMapper.hadJoin(userId);
        if(i>0){
            return 500;
        }
        //创建团队
        GraTeam graTeam = new GraTeam();
        graTeam.setCode(Integer.parseInt(code));
        int team = graTeamMapper.createTeam(graTeam);
        if(team <=0){
            return 504;
        }
        //更新身份标识和用户所属队伍id
        int res = graTeamMapper.updateTagByUserId(userId, graTeam.getTid());
        if(res <=0){
            return 504;
        }
        return 200;
    }

    @Override
    public int joinTeam(int userId,String code) {
        //1、判断用户是否已加入队伍
        Integer i = graTeamMapper.hadJoin(userId);
        if(i > 0){
            return 500;
        }
        //2、用code获取队伍id
        Integer teamId = graTeamMapper.getTeamIdByCode(code);
        if( teamId <=0){
            return 504;
        }
        int res = graTeamMapper.joinTeam(userId, teamId);
        if(res <=0){
            return 504;
        }
        return res;
    }

    @Override
    public int chooseSubjects(int teamId,Object[] suidList) {
        List<GraTeamIntention> list = new ArrayList<>();
        int i =1;
        for(Object obj:suidList){
            GraTeamIntention graTeamIntention = new GraTeamIntention();
            graTeamIntention.setSid((Integer) obj);
            graTeamIntention.setTid(teamId);
            graTeamIntention.setPriority(i);

            list.add(graTeamIntention);
            i++;
        }
        int res = graTeamIntentionMapper.chooseSubjects(list);
        return res;
    }

    @Override
    public List<TeamIntenVO> getTeamIntenByTeamId(int teamId) {
        List<TeamIntenVO> teamIntenVO = new ArrayList<>();
        List<TeamIntVO> intenByTeamId = graTeamIntentionMapper.getIntenByTeamId(teamId);
        if(intenByTeamId == null || intenByTeamId.size() <=0){
            return null;
        }
        //DO 转 VO
        for(TeamIntVO teamIntVO:intenByTeamId){
            teamIntenVO.add(do2teamIntentionInfo(teamIntVO));
        }
        return teamIntenVO;
    }

    @Override
    public int countTeamMember(int teamId) {
        int i = graTeamMapper.countTeamMember(teamId);
        return i;
    }

    @Override
    public int updateHadSelected(Object[] suidList) {
        List<GraSubject> list = new ArrayList<>();
        for(Object obj:suidList){
            GraSubject graSubject = new GraSubject();
            graSubject.setSuid((Integer) obj);

            list.add(graSubject);
        }
        int res = graSubjectMapper.updateHadSelected(list);
        return res;
    }

    @Override
    public int countHadSubjetcs(int teamId) {
        int i = graTeamIntentionMapper.countHadSubjects(teamId);
        return i;
    }

    //user表的DO 转 VO
    private UserInfoModel do2UserInfo(GraUser graUser){
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setId(graUser.getId());
        userInfoModel.setSex(graUser.getSex());
        userInfoModel.setName(graUser.getName());
        userInfoModel.setIdentity(graUser.getIdentity());
        userInfoModel.setTeamId(graUser.getTeamId());
        userInfoModel.setTag(graUser.getTag());

        return userInfoModel;
    }

    //subject表的DO 转 VO
    private SubjectInfoVO do2SubjectInfo(GraSubject graSubject){
        SubjectInfoVO subjectInfoVO = new SubjectInfoVO();

        subjectInfoVO.setSuid(graSubject.getSuid());
        subjectInfoVO.setTitle(graSubject.getTitle());
        subjectInfoVO.setType(graSubject.getType());
        subjectInfoVO.setTeacher(graSubject.getTeacher());
        subjectInfoVO.setSelectedNum(graSubject.getSelectedNum());
        subjectInfoVO.setLimitedNum(graSubject.getLimitedNum());
        subjectInfoVO.setContent(graSubject.getContent());

        return subjectInfoVO;
    }

    //teamIntention表的DO 转 VO
    private TeamIntenVO do2teamIntentionInfo(TeamIntVO intention){
        TeamIntenVO teamIntenVO = new TeamIntenVO();

        teamIntenVO.setSuid(intention.getSuid());
        teamIntenVO.setTitle(intention.getTitle());
        teamIntenVO.setType(intention.getType());
        teamIntenVO.setTeacher(intention.getTeacher());
        teamIntenVO.setSelectedNum(intention.getSelectedNum());
        teamIntenVO.setLimitedNum(intention.getLimitedNum());
        teamIntenVO.setContent(intention.getContent());
        Integer priority = intention.getPriority();
        switch (priority){
            case 1:
                teamIntenVO.setPriority("第一志愿");
                break;
            case 2:
                teamIntenVO.setPriority("第二志愿");
                break;
            case 3:
                teamIntenVO.setPriority("第三志愿");
                break;
        }

        return teamIntenVO;
    }

//    @Override
//    @Cacheable(value = "redisCache",key = "'redis_user_'+#uuid")
//    public UserInfoModel getUserInfo(int uuid) {
//        // 根据主键查询用户信息 [MoocUserT]
//        SnailUser moocUserT = snailUserMapper.selectById(uuid);
//        // 将MoocUserT转换UserInfoModel
//        UserInfoModel userInfoModel = do2UserInfo(moocUserT);
//        // 返回UserInfoModel
//        return userInfoModel;
//    }

//    更新数据后，更新缓存
//    @Override
//    @CachePut(value = "redisCache",condition = "#result != 'null'",key = "'redis_user_'+#result.uuid")
//    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
//        // 将传入的参数转换为DO 【MoocUserT】
//        SnailUser snailUser = new SnailUser();
//        snailUser.setUuid(userInfoModel.getUuid());
//        snailUser.setUsername(userInfoModel.getUsername());
//        snailUser.setHeadAddr(userInfoModel.getHeadAddr());
////        snailUser.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
////        snailUser.setBirthday(userInfoModel.getBirthday());
////        snailUser.setBiography(userInfoModel.getBiography());
////        snailUser.setBeginTime(null);
////        snailUser.setHeadUrl(userInfoModel.getHeadAddress());
////        snailUser.setEmail(userInfoModel.getEmail());
////        snailUser.setAddress(userInfoModel.getAddress());
////        snailUser.setUserPhone(userInfoModel.getPhone());
////        snailUser.setUserSex(userInfoModel.getSex());
//        snailUser.setUpdateTime(null);

//        // DO存入数据库
//        Integer integer = snailUserMapper.updateById(snailUser);
//        if(integer>0){
//            // 将数据从数据库中读取出来
//            UserInfoModel userInfo = getUserInfo(snailUser.getUuid());
//            System.out.println(userInfo.getHeadAddr());
//            // 将结果返回给前端
//            return userInfo;
//        }else{
//            return null;
//        }
//    }


}
