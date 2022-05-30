package com.mltt.biz.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FUser implements Serializable {
    private Integer id;
    private String username;
    private String phone;
    private String created_time;
    private String updated_time;
    private String last_login_ip;
    private String register_ip;
    private String avatar;
    private String password;
    private String salt;
}
