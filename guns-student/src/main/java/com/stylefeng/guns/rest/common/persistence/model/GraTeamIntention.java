package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 队伍选课优先级  表
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-11
 */
@TableName("gra_team_intention")
public class GraTeamIntention extends Model<GraTeamIntention> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 队伍id
     */
    private Integer tid;
    /**
     * 题目id
     */
    private Integer sid;
    /**
     * 优先级： 1(第一志愿) 2（第2志愿） 3（第三志愿）
     */
    private Integer priority;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "GraTeamIntention{" +
        "id=" + id +
        ", tid=" + tid +
        ", sid=" + sid +
        ", priority=" + priority +
        "}";
    }
}
