package com.qdu.service.impl;

import com.qdu.dto.req.admin.FeedBackReplyReqDTO;
import com.qdu.dto.req.user.FeedbackCreateReqDTO;
import com.qdu.entity.Feedback;
import com.qdu.mapper.FeedbackMapper;
import com.qdu.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户意见反馈表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-05-04
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

    @Override
    public String feedback(FeedbackCreateReqDTO requestParam) {

        Feedback feedback = new Feedback();
        feedback.setUserId(requestParam.getUserId());
        feedback.setFeedbackType(requestParam.getType());
        feedback.setTitle(requestParam.getTitle());
        feedback.setContent(requestParam.getContent());
        feedback.setContactInfo(requestParam.getContact());
        feedback.setStatus("pending");
        baseMapper.insert(feedback);
        return "ok";
    }

    @Override
    public List<Feedback> queryFeedbackHistory(Integer userId) {
        return baseMapper.queryFeedbackHistory(userId);
    }

    @Override
    public void replyFeedBack(FeedBackReplyReqDTO feedback) {

        Feedback feedback1 = baseMapper.selectById(feedback.getFeedbackId());
        feedback1.setReplyContent(feedback.getReplyContent());
        baseMapper.updateById(feedback1);
    }
}
