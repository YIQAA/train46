package com.qdu.service;

import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
public interface IUsersService extends IService<Users> {

    UserPassengerRespDTO getUserPassenger(String userName);

    Users getOneByUsername(String username);

    //根据userid查询用户名
    String getUserNameByUserId(Integer userId);
}
