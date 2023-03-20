package com.rashad.currency.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegisterRequest {

    @NotBlank(message = "required should not be empty")
    @Schema(example = "resad")
    private String username;

    @NotBlank(message = "required should not be empty")
    @Schema(example = "12345")
    private String password;
}
