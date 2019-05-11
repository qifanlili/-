package com.stylefeng.guns.api.student.vo;

import lombok.Data;

import java.io.Serializable;

//显示我的队伍的志愿
@Data
public class TeamIntenVO implements Serializable {
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
    private Integer selectedNum;
    /**
     * 限选人数
     */
    private Integer limitedNum;
    /**
     * 内容
     */
    private String content;

    private String priority;
}
