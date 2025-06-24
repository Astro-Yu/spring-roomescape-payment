package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
    }
}
