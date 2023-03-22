package com.mangareader.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordVM {

    @NotNull
    @NotBlank
    private String oldPassword;

    @NotNull
    @NotBlank
    private String newPassword;
}
