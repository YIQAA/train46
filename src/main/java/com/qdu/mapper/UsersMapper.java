package com.qdu.mapper;

import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-23
 */
public interface UsersMapper extends BaseMapper<Users> {

    UserPassengerRespDTO getUserPassenger(@Param("userName") String userName);

    Users getOneByUsername(String username);

    String getUserNameByUserId(Integer userId);

    Users getOneByUserId(Integer userId);

    Integer getTodayViewCount();
}
