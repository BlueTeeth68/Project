package com.mangareader.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonUserDTO {

    private Long id;

    private String displayName;

    private String avatarUrl;

    private Boolean activate = true;

}
