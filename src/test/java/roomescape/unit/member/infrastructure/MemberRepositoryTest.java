package roomescape.unit.member.infrastructure;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;

@DataJpaTest()
@Sql("/sql/Member.sql")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("삭제된 멤버 조회")
    void findDeletedMember() {
        // when & then
        List<Member> deletedMembers = memberRepository.findByDeleted(true);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(deletedMembers).hasSize(2);
        soft.assertThat(deletedMembers.getFirst().getId()).isEqualTo(3L);
        soft.assertAll();
    }

    @Test
    @DisplayName("존재하는 멤버 조회")
    void findActiveMember() {
        // when & then
        List<Member> activeMembers = memberRepository.findByDeleted(false);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(activeMembers).hasSize(2);
        soft.assertThat(activeMembers.getFirst().getId()).isEqualTo(1L);
        soft.assertAll();
    }
}
