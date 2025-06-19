package roomescape.reservationTime.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class InvalidReservationTimeException extends CustomException {
    public InvalidReservationTimeException() {
        super("10시 이전, 22시 이후 예약은 불가능합니다.", HttpStatus.BAD_REQUEST);
    }
}
