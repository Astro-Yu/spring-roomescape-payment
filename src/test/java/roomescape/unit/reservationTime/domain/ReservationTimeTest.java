package roomescape.unit.reservationTime.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.exception.InvalidReservationTimeException;

public class ReservationTimeTest {

    static Stream<Arguments> invalidTime() {
        return Stream.of(
                Arguments.arguments(LocalTime.of(9, 0)),
                Arguments.arguments(LocalTime.of(8, 0)),
                Arguments.arguments(LocalTime.of(22, 0)),
                Arguments.arguments(LocalTime.of(23, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTime")
    @DisplayName("유효하지 않은 예약 시간을 검사합니다.")
    void validateReservationTime(LocalTime invalidTime) {
        // when & then
        assertThatCode(() -> new ReservationTime(1L, invalidTime))
                .isInstanceOf(InvalidReservationTimeException.class)
                .hasMessage("10시 이전, 22시 이후 예약은 불가능합니다.");
    }
}
