package roomescape.payment.dto.request;

public record PaymentRequest(String orderId, String paymentKey, int amount) {
}
