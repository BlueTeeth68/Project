package com.mangareader.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordVM {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(example = "trihandsome")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 20)
    @Schema(example = "0000")
    private String password;
}
