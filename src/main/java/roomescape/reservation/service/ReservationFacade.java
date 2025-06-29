package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.payment.domain.Payment;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.service.PaymentService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.UserReservationCreateRequest;

@Service
public class ReservationFacade {
    private final PaymentService paymentService;
    private final UserReservationService userReservationService;

    public ReservationFacade(final PaymentService paymentService, final UserReservationService userReservationService) {
        this.paymentService = paymentService;
        this.userReservationService = userReservationService;
    }

    public Reservation createReservationWithPayment(final UserReservationCreateRequest request, final Long memberId) {
        Reservation reservation = userReservationService.creaetReservation(request, memberId);

        PaymentRequest paymentRequest = new PaymentRequest(request.orderId(), request.paymentKey(), request.amount());
        Payment payment = paymentService.createPayment(paymentRequest);

        reservation.confirmWithPayment(payment);
        return reservation;
    }
}
