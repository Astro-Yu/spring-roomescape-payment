package roomescape.unit.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.domain.Name;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;

public class ReservationTest {
    @ParameterizedTest
    @DisplayName("예약이 특정 멤버의 소유인지 확인합니다.")
    @CsvSource(value = {"1, false", "2, true", "3, true"})
    void isNotOwnedTest(Long id, boolean expected) {
        // given
        Member member1 = new Member(1L, new Name("하하"), new Credentials("이메일1", "비밀번호 1234"), Role.ADMIN, false);
        Reservation reservation = new Reservation(null, null, null, member1, null, null);

        // when & then
        assertThat(reservation.isNotOwnedBy(id)).isEqualTo(expected);
    }
}
