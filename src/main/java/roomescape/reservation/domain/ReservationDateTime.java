package roomescape.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Getter;
import roomescape.reservationTime.domain.ReservationTime;

@Embeddable
@Getter
public class ReservationDateTime {

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationTime startAt;

    public ReservationDateTime(final LocalDate date, final ReservationTime startAt) {
        this.date = date;
        this.startAt = startAt;
    }

    protected ReservationDateTime() {
    }

    public boolean isBeforeOrToday(final LocalDate today) {
        return date.isBefore(today) || date.isEqual(today);
    }
}
