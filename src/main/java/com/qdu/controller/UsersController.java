package com.qdu.controller;
import com.qdu.common.convention.result.Result;
import com.qdu.dto.resp.user.UserQueryRespDTO;
import com.qdu.dto.resp.user.UserRegisterRespDTO;
import com.qdu.service.impl.UsersServiceImpl;
import jakarta.validation.Valid;
import com.qdu.common.convention.result.Results;
import com.qdu.dto.req.user.UserRegisterReqDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 徐增润
 * @since 2025-03-14
 */
@RestController
@RequestMapping("/api/user-service")
@RequiredArgsConstructor
public class UsersController {

    private final UsersServiceImpl userService;

    @PostMapping("/register")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO requestParam) {
        return Results.success(userService.register(requestParam));
    }


    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/query")
    public Result<UserQueryRespDTO> queryUserByUsername(@RequestParam("username") @NotEmpty String username) {
        return Results.success(userService.queryUserByUsername(username));
    }

}
