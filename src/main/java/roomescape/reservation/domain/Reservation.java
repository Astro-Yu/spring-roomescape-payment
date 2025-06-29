package roomescape.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import roomescape.member.domain.Member;
import roomescape.payment.domain.Payment;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Entity
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ReservationDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne
    private Payment payment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(final Long id, final ReservationDateTime dateTime, final Theme theme, final Member member,
                       final Payment payment,
                       final ReservationStatus status) {
        this.id = id;
        this.dateTime = dateTime;
        this.theme = theme;
        this.member = member;
        this.payment = payment;
        this.status = status;
    }

    protected Reservation() {
    }

    public static Reservation createWithoutIdAndPayment(final LocalDate date, final ReservationTime time,
                                                        final Theme theme,
                                                        final Member member) {
        return new Reservation(null, new ReservationDateTime(date, time), theme, member, null,
                ReservationStatus.PENDING);
    }

    public static Reservation createFreeReservationWithoutId(final LocalDate date, final ReservationTime time,
                                                             final Theme theme,
                                                             final Member member) {
        return new Reservation(null, new ReservationDateTime(date, time), theme, member, Payment.createFreePayment(),
                ReservationStatus.FREE);
    }

    public void confirmWithPayment(Payment userPayment) {
        if (status == ReservationStatus.PENDING && payment == null) {
            status = ReservationStatus.PAID;
            payment = userPayment;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean isNotOwnedBy(Long memberId) {
        return !member.isSameId(memberId);
    }
}
