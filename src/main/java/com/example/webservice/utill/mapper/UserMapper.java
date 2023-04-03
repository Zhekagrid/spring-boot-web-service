package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.LoginInfoDto;
import com.example.webservice.model.entity.User;
import org.mapstruct.Mapper;

/**
 * {@link User} mapper
 */
@Mapper
public interface UserMapper {
    /**
     * Maps {@link LoginInfoDto} to {@link User}
     *
     * @param loginInfoDto the {@link LoginInfoDto} object for mapping
     * @return {@link User} mapped from loginInfoDto
     */
    User loginDtoToUser(LoginInfoDto loginInfoDto);


}
