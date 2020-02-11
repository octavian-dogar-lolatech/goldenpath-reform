package com.lola.goldenpath.dto;

import com.lola.goldenpath.model.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private List<RoleEntity> role;
    private String email;
}
