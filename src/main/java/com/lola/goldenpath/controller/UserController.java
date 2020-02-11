package com.lola.goldenpath.controller;

import com.lola.goldenpath.dto.LoginModelDto;
import com.lola.goldenpath.dto.LoginResponseDto;
import com.lola.goldenpath.dto.RoleDto;
import com.lola.goldenpath.dto.SignUpModelDto;
import com.lola.goldenpath.model.UserEntity;
import com.lola.goldenpath.security.CurrentUser;
import com.lola.goldenpath.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody final LoginModelDto loginModelDto) {

        final LoginResponseDto response = userService.signInUser(loginModelDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody final SignUpModelDto signUpModelDto) {

        return userService.signUpUser(signUpModelDto);
    }

    @PutMapping("/admin/update/{username}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<?> updateRoles(@PathVariable("username") final String username, @Valid @RequestBody final List<RoleDto> roleDtos) {

        return userService.updateRoles(username, roleDtos);
    }

    @GetMapping(value = "/user/profile")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<?> getProfile(@CurrentUser final UserEntity userEntity) {

        return userService.getProfile(userEntity);
    }
}
