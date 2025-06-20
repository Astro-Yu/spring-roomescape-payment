package roomescape.reservationTime.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class ReservationTimeNotFoundException extends CustomException {
    public ReservationTimeNotFoundException() {
        super("해당 예약 시간이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
