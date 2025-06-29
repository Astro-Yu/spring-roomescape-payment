package roomescape.reservation.infrastructure;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReservationRepositoryCustom;
import roomescape.theme.domain.Theme;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    boolean existsByDateTimeAndTheme(final ReservationDateTime dateTime, final Theme theme);

    List<Reservation> searchByConditions(LocalDate dateFrom, LocalDate dateTo, Long memberId, Long themeId);

    List<Reservation> findAllByMemberId(Long id);
}
