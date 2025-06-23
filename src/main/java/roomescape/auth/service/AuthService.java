package roomescape.auth.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.exception.InvalidAuthenticationException;
import roomescape.config.dto.SessionMember;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void login(final LoginRequest request, final HttpSession session) {
        // 인증 과정
        Credentials credentials = request.toCredentials();
        Member member = memberRepository.findMemberByCredentials(credentials)
                .orElseThrow(InvalidAuthenticationException::new);

        // 세션에 저장
        session.setAttribute("LOGIN_MEMBER", new SessionMember(member.getId(), member.getName(), member.getRole()));
    }

    public void logout(final HttpSession session) {
        session.invalidate(); // 세션 무효화
    }
}
