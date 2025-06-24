package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidAuthorizationException extends CustomException {
    public InvalidAuthorizationException() {
        super("권한이 없습니다.", HttpStatus.UNAUTHORIZED);
    }
}
