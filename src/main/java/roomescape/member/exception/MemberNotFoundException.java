package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException() {
        super("해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
