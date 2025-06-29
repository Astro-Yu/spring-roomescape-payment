package roomescape.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.domain.Name;
import roomescape.member.domain.Role;

public class MemberTest {
    @Test
    @DisplayName("id가 같을 때, 같은 멤버인지 확인합니다.")
    void isSameMember() {
        // given
        Member member1 = new Member(1L, new Name("하하"), new Credentials("이메일1", "비밀번호 1234"), Role.ADMIN, false);
        Member member2 = new Member(1L, new Name("호호"), new Credentials("이메일2", "비밀번호 1234"), Role.USER, false);

        // when & then
        assertThat(member1.equals(member2)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("같은 id인지 확인합니다.")
    @CsvSource(value = {"1, true", "2, false", "3, false"})
    void isSameId(Long id, boolean expected) {
        // given
        Member member1 = new Member(1L, new Name("하하"), new Credentials("이메일1", "비밀번호 1234"), Role.ADMIN, false);

        // when & then
        assertThat(member1.isSameId(id)).isEqualTo(expected);
    }
}
