package com.stylefeng.guns.api.student.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserModel implements Serializable {
    private String username;
    private String password;

}
