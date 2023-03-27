package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User loginDtoToUser(LoginInfoDto loginInfoDto);

    LoginInfoDto userToLoginDto(User user);
}
