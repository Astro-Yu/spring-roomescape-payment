package roomescape.unit.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.exception.InvalidAuthenticationException;
import roomescape.auth.service.AuthService;
import roomescape.config.dto.SessionMember;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.domain.Name;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest = new LoginRequest("email@email.com", "password123");
    private Credentials credentials = loginRequest.toCredentials();
    private Member member = new Member(1L, new Name("유석현"), new Credentials("email@email.com", "password123"),
            Role.ADMIN,
            false);

    @Test
    @DisplayName("로그인을 테스트합니다.")
    void loginSuccessTest() {
        // given
        given(memberRepository.findMemberByCredentials(credentials)).willReturn(Optional.of(member));

        // when
        authService.login(loginRequest, httpSession);

        // then
        then(memberRepository).should(times(1)).findMemberByCredentials(credentials);

        ArgumentCaptor<SessionMember> captor = ArgumentCaptor.forClass(SessionMember.class);
        then(httpSession).should(times(1)).setAttribute(eq("LOGIN_MEMBER"), captor.capture());

        SessionMember savedSessionMember = captor.getValue();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(savedSessionMember.id()).isEqualTo(member.getId());
        soft.assertThat(savedSessionMember.name()).isEqualTo(member.getName());
        soft.assertThat(savedSessionMember.role()).isEqualTo(member.getRole());
        soft.assertAll();
    }

    @Test
    @DisplayName("로그인 실패 시, 예외가 발생합니다.")
    void loginFailTest() {
        // given
        given(memberRepository.findMemberByCredentials(any())).willReturn(Optional.empty());
        // when & then
        assertThatCode(() -> authService.login(loginRequest, httpSession))
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
    }
}
