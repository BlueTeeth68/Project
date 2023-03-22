package com.mangareader.web.rest.vm;

import com.mangareader.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenVM {

    private User user;
    private String accessToken;
    private String refreshToken;
}
