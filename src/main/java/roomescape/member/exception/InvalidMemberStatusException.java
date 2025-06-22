package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidMemberStatusException extends CustomException {
    public InvalidMemberStatusException() {
        super("유효하지 않은 검색 조건입니다", HttpStatus.BAD_REQUEST);
    }
}
