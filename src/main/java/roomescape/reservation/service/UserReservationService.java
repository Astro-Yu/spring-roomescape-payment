package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.exception.InvalidAuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.dto.request.UserReservationCreateRequest;
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
@Transactional
public class UserReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public UserReservationService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository,
                                  final ThemeRepository themeRepository,
                                  final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Reservation creaetReservation(final UserReservationCreateRequest request, final Long memberId) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        ReservationDateTime dateTime = new ReservationDateTime(request.date(), time);

        validateDuplicateReservation(dateTime, theme);
        validateBeforeOrTodayDate(dateTime);

        Reservation reservation = Reservation.createWithoutIdAndPayment(request.date(), time, theme, member);
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

    public List<Reservation> getMyReservations(Long id) {
        return reservationRepository.findAllByMemberId(id);
    }

    public void deleteMyReservation(final Long id, final Long memberId) {
        validateReservationExists(id);
        validateReservationIsMine(id, memberId);
        reservationRepository.deleteById(id);
    }

    private void validateReservationExists(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException();
        }
    }

    private void validateReservationIsMine(final Long id, final Long memberId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        if (reservation.isNotOwnedBy(memberId)) {
            throw new InvalidAuthorizationException();
        }
    }
}
