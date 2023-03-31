package com.mangareader.web.rest;

import com.mangareader.domain.User;
import com.mangareader.exception.BadRequestException;
import com.mangareader.service.IUserService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.util.APIUtil;
import com.mangareader.web.rest.vm.ChangeUserRoleVM;
import com.mangareader.web.rest.vm.ChangeUserStatusVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
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
public class UserResource {

    private final IUserService userService;
    private final HttpServletRequest request;

    @Operation(
            summary = "Get user by id or userName",
            description = "Admin user can search user by id or userName", tags = "User",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByIdOrUsername(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) {
        User result;

        //find by id
        if (id != null && username == null) {
            result = userService.getUserById(id);
        }
        //find by username
        else if (id == null && username != null) {
            result = userService.getUserByUsername(username);
        } else {
            throw new BadRequestException("Bad request for id and username value.");
        }

        String serverName = APIUtil.getServerName(request);
        result = userService.addServerNameToAvatarURL(result, serverName);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all user",
            description = "Admin user can get user list with pageable", tags = "User",
            security = @SecurityRequirement(name = "authorize"))
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<User>> getAllAndPaginateUsers(
            @RequestParam(required = false, defaultValue = "100") String size,
            @RequestParam(required = false, defaultValue = "0") String page
    ) {
        Page<User> users = userService.getAllUsersWithPageable(page, size);
        String serverName = APIUtil.getServerName(request);

        PagingReturnDTO<User> result = new PagingReturnDTO<>();
        result.setContent(userService.addServerNameToAvatarURL(users.getContent(), serverName));
        result.setTotalElements(users.getTotalElements());
        result.setTotalPages(users.getTotalPages());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Change user role",
            description = "Admin user can change a user's role", tags = "User",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> changeRoleOfUser(
            @RequestBody ChangeUserRoleVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        User user = userService.setRoleToUser(vm.getId(), vm.getRoleName(), serverName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Block user",
            description = "Admin user can block or unblock user", tags = "User",
            security = @SecurityRequirement(name = "authorize"))
    @PatchMapping(value = "/active-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> changeActiveStatus(
            @RequestBody ChangeUserStatusVM vm
    ) {
        String serverName = APIUtil.getServerName(request);
        User user = userService.changeUserStatus(vm.getId(), vm.getStatus(), serverName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
