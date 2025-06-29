package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class ReservationNotFoundException extends CustomException {
    public ReservationNotFoundException() {
        super("해당 예약이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
