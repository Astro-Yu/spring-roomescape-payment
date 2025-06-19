package roomescape.reservationTime.dto.request;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(LocalTime createAt) {
}
