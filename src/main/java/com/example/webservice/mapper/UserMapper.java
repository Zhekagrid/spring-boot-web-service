package com.example.webservice.mapper;

import com.example.webservice.dto.LoginInfoDto;
import com.example.webservice.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User loginDtoToUser(LoginInfoDto loginInfoDto);

    LoginInfoDto userToLoginDto(User user);
}
