package com.mangareader.web.rest.vm;

import com.mangareader.domain.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserRoleVM {

    @NotNull
    private Long id;

    @NotNull
    private RoleName roleName;

}
