package roomescape.unit.reservation.service;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.domain.Name;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservation.service.UserReservationService;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.infrastructure.ThemeRepository;

@ExtendWith(MockitoExtension.class)
public class UserReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserReservationService userReservationService;

    private Theme theme1 = new Theme(1L, new ThemeName("테마1"), "설명1", "썸네일1");
    private Theme theme2 = new Theme(2L, new ThemeName("테마2"), "설명2", "썸네일2");

    private ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private ReservationTime time2 = new ReservationTime(1L, LocalTime.of(11, 0));

    private LocalDate date = LocalDate.now().plusDays(1);

    private Member member1 = new Member(1L, new Name("회원1"), new Credentials("이메일", "비밀번호12345"), Role.USER, false);

    @Test
    @DisplayName("자신의 예약을 가져옵니다.")
    void getMyReservations() {
        // given
        Reservation reservation1 = new Reservation(1L, new ReservationDateTime(date, time1), theme1, member1, null,
                ReservationStatus.PAID);
        Reservation reservation2 = new Reservation(2L, new ReservationDateTime(date, time2), theme2, member1, null,
                ReservationStatus.PAID);
        Reservation reservation3 = new Reservation(3L, new ReservationDateTime(date, time1), theme2, member1, null,
                ReservationStatus.PAID);
        Reservation reservation4 = new Reservation(4L, new ReservationDateTime(date, time2), theme1, member1, null,
                ReservationStatus.PAID);

        given(reservationRepository.findAllByMemberId(member1.getId())).willReturn(
                List.of(reservation1, reservation2, reservation3, reservation4));
        
        // when
        List<Reservation> reservations = userReservationService.getMyReservations(member1.getId());

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(reservations).hasSize(4);
        soft.assertThat(reservations.stream()
                        .allMatch(reservation -> reservation.getMember().equals(member1)))
                .isTrue();
        soft.assertAll();
    }
}
