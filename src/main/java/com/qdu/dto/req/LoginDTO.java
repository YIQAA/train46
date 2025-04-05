package com.qdu.dto.req;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;

    public LoginDTO() {

    }
}
