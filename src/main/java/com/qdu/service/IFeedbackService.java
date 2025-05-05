package com.qdu.service;

import com.qdu.dto.req.admin.FeedBackReplyReqDTO;
import com.qdu.dto.req.user.FeedbackCreateReqDTO;
import com.qdu.entity.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户意见反馈表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-05-04
 */
public interface IFeedbackService extends IService<Feedback> {



    String feedback(FeedbackCreateReqDTO requestParam);

    List<Feedback> queryFeedbackHistory(Integer userId);


    void replyFeedBack(FeedBackReplyReqDTO feedback);
}
