package roomescape.reservationTime.ui;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.response.ReservationTimeResponse;
import roomescape.reservationTime.service.ReservationTimeService;

@RestController
@RequestMapping("/api/reservation-times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponse> getAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.getReservationTimes();

        return reservationTimes.stream().
                map(ReservationTimeResponse::from)
                .toList();
    }
}
