package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidAuthenticationException extends CustomException {
    public InvalidAuthenticationException() {
        super("아이디 혹은 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
