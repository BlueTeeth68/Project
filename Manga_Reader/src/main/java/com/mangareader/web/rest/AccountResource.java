package com.mangareader.web.rest;

import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.CommonUserDTO;
import com.mangareader.service.mapper.UserMapper;
import com.mangareader.web.rest.vm.ChangePasswordVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})

@Tag(name = "02. Account")
public class AccountResource {

    private final IUserService userService;
    private final UserMapper userMapper;
    private final HttpServletRequest request;

    @Operation(
            summary = "Get current account user",
            description = "Logged in user can get their account",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getCurrentLoginUser() {
        User user = userService.getCurrentUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Search other user account",
            description = "Logged in user can search other account by their id or display name",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonUserDTO> getUserByIdOrUsername(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String displayName
    ) {
        User user;

        //find by id
        if (id != null && displayName == null) {
            user = userService.getUserById(id);
        }
        //find by username
        else if (id == null && displayName != null) {
            user = userService.getUserByDisplayName(displayName);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }
        CommonUserDTO result = userMapper.toCommonUserDTO(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Change display name",
            description = "Logged in user can change their display name",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> changeDisplayName(
            @RequestParam String displayName
    ) {
        User result = userService.changeDisplayName(displayName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Change avatar",
            description = "Logged in user can change their avatar",
            security = @SecurityRequirement(name = "authorize"))
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> changeUserAvatar(
            @RequestPart("file") MultipartFile file
    ) throws TimeoutException {
        User result = userService.updateAvatar(file);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Change password",
            description = "Logged in user can change their password by inputting their old and new password",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> changePassword(
            @Valid @RequestBody ChangePasswordVM vm
    ) {
        return new ResponseEntity<>(userService.changePassword(vm), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete account",
            description = "Logged in user can delete their account from the system",
            security = @SecurityRequirement(name = "authorize"))
    @DeleteMapping()
    public ResponseEntity<?> deleteUserById(
    ) {
        User user = userService.getCurrentUser();
        userService.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
