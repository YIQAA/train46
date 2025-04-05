package com.qdu.service.impl;

import com.qdu.dto.resp.ticketList.UserPassengerRespDTO;
import com.qdu.entity.Users;
import com.qdu.mapper.UsersMapper;
import com.qdu.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
