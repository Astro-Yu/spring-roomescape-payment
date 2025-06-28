package roomescape.unit.reservationTime.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.config.RepositoryTestBase;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;

@DataJpaTest
@Sql(value = "/sql/ReservationTime.sql")
public class ReservationTimeRepositoryTest extends RepositoryTestBase {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간이 존재하는지 예약 시간으로 확인합니다.")
    void existByStartAt() {
        // given
        LocalTime time = LocalTime.of(11, 0);
        // when & then
        assertThat(reservationTimeRepository.existsByStartAt(time)).isTrue();
    }
}
