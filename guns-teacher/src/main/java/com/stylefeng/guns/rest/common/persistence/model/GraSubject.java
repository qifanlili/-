package com.stylefeng.guns.rest.common.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author qifanlee
 * @since 2019-05-10
 */
@TableName("gra_subject")
public class GraSubject extends Model<GraSubject> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "suid", type = IdType.AUTO)
    private Integer suid;
    /**
     * 题目名称
     */
    private String title;
    /**
     * 题目类别
     */
    private String type;
    /**
     * 指导老师
     */
    private String teacher;
    /**
     * 已选人数
     */
    @TableField("selected_num")
    private Integer selectedNum;
    /**
     * 限选人数
     */
    @TableField("limited_num")
    private Integer limitedNum;
    /**
     * 内容
     */
    private String content;


    public Integer getSuid() {
        return suid;
    }

    public void setSuid(Integer suid) {
        this.suid = suid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Integer getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(Integer selectedNum) {
        this.selectedNum = selectedNum;
    }

    public Integer getLimitedNum() {
        return limitedNum;
    }

    public void setLimitedNum(Integer limitedNum) {
        this.limitedNum = limitedNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected Serializable pkVal() {
        return this.suid;
    }

    @Override
    public String toString() {
        return "GraSubject{" +
        "suid=" + suid +
        ", title=" + title +
        ", type=" + type +
        ", teacher=" + teacher +
        ", selectedNum=" + selectedNum +
        ", limitedNum=" + limitedNum +
        ", content=" + content +
        "}";
    }
}
