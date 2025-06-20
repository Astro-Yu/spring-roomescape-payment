package roomescape.reservationTime.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservationTime.domain.ReservationTime;

public record ReservationTimeCreateRequest(
        @NotNull
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return ReservationTime.createWithoutId(startAt);
    }
}
