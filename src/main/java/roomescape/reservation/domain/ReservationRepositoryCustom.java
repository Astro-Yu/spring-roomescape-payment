package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> searchByConditions(LocalDate dateFrom, LocalDate dateTo, Long memberId, Long themeId);
}
