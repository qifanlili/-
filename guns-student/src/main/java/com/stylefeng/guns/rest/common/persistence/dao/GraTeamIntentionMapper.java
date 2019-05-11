package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.GraTeamIntention;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.modular.user.vo.TeamIntVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 队伍选课优先级  表 Mapper 接口
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-11
 */
public interface GraTeamIntentionMapper extends BaseMapper<GraTeamIntention> {

    int chooseSubjects(List<GraTeamIntention> list);

    //显示我的志愿
    List<TeamIntVO> getIntenByTeamId(int teamId);

    //统计是否已选满三条志愿，已满不可再选
    @Select("select count(*) from gra_team_intention where tid = #{teamId}")
    int countHadSubjects(int teamId);
}
