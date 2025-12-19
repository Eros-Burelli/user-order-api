package com.eros.userorderapi.dto.request;

import com.eros.userorderapi.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDTO {
	private String name;
	private String email;
	private String password;
	private UserRole role;
}
