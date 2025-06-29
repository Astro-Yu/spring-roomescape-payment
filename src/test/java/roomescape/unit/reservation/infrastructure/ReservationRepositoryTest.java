package roomescape.unit.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.config.RepositoryTestBase;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.ThemeRepository;

@DataJpaTest
@Sql(value = {"/sql/Member.sql", "/sql/ReservationTime.sql", "/sql/Theme.sql"})
public class ReservationRepositoryTest extends RepositoryTestBase {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;
    private Member member2;

    private ReservationTime time1;
    private ReservationTime time2;

    private Theme theme1;
    private Theme theme2;

    @BeforeEach
    void setUp() {
        List<Member> members = memberRepository.findAll();
        List<ReservationTime> times = reservationTimeRepository.findAll();
        List<Theme> themes = themeRepository.findAll();

        member1 = members.get(0);
        member2 = members.get(1);

        time1 = times.get(0);
        time2 = times.get(1);

        theme1 = themes.get(0);
        theme2 = themes.get(1);
    }

    @Test
    @DisplayName("예약날짜시간과 테마로 이미 존재하는 예약인지 확인합니다.")
    void existByDateTimeAndTheme() {
        // given
        LocalDate date = LocalDate.of(2025, 5, 5);

        ReservationDateTime dateTime = new ReservationDateTime(date, time1);

        Reservation reservation = Reservation.createWithoutIdAndPayment(date, time1, theme1, member1);
        reservationRepository.save(reservation);

        // when & then
        assertThat(reservationRepository.existsByDateTimeAndTheme(dateTime, theme1)).isTrue();
    }

    @Test
    @DisplayName("조건으로 예약을 검색합니다.")
    void searchByConditions() {
        // given
        LocalDate date1 = LocalDate.of(2025, 5, 5);

        Reservation reservation1 = Reservation.createWithoutIdAndPayment(date1, time1, theme1, member1);
        Reservation reservation2 = Reservation.createWithoutIdAndPayment(date1, time1, theme2, member1);

        Reservation reservation3 = Reservation.createWithoutIdAndPayment(date1, time2, theme1, member2);
        Reservation reservation4 = Reservation.createWithoutIdAndPayment(date1, time2, theme2, member2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        // when
        List<Reservation> reservations = reservationRepository.searchByConditions(null, null, null, theme1.getId());

        // then
        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("회원 ID로 예약들을 검색합니다.")
    void findReservationByMemberId() {
        // given
        LocalDate date1 = LocalDate.of(2025, 5, 5);

        Reservation reservation1 = Reservation.createWithoutIdAndPayment(date1, time1, theme1, member1);
        Reservation reservation2 = Reservation.createWithoutIdAndPayment(date1, time1, theme2, member1);

        Reservation reservation3 = Reservation.createWithoutIdAndPayment(date1, time2, theme1, member2);
        Reservation reservation4 = Reservation.createWithoutIdAndPayment(date1, time2, theme2, member2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        // when
        List<Reservation> reservations = reservationRepository.findAllByMemberId(member1.getId());

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(reservations).hasSize(2);
        soft.assertThat(reservations.stream()
                        .allMatch(reservation -> reservation.getMember().equals(member1)))
                .isTrue();
        soft.assertAll();
    }
}
