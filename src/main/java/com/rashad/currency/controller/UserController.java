package com.rashad.currency.controller;

import com.rashad.currency.model.CustomResponse;
import com.rashad.currency.model.JwtResponse;
import com.rashad.currency.model.RegisterRequest;
import com.rashad.currency.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "1. Login and Register")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register",
            description = "Register olmaq üçün asagıdakı Try it out düyməsini vurub " +
                    "request body hissəsini doldurduqdan sonra Execute düyməsini basın.",
            responses = {@ApiResponse(responseCode = "201", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = userService.register(request);
        return new ResponseEntity<>(new CustomResponse(message), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "Login olmaq üçün asagıdakı Try it out düyməsini vurub " +
                    "request body hissəsini doldurduqdan sonra Execute düyməsini basın.",
            responses = {@ApiResponse(responseCode = "200", description = "Success Response",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE))}
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
        JwtResponse jwtResponse = userService.login(request);
        return new ResponseEntity<>(new CustomResponse(jwtResponse), HttpStatus.OK);
    }

}
