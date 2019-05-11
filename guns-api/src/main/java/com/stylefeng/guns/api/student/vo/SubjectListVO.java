package com.stylefeng.guns.api.student.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SubjectListVO implements Serializable {
    private List<SubjectInfoVO> allSubjects;
//    private List<SubjectInfoVO> subjectsWithTeam;
}
