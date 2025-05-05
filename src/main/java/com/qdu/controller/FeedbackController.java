package com.qdu.controller;

import com.qdu.dto.req.admin.FeedBackReplyReqDTO;
import com.qdu.dto.req.admin.FeedBackStatusUpdateReqDTO;
import com.qdu.dto.req.user.FeedbackCreateReqDTO;
import com.qdu.entity.Feedback;
import com.qdu.service.IFeedbackService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户意见反馈表 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-05-04
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor

public class FeedbackController {

    private final IFeedbackService feedbackService;

    @PostMapping("/user-service/feedback")
    public String feedback(@RequestBody FeedbackCreateReqDTO requestParam){
        return feedbackService.feedback(requestParam);
    }

    @GetMapping("/user-service/feedback/history")
    public List<Feedback> queryFeedbackHistory(@RequestParam("userId") Integer userId){
        return feedbackService.queryFeedbackHistory(userId);
    }
    @GetMapping("/admin-service/complainList/query")
    public List<Feedback> queryComplainList(){
        return feedbackService.list();
    }
    @PostMapping("/admin-service/complainList/reply")
    public String replyComplain(@RequestBody FeedBackReplyReqDTO feedback){
        feedbackService.replyFeedBack(feedback);
        return "success";
    }
    @PostMapping("/admin-service/complainList/updateStatus")
    public String updateStatus(@RequestBody FeedBackStatusUpdateReqDTO feedback){
        System.out.println("反馈状态修改"+feedback);
        Feedback feedback1 = new Feedback();
        feedback1.setFeedbackId(feedback.getFeedbackId());
        feedback1.setStatus(feedback.getStatus());
        feedbackService.updateById(feedback1);
        return "success";
    }

}
