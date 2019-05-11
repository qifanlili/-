package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.GraSubject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-10
 */
public interface GraSubjectMapper extends BaseMapper<GraSubject> {
    @Select("select * from gra_subject")
    List<GraSubject> getAllSubjects();

    List<GraSubject> getSubjectsByTeamId(int teamId);

    //查询题目
    List<GraSubject> getSubjectsByKeyword(String keyword);

    //更新题目已选人数
    int updateHadSelected(List<GraSubject> graSubjects);
}
