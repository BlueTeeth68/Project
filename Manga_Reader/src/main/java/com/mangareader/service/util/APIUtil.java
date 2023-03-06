package com.mangareader.service.util;

import com.mangareader.domain.MangaStatus;
import com.mangareader.domain.RoleName;
import com.mangareader.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIUtil {

    public static Long parseStringToLong(String input, String errorMessage) {
        Long result;
        try {
            log.info("Convert {} to Long.", input);
            result = Long.parseLong(input);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(errorMessage);
        }
        return result;
    }

    public static Integer parseStringToInteger(String input, String errorMessage) {
        Integer result;
        try {
            log.info("Convert {} to Integer.", input);
            result = Integer.parseInt(input);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(errorMessage);
        }
        return result;
    }

    public static RoleName parseStringToRoleNameEnum(String input, String errorMessage) {
        RoleName result;
        try {
            log.info("Convert role {} to RoleName enum", input);
            result = RoleName.valueOf(input);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(errorMessage);
        }
        return result;
    }

    public static Boolean parseStringToBoolean(String input, String errorMessage) {
        Boolean result;
        try {
            log.info("Convert {} to Boolean.", input);
            result = Boolean.valueOf(input);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(errorMessage);
        }
        return result;
    }

    public static MangaStatus parseStringToMangaStatus(String input, String errorMessage) {
        if (input != null) {
            input = input.charAt(0) + input.substring(1).toLowerCase();
        }

        MangaStatus result;
        try {
            log.info("Convert manga status {} to MangaStatus enum", input);
            result = MangaStatus.valueOf(input);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BadRequestException(errorMessage);
        }
        return result;
    }
}
