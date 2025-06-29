package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationWithPaymentResponse(
        Long id,
        LocalDate date,
        LocalTime startAt,
        String themeName,
        String memberName) {
}
