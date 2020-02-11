package com.lola.goldenpath.service;

import com.lola.goldenpath.dto.*;
import com.lola.goldenpath.model.entity.RoleEntity;
import com.lola.goldenpath.model.entity.UserEntity;
import com.lola.goldenpath.model.exception.RoleNotFoundException;
import com.lola.goldenpath.repository.RoleRepository;
import com.lola.goldenpath.repository.UserRepository;
import com.lola.goldenpath.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lola.goldenpath.constants.Constants.ROLE_USER_NAME;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public LoginResponseDto signInUser(final LoginModelDto loginModelDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginModelDto.getUsername(),
                        loginModelDto.getPassword()
                )
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        final String jwt = jwtTokenProvider
                .generateToken(authentication);

        final LoginResponseDto response = new LoginResponseDto(jwt);

        return LoginResponseDto.builder()
                .jwtCode(response.getJwtCode())
                .build();
    }

    public ResponseEntity<ApiResponseDto> signUpUser(final SignUpModelDto signUpModelDto) {

        if (userRepository.existsByUsernameOrEmail(signUpModelDto.getUsername(), signUpModelDto.getEmail())) {
            return new ResponseEntity<>(new ApiResponseDto(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        final UserEntity user = UserEntity.builder()
                .name(signUpModelDto.getName())
                .email(signUpModelDto.getEmail())
                .username(signUpModelDto.getUsername())
                .password(signUpModelDto.getPassword())
                .createdDateTime(ZonedDateTime.now())
                .roles(Collections.singleton(roleRepository.findByName(ROLE_USER_NAME)
                        .orElseThrow(() -> new RoleNotFoundException("Role was not set!"))))
                .enabled(true)
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponseDto(true, "User registered successfully"));

    }

    public ResponseEntity<?> updateRoles(final String username, final List<RoleDto> roleDtos) {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        final Set<RoleEntity> roleEntities = new HashSet<>();
        final var allRoles = roleRepository.findAll();

        final var roleDtoNames = roleDtos.stream().map(RoleDto::getRoleName).collect(Collectors.toList());

        allRoles.forEach(roleEntity -> {
            if (roleDtoNames.contains(roleEntity.getName())) {
                roleEntities.add(roleEntity);
            }
        });

        if (roleEntities.size() != roleDtos.size()) {
            return new ResponseEntity<>(new ApiResponseDto(false, "List of roles contain " + (roleDtos.size() - roleEntities.size()) + " invalid values"), HttpStatus.NOT_FOUND);
        }

        userEntity.setRoles(roleEntities);
        userEntity.setUpdatedDateTime(ZonedDateTime.now());
        userRepository.save(userEntity);

        return ResponseEntity.ok(new ApiResponseDto(true, "User roles were updated successfully"));
    }

    public ResponseEntity<?> getProfile(final UserEntity userEntity) {
        return ResponseEntity.ok(UserDto
                .builder()
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(new ArrayList<>(userEntity.getRoles()))
                .build());
    }
}
