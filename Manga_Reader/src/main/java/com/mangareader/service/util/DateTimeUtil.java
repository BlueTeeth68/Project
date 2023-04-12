package com.mangareader.service.util;

import com.mangareader.exception.BadRequestException;

import java.time.LocalDateTime;

public class DateTimeUtil {

    public static LocalDateTime calculateMinusDate(long second) {
        if (second <= 0) {
            throw new BadRequestException("Second is less than or equal to 0.");
        }
        LocalDateTime result = LocalDateTime.now();
        return result.minusSeconds(second);
    }


}
