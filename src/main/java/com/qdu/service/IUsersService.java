package com.qdu.service;

import com.qdu.dto.req.admin.ToggleFreezeUserReqDTO;
import com.qdu.dto.req.user.UserRegisterReqDTO;
import com.qdu.dto.resp.admin.UserListRespDTO;
import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.dto.resp.user.UserQueryRespDTO;
import com.qdu.dto.resp.user.UserRegisterRespDTO;
import com.qdu.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

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

    //注册
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);

    //根据用户名查用户信息
    UserQueryRespDTO queryUserByUsername(String username);

    List<UserListRespDTO> getUserList();

    void toggleFreezeUser( ToggleFreezeUserReqDTO requestParam);
}
