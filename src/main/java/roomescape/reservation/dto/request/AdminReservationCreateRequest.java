package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record AdminReservationCreateRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {
}
