package roomescape.reservation.ui.user;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.config.dto.SessionMember;
import roomescape.config.resolver.Login;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.UserReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.UserReservationService;

@Controller
@RequestMapping("/api/reservations")
public class UserReservationController {

    private final UserReservationService userReservationService;

    public UserReservationController(final UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @GetMapping("/my")
    public List<ReservationResponse> getMyReservation(@Login SessionMember sessionMember) {
        List<Reservation> reservations = userReservationService.getMyReservations(sessionMember.id());
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createMyReservation(@RequestBody UserReservationCreateRequest request,
                                                                   @Login SessionMember sessionMember) {
        Reservation reservation = userReservationService.creaetReservation(request, sessionMember.id());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/api/reservations/" + response.id())).body(response);
    }
}
