package roomescape.reservationTime.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationTime.exception.DuplicatedReservationTimeException;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;

@Service
@Transactional
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime createReservationTime(final ReservationTimeCreateRequest request) {
        validateDuplication(request.startAt());
        ReservationTime reservationTime = request.toReservationTime();
        return reservationTimeRepository.save(reservationTime);
    }

    private void validateDuplication(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicatedReservationTimeException();
        }
    }
}
