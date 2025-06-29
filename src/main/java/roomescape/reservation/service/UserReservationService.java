package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.payment.domain.Payment;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.service.PaymentService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.dto.request.UserReservationCreateRequest;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.PastOrPresentReservationException;
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
    private final PaymentService paymentService;

    public UserReservationService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository,
                                  final ThemeRepository themeRepository,
                                  final MemberRepository memberRepository, final PaymentService paymentService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.paymentService = paymentService;
    }

    public Reservation creaetReservation(final UserReservationCreateRequest request, final Long memberId) {
        // 보류 상태 예약 생성
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(ThemeNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Reservation reservation = Reservation.createWithoutIdAndPayment(request.date(), time, theme, member);

        ReservationDateTime dateTime = new ReservationDateTime(request.date(), time);

        validateDuplicateReservation(dateTime, theme);
        validateBeforeOrTodayDate(dateTime);

        PaymentRequest paymentRequest = new PaymentRequest(request.orderId(), request.paymentKey(), request.amount());

        // 결제 시작
        Payment payment = paymentService.createPayment(paymentRequest);

        // 얘약이랑 결제 합체 (상태도 변경됨)
        reservation.confirmWithPayment(payment);

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
}
