package roomescape.theme.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidThemeNameLengthException extends CustomException {
    public InvalidThemeNameLengthException() {
        super("테마의 이름은 1자 이상, 20자 이하만 가능합니다.", HttpStatus.BAD_REQUEST);
    }
}
