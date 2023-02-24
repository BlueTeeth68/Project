package com.mangareader.main.service.mapper;

import com.mangareader.domain.User;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    private User user;

    private CommonUserDTO commonUserDTO;

    @BeforeEach
    public void init() {
        log.info("Create default user entity for testing");
        user = new User();
        user.setId(10L);
        user.setRole("ADMIN");
        user.setDisplayName("test admin");
        user.setActivate(true);
        user.setPassword(/*passwordEncoder.encode("0000")*/ "0000");
        user.setUsername("testAdmin");

        log.info("Create default CommonUserDTO for testing");
        commonUserDTO = new CommonUserDTO();
        commonUserDTO.setId(1L);
        commonUserDTO.setActivate(true);
        commonUserDTO.setDisplayName("abc");

    }

    @Test
    @DisplayName("Test 1: CommonUserDTO must equal to source User entity")
    public void commonUserDTOMustBeEqualToEntity() {
        CommonUserDTO result = userMapper.entityToCommonUserDTO(user);

        assertEquals(result.getId(), user.getId());
        assertEquals(result.getActivate(), user.getActivate());
        assertEquals(result.getAvatarUrl(), user.getAvatarUrl());
        assertEquals(result.getDisplayName(), user.getDisplayName());
    }

    @Test
    @DisplayName("Test 2: User entity must be call from id of CommonUserDTO")
    public void TestCommonUserDTOToUserMapping() {
        User result = userMapper.commonUserDTOToUser(commonUserDTO);
        assertEquals(result.getDisplayName(), "System Admin");

    }
}
