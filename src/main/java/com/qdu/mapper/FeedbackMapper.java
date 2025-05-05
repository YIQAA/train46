package com.qdu.mapper;

import com.qdu.entity.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户意见反馈表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-05-04
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {

    List<Feedback> queryFeedbackHistory(Integer userId);
}
