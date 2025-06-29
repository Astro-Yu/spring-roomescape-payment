package roomescape.payment.dto.response;

import roomescape.payment.domain.Payment;

public record PaymentResponse(String orderId, String paymentKey, int totalAmount) {

    public Payment toPayment() {
        return Payment.createWithoutId(orderId, paymentKey, totalAmount);
    }
}
