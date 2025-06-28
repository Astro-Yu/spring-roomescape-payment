package roomescape.unit.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
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
import roomescape.reservation.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.exception.DuplicateReservationException;
import roomescape.reservation.exception.PastOrPresentReservationException;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservation.service.AdminReservationService;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.infrastructure.ThemeRepository;

@ExtendWith(MockitoExtension.class)
public class AdminReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AdminReservationService adminReservationService;

    @Test
    @DisplayName("예약 생성을 테스트합니다.")
    void createReservationSuccess() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, new ThemeName("테마 이름"), "description", "thumbnail");
        Member member = new Member(1L, new Name("이름"), new Credentials("이메일", "비밀번호123456"), Role.USER, false);
        given(reservationTimeRepository.findById(any())).willReturn(Optional.of(time));
        given(themeRepository.findById(any())).willReturn(Optional.of(theme));
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        Reservation reservationWithoutId = Reservation.createWithoutId(date, time, theme, member);
        Reservation reservation = new Reservation(1L, new ReservationDateTime(date, time), theme, member);
        given(reservationRepository.save(reservationWithoutId)).willReturn(reservation);

        AdminReservationCreateRequest request = new AdminReservationCreateRequest(date, 1L, 1L, 1L);
        // when & then
        assertThat(adminReservationService.createReservation(request)).isEqualTo(reservation);
    }

    @Test
    @DisplayName("예약 생성시 중복 예약이면 예외가 발생합니다.")
    void createReservationFailWithDuplicateReservation() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, new ThemeName("테마 이름"), "description", "thumbnail");
        Member member = new Member(1L, new Name("이름"), new Credentials("이메일", "비밀번호123456"), Role.USER, false);
        given(reservationTimeRepository.findById(any())).willReturn(Optional.of(time));
        given(themeRepository.findById(any())).willReturn(Optional.of(theme));
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        given(reservationRepository.existsByDateTimeAndTheme(any(), any())).willReturn(true);
        AdminReservationCreateRequest request = new AdminReservationCreateRequest(date, 1L, 1L, 1L);

        // when & then
        assertThatCode(() -> adminReservationService.createReservation(request))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("같은 날짜, 시간, 테마에 예약이 이미 존재합니다.");
    }

    @Test
    @DisplayName("예약 생성시 과거 혹은 당일 예약이면 예외가 발생합니다.")
    void createReservationFailWithBeforeOrToday() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, new ThemeName("테마 이름"), "description", "thumbnail");
        Member member = new Member(1L, new Name("이름"), new Credentials("이메일", "비밀번호123456"), Role.USER, false);
        given(reservationTimeRepository.findById(any())).willReturn(Optional.of(time));
        given(themeRepository.findById(any())).willReturn(Optional.of(theme));
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        given(reservationRepository.existsByDateTimeAndTheme(any(), any())).willReturn(false);
        AdminReservationCreateRequest request = new AdminReservationCreateRequest(date, 1L, 1L, 1L);

        // when & then
        assertThatCode(() -> adminReservationService.createReservation(request))
                .isInstanceOf(PastOrPresentReservationException.class)
                .hasMessage("당일 예약 및 과거 예약은 생성할 수 없습니다.");
    }


}
