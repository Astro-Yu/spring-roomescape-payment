package roomescape.unit.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
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
import roomescape.member.dto.request.MemberCreateRequest;
import roomescape.member.exception.DuplicateEmailException;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.member.service.UserMemberService;

@ExtendWith(MockitoExtension.class)
public class UserMemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserMemberService userMemberService;

    @Test
    @DisplayName("자신의 멤버 정보를 조회합니다.")
    void getMyMemberSuccess() {
        // given
        Member member = new Member(1L, new Name("김수한"), new Credentials("이메일", "비번12345678"), Role.USER, false);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        // when
        Member findMember = userMemberService.getMyMember(1L);
        // then
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    @DisplayName("자신의 멤버 정보를 조회 실패 시, 예외가 발생합니다.")
    void getMyMemberFail() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.empty());
        // when & then
        assertThatCode(() -> userMemberService.getMyMember(1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("신규 회원을 생성합니다.")
    void createMemberSuccess() {
        // given
        String name = "이름";
        String email = "이메일";
        String password = "비밀번호12345";

        MemberCreateRequest request = new MemberCreateRequest(name, email, password);
        Member member = new Member(1L, new Name(name), new Credentials(email, password), Role.USER, false);
        given(memberRepository.save(any())).willReturn(member);
        given(memberRepository.existsByCredentials_Email(email)).willReturn(false);

        // when
        Member savedMember = userMemberService.createMember(request);

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(savedMember.getName()).isEqualTo(name);
        soft.assertThat(savedMember.getCredentials()).isEqualTo(new Credentials(email, password));
    }

    @Test
    @DisplayName("신규 회원을 생성시, 중복 이메일이 있다면 예외가 발생합니다.")
    void createMemberFail() {
        // given
        String name = "이름";
        String email = "이메일";
        String password = "비밀번호12345";

        MemberCreateRequest request = new MemberCreateRequest(name, email, password);
        given(memberRepository.existsByCredentials_Email(email)).willReturn(true);

        // when
        assertThatCode(() -> userMemberService.createMember(request))
                .isInstanceOf(DuplicateEmailException.class).
                hasMessage("이미 존재하는 이메일입니다.");

    }
}
