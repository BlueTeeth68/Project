package com.mangareader.service.mapper;

import com.mangareader.domain.User;
import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.repository.UserRepository;
import com.mangareader.service.dto.CommonUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMapper {

    private final UserRepository userRepository;

    public CommonUserDTO entityToCommonUserDTO(User user) {
        CommonUserDTO result = new CommonUserDTO();

        log.info("Converting user {} to commonUserDTO.", user.getUsername());
        result.setId(user.getId());
        result.setActivate(user.getActivate());
        result.setAvatarUrl(user.getAvatarUrl());
        result.setDisplayName(user.getDisplayName());

        return result;
    }

    public User commonUserDTOToUser(CommonUserDTO commonUserDTO) {
        User result = new User();

        if (commonUserDTO.getId() == null) {
            log.error("Id of commonUserDTO is null");

            return null;
        }
        result = userRepository.findById(commonUserDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User " + commonUserDTO.getId() + " is not found.")
        );
        return result;
    }

}
