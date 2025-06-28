package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.member.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.theme.domain.ThemeName;

public record ReservationResponse(Long id, LocalDate date, LocalTime startAt, String themeName, String memberName) {
    public static ReservationResponse from(Reservation reservation) {
        ReservationDateTime dateTime = reservation.getDateTime();
        ThemeName themeName = reservation.getTheme().getName();
        Name memberName = reservation.getMember().getName();
        return new ReservationResponse(
                reservation.getId(),
                dateTime.getDate(),
                dateTime.getStartAt().getStartAt(),
                themeName.getName(),
                memberName.getName());
    }
}
