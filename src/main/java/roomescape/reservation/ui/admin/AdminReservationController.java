package roomescape.reservation.ui.admin;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationSearchFilter;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.AdminReservationService;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(final AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @GetMapping("/search")
    public List<ReservationResponse> searchReservations(@ModelAttribute ReservationSearchFilter filter) {
        List<Reservation> filteredReservation = adminReservationService.searchReservations(filter);
        return filteredReservation.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody AdminReservationCreateRequest request) {
        Reservation reservation = adminReservationService.createReservation(request);
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/api/admin/reservations/" + reservation.getId())).body(response);
    }
}
