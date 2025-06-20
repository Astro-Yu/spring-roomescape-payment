package roomescape.reservationTime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.Getter;
import roomescape.reservationTime.exception.InvalidReservationTimeException;

@Entity
@Getter
public class ReservationTime {

    private static final LocalTime FIRST_ORDER_TIME = LocalTime.of(10, 0);
    private static final LocalTime LAST_ORDER_TIME = LocalTime.of(21, 0);


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        validateTime(startAt);
        this.startAt = startAt;
    }

    protected ReservationTime() {
    }

    public static ReservationTime createWithoutId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validateTime(LocalTime time) {
        if (isNotValidTime(time)) {
            throw new InvalidReservationTimeException();
        }
    }

    private boolean isNotValidTime(LocalTime time) {
        return time.isBefore(FIRST_ORDER_TIME) || time.isAfter(LAST_ORDER_TIME);
    }
}
