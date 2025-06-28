package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class PastOrPresentReservationException extends CustomException {
    public PastOrPresentReservationException() {
        super("당일 예약 및 과거 예약은 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
