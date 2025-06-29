package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record UserReservationCreateRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        String paymentKey,
        String orderId,
        int amount) {
}
