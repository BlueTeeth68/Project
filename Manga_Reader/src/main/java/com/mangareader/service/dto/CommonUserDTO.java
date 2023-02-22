package com.mangareader.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonUserDTO {

    private Long id;

    private String googleId;

    private String facebookId;

    private String displayName;

    private byte[] avatar;

    private Boolean activate = true;

}
