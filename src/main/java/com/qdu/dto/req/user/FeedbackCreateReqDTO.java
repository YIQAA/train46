package com.qdu.dto.req.user;

import lombok.Data;

@Data
public class FeedbackCreateReqDTO {

    Integer userId;
    String type;
    String title;
    String content;
    String contact;

}
