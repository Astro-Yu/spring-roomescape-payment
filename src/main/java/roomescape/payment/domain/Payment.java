package roomescape.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Payment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private int amount;

    public Payment(final Long id, final String orderId, final String paymentKey, final int amount) {
        this.id = id;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }

    public Payment() {
    }

    public static Payment createWithoutId(final String orderId, final String paymentKey, final int amount) {
        return new Payment(null, orderId, paymentKey, amount);
    }

    public static Payment createFreePayment() {
        return new Payment(null, "freeOrder", "freePayment", 0);
    }
}
