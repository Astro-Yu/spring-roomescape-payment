package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidPasswordLengthException extends CustomException {
    public InvalidPasswordLengthException() {
        super("비밀번호는 8자 이상, 20자 이하여야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
