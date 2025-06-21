package roomescape.unit.member.service;

import static org.mockito.BDDMockito.given;

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
import roomescape.member.service.AdminMemberService;

@ExtendWith(MockitoExtension.class)
public class AdminMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AdminMemberService adminMemberService;

    private Member member1 = new Member(1L, new Name("이름1"), new Credentials("이메일1", "비번1"), Role.ADMIN, true);
    private Member member2 = new Member(2L, new Name("이름2"), new Credentials("이메일2", "비번2"), Role.USER, true);
    private Member member3 = new Member(3L, new Name("이름3"), new Credentials("이메일3", "비번3"), Role.USER, false);
    private Member member4 = new Member(4L, new Name("이름4"), new Credentials("이메일4", "비번4"), Role.USER, false);

    @Test
    @DisplayName("탈퇴한 모든 멤버 조회")
    void findAllDeletedMembers() {
        // given
        List<Member> deletedMembers = List.of(member3, member4);
        given(memberRepository.findByDeleted(true)).willReturn(deletedMembers);

        // when
        List<Member> members = adminMemberService.findDeletedMember();

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(members).hasSize(2);
        soft.assertThat(members.getFirst()).isEqualTo(member3);
        soft.assertAll();
    }

    @Test
    @DisplayName("탈퇴하지 않은 모든 멤버 조회")
    void findAllActiveMembers() {
        // given
        List<Member> activeMembers = List.of(member1, member2);
        given(memberRepository.findByDeleted(false)).willReturn(activeMembers);

        // when
        List<Member> members = adminMemberService.findActiveMember();

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(members).hasSize(2);
        soft.assertThat(members.getFirst()).isEqualTo(member1);
        soft.assertAll();
    }
}
