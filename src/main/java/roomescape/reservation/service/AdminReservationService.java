package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationSearchFilter;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.PastOrPresentReservationException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.exception.ReservationTimeNotFoundException;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.infrastructure.ThemeRepository;

@Service
public class AdminReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public AdminReservationService(final ReservationRepository reservationRepository,
                                   final ReservationTimeRepository reservationTimeRepository,
                                   final ThemeRepository themeRepository,
                                   final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }


    public Reservation createReservation(final AdminReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new);
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(MemberNotFoundException::new);

        ReservationDateTime dateTime = new ReservationDateTime(request.date(), time);

        validateDuplicateReservation(dateTime, theme);
        validateBeforeOrTodayDate(dateTime);

        Reservation reservation = Reservation.createFreeReservationWithoutId(request.date(), time, theme, member);

        return reservationRepository.save(reservation);
    }

    private void validateDuplicateReservation(ReservationDateTime dateTime, Theme theme) {
        if (reservationRepository.existsByDateTimeAndTheme(dateTime, theme)) {
            throw new DuplicateReservationException();
        }
    }

    private void validateBeforeOrTodayDate(final ReservationDateTime dateTime) {
        if (dateTime.isBeforeOrToday(LocalDate.now())) {
            throw new PastOrPresentReservationException();
        }
    }

    public List<Reservation> searchReservations(final ReservationSearchFilter filter) {
        return reservationRepository.searchByConditions(
                filter.dateFrom(),
                filter.dateTo(),
                filter.memberId(),
                filter.themeId());
    }

    public void deleteReservation(final Long id) {
        validateReservationExists(id);
        reservationRepository.deleteById(id);
    }

    private void validateReservationExists(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException();
        }
    }
}
