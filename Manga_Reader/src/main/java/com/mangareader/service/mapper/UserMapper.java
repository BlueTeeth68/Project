package com.mangareader.service.mapper;

import com.mangareader.domain.User;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class UserMapper {

    private final IUserService userService;

    public CommonUserDTO toCommonUserDTO(User user) {
        if (user == null)
            return null;

        CommonUserDTO result = new CommonUserDTO();

        log.info("Converting user {} to commonUserDTO.", user.getUsername());
        result.setId(user.getId());
        result.setActivate(user.getActivate());
        result.setAvatarUrl(user.getAvatarUrl());
        result.setDisplayName(user.getDisplayName());

        return result;
    }

    public User toUserEntity(CommonUserDTO commonUserDTO) {
        if (commonUserDTO == null) {
            return null;
        }
        return userService.getUserById(commonUserDTO.getId());
    }

}
