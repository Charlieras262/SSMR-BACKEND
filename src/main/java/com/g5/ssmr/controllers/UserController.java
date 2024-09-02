package com.g5.ssmr.controllers;

import com.g5.ssmr.dto.*;
import com.g5.ssmr.projections.UserDetailProjection;
import com.g5.ssmr.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@Slf4j
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping(value = "internal/users/token/refresh")
    @ApiOperation(value = "Retorna todos los usuarios registrados en el sistema.")
    public UserLoggedDto refreshToken(@RequestHeader HttpHeaders headers) {
        return userService.refreshToken(headers);
    }

    @PostMapping(value = "internal/users/token/revoke")
    @ApiOperation(value = "Retorna todos los usuarios registrados en el sistema.")
    public void revokeToken(@RequestHeader HttpHeaders headers) {
        userService.revokeToken(headers);
    }

    @GetMapping(value = "internal/users/find")
    @ApiOperation(value = "Retorna todos los usuarios registrados en el sistema.")
    public List<UserDetailProjection> getAllUser(@RequestHeader HttpHeaders headers) {
        return userService.getAllUsers(headers);
    }

    @GetMapping(value = "internal/users/find/companies")
    @ApiOperation(value = "Retorna todos los usuarios registrados en el sistema.")
    public List<UserDetailProjection> getAllUserCompanies(@RequestHeader HttpHeaders headers) {
        return userService.getAllUsersCompanies(headers);
    }

    @GetMapping(value = "internal/users/find/{userId}")
    @ApiOperation(value = "Retorna todos los usuarios registrados en el sistema.")
    public UserProfileDto getUser(@PathVariable String userId) {
        return userService.getUserProfile(userId);
    }

    @PostMapping(value = "internal/users/create")
    @ApiOperation(value = "Crea un registro de usuario en base de datos.")
    public UserCreatedDto createUser(@RequestHeader HttpHeaders headers, @RequestBody CreateUserDto dto) {
        return userService.createUser(headers, dto);
    }

    @PatchMapping(value = "internal/users/edit/{username}")
    @ApiOperation(value = "Actualiza el registro de un usuario en base de datos.")
    public void updateUser(@PathVariable String username, @RequestBody EditUserDto dto) {
        userService.updateUser(username, dto);
    }

    @PostMapping(value = "external/users/login")
    @ApiOperation(value = "Inicia la sesion de un usuario en base a el nombre de usuario y contraseña.")
    public UserLoggedDto loginUser(@RequestBody UserDto dto) {
        return userService.authUser(dto);
    }

    @GetMapping(value = "external/verify/existing/login/{username}")
    @ApiOperation(value = "Inicia la sesion de un usuario en base a el nombre de usuario y contraseña.")
    public UserLoggedDto existingLogin(@PathVariable String username) {
        return userService.existingLogin(username);
    }

    @PatchMapping(value = "external/users/recover/password/{token}")
    @ApiOperation(value = "Recupera la contraseña de un usuario en base de datos.")
    public boolean recoverPassword(@PathVariable String token, @RequestBody UserDto dto) {
        final ChangePassDto changePassDto = ChangePassDto.builder().oldPass(token).newPass(dto.getPassword()).username(dto.getUsername()).build();
        return userService.changePassword(null, changePassDto);
    }

    @PostMapping(value = "external/users/request/recover/password/{username}")
    @ApiOperation(value = "Recupera la contraseña de un usuario en base de datos.")
    public boolean requestRecoverPassword(@PathVariable String username) {
        return userService.requestChangePassword(username);
    }

    @PostMapping(value = "external/users/request/recover/validate/{username}/{token}")
    @ApiOperation(value = "Recupera la contraseña de un usuario en base de datos.")
    public boolean validateToken(@PathVariable String username, @PathVariable String token) {
        return userService.validateToken(username, token);
    }

    @PatchMapping(value = "internal/users/change/password")
    @ApiOperation(value = "Actualiza la contraseña de un usuario en base de datos.")
    public boolean updatePassword(@RequestHeader HttpHeaders headers, @RequestBody ChangePassDto dto) {
        return userService.changePassword(headers, dto);
    }

    @DeleteMapping(value = "internal/users/delete/{username}")
    @ApiOperation(value = "Eliminación de un usuario en base de datos y en servidor de correos.")
    public boolean deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }
}
