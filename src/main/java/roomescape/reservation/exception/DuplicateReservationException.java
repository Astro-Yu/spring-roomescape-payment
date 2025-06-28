package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class DuplicateReservationException extends CustomException {
    public DuplicateReservationException() {
        super("같은 날짜, 시간, 테마에 예약이 이미 존재합니다.", HttpStatus.BAD_REQUEST);
    }
}
