package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.GraTeam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-10
 */
public interface GraTeamMapper extends BaseMapper<GraTeam> {
    /**
     * 判断用户尚未加入团队->创建团队->更改本人身份标识为1（队长）,更改teamId为队伍Id
     */
    //1、判断用户是否已加入团队
    @Select("select teamId from gra_user where id = #{userId}")
    Integer hadJoin(int userId);

    //2、创建团队
    @Insert("insert into gra_team (code) values(#{code})")
    @Options(useGeneratedKeys = true,keyProperty = "tid",keyColumn = "id")
    int createTeam(GraTeam graTeam);

    //3、更改本人身份标识为1（队长），更改teamId为队伍Id
    @Update("update gra_user set tag = 1,teamId = #{teamId} where id = #{userId}")
    int updateTagByUserId(@Param("userId") int userId,@Param("teamId")int teamId);


    /**
     * 判断用户尚未加入团队->用code查找队伍Id->加入团队
     */
    //2、用code查找队伍
    @Select("select tid from gra_team where code =#{code}")
    Integer getTeamIdByCode(String code);

    //3、加入团队（更改用户所属队伍Id）
    @Update("update gra_user set teamId = #{teamId} where id = #{userId}")
    int joinTeam(@Param("userId") int userId,@Param("teamId")int teamId);

    //统计队伍内人数
    @Select("select count(*) from gra_user where teamId = #{teamId} ")
    int countTeamMember(int teamId);
}
