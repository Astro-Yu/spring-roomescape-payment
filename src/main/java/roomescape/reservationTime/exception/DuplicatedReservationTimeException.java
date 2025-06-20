package roomescape.reservationTime.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.CustomException;

public class DuplicatedReservationTimeException extends CustomException {
    public DuplicatedReservationTimeException() {
        super("중복된 예약 시간은 생성 불가능합니다.", HttpStatus.BAD_REQUEST);
    }
}
