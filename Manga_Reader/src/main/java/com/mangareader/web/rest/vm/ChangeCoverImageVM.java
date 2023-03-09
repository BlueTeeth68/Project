package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCoverImageVM {

    @NotNull
    private Long id;

    @NotNull
    private MultipartFile file;
}
