package roomescape.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.payment.domain.Payment;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.dto.response.PaymentResponse;
import roomescape.payment.infrastructure.PaymentClient;
import roomescape.payment.infrastructure.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    public PaymentService(final PaymentClient paymentClient, final PaymentRepository paymentRepository) {
        this.paymentClient = paymentClient;
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(PaymentRequest request) {
        PaymentResponse response = paymentClient.approve(request);
        Payment payment = response.toPayment();
        return paymentRepository.save(payment);
    }
}
