package com.qdu.service.impl;

import com.qdu.dto.req.user.UserRegisterReqDTO;
import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.dto.resp.user.UserRegisterRespDTO;
import com.qdu.entity.Users;
import com.qdu.mapper.UsersMapper;
import com.qdu.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Override
    public UserPassengerRespDTO getUserPassenger(String userName) {
        return baseMapper.getUserPassenger(userName);
    }


    @Override
    public Users getOneByUsername(String username) {
        return baseMapper.getOneByUsername(username);
    }

    @Override
    public String getUserNameByUserId(Integer userId) {
        return baseMapper.getUserNameByUserId(userId);
    }

    @Autowired
    private PasswordEncoder passwordEncoder2;

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        Users user = new Users();
        user.setUserName(requestParam.getUsername());
        user.setEncryptedPassword(passwordEncoder2.encode(requestParam.getPassword()));
        user.setRealName(requestParam.getRealName());
        user.setIdCard(requestParam.getIdCard());
        user.setPhone(requestParam.getPhone());
        user.setRole("user");
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());
        baseMapper.insert(user);


        UserRegisterRespDTO userRegisterRespDTO = new UserRegisterRespDTO();
        userRegisterRespDTO.setUsername(user.getUserName());
        userRegisterRespDTO.setRealName(user.getRealName());
        userRegisterRespDTO.setPhone(user.getPhone());

        return userRegisterRespDTO;
    }
}
