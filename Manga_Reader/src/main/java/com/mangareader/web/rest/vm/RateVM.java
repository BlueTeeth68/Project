package com.mangareader.web.rest.vm;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateVM {
    @NotNull
    @Min(0)
    @Max(10)
    private Integer point;

    @NotNull
    private Long mangaId;
}
