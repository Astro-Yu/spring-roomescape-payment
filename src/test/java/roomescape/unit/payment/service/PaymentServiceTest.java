package roomescape.unit.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.payment.domain.Payment;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.dto.response.PaymentResponse;
import roomescape.payment.infrastructure.PaymentClient;
import roomescape.payment.infrastructure.PaymentRepository;
import roomescape.payment.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    PaymentClient paymentClient;

    @InjectMocks
    PaymentService paymentService;

    @Test
    @DisplayName("결제 생성 테스트")
    void createPaymentSuccess() {
        // given
        PaymentRequest request = new PaymentRequest("123", "456", 1000);
        PaymentResponse response = new PaymentResponse("123", "456", 1000);
        Payment paymentWithId = new Payment(1L, "123", "456", 1000);
        given(paymentClient.approve(request)).willReturn(response);
        given(paymentRepository.save(any())).willReturn(paymentWithId);

        // when
        Payment payment = paymentService.createPayment(request);

        // then
        assertThat(payment.getId()).isEqualTo(1L);
    }
}
