package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidNameLengthException extends CustomException {
    public InvalidNameLengthException() {
        super("이름은 1자 이상, 10자 이하만 가능합니다.", HttpStatus.BAD_REQUEST);
    }
}
