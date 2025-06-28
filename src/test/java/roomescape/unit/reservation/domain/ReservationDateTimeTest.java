package roomescape.unit.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.reservation.domain.ReservationDateTime;

public class ReservationDateTimeTest {

    static Stream<Arguments> invalidDates() {
        return Stream.of(
                Arguments.arguments(LocalDate.of(2025, 5, 7), false),
                Arguments.arguments(LocalDate.of(2025, 5, 6), false),
                Arguments.arguments(LocalDate.of(2025, 5, 5), true),
                Arguments.arguments(LocalDate.of(2025, 5, 4), true),
                Arguments.arguments(LocalDate.of(2025, 5, 3), true)
        );
    }

    @DisplayName("예약 시간날짜가 당일, 혹은 과거인지 확인합니다.")
    @ParameterizedTest
    @MethodSource("invalidDates")
    void isBeforeOrToday(LocalDate date, boolean expected) {
        // given
        LocalDate today = LocalDate.of(2025, 5, 5);
        ReservationDateTime dateTime = new ReservationDateTime(date, null);
        // when & then
        assertThat(dateTime.isBeforeOrToday(today)).isEqualTo(expected);
    }
}
