package com.stylefeng.guns.api.student.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubjectInfoVO implements Serializable {
    /**
     * 题目id
     */
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
}
