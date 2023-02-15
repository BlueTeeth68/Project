package com.mangareader.vm;

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
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 40)
    private String password;
}
