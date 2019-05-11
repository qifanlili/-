package com.stylefeng.guns.api.student.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoModel implements Serializable{
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身份id   1为学生   2为老师
     */
    private Integer identity;
    /**
     * 团队id
     */
    private Integer teamId;
    /**
     * 标识符 默认是2不能选课  队长则为1可选课
     */
    private Integer tag;
}
