package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.GraUserExpand;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-11
 */
public interface GraUserExpandMapper extends BaseMapper<GraUserExpand> {

    //检查用户是否介绍过自己
    @Select("select * from gra_user_expand where userId = #{userId}")
    GraUserExpand hadIntroduce(int userId);

    //保存介绍自己
    @Insert("insert into gra_user_expand (userId,introduce) values(#{userId},#{introduce})")
    int svaeIntroduce(@Param("userId") int userId, @Param("introduce") String introduce);

    //更新介绍自己
    @Update("update gra_user_expand set introduce = #{introduce} where userId = #{userId}")
    int updateIntroduce(@Param("userId") int userId, @Param("introduce") String introduce);
}
