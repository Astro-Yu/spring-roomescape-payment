package roomescape.unit.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.reservationTime.service.ReservationTimeService;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {

    private final ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
    private final ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.of(12, 0));

    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("모든 예약 시간을 조회합니다.")
    void getReservationTimes() {
        // given
        List<ReservationTime> times = List.of(reservationTime1, reservationTime2, reservationTime3);
        given(reservationTimeRepository.findAll()).willReturn(times);

        // when
        List<ReservationTime> foundTimes = reservationTimeService.getReservationTimes();

        // then
        assertThat(foundTimes).hasSize(3);
    }

    @Test
    @DisplayName("예약 시간을 생성합니다.")
    void createReservationTimes() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        given(reservationTimeRepository.save(any())).willReturn(time);

        // when & then
        ReservationTime savedTime = reservationTimeService.createReservationTime(request);

        // then
        assertThat(savedTime.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복 예약시간 생성 시, 예외가 발생합니다.")
    void createReservationTimesWithException() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        given(reservationTimeRepository.save(any())).willReturn(time);

        // when & then
        ReservationTime savedTime = reservationTimeService.createReservationTime(request);

        // then
        assertThat(savedTime.getId()).isEqualTo(1L);
    }
}
