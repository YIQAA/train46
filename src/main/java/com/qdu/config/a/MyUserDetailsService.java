//package com.qdu.config;
//
//import com.qdu.entity.Userinfo;
//import com.qdu.mapper.UserMapper;
//import com.qdu.mapper.UserinfoMapper;
//import com.qdu.service.impl.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class MyUserDetailsService implements UserDetailsService{
//
//    @Autowired
//    private UserinfoMapper userinfoMapper;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
////        Userinfo info=userMapper.selectByUsername(username);
//        com.qdu.entity.User user1 = userService.getUserByUserid(username);
//
//        if(null==user1)
//            throw new UsernameNotFoundException("查无此用户！！！");
//
//        List<GrantedAuthority> list=new ArrayList<>();
//
//        List<String> roleList=userMapper.selectRoleNamesById(username);
//
//        for(String roleName:roleList) {
//            GrantedAuthority auth=new SimpleGrantedAuthority("ROLE_"+roleName);
//            list.add(auth);
//        }
//
//        return new User(username,user1.getUserpwd(),list);
//    }
//}
