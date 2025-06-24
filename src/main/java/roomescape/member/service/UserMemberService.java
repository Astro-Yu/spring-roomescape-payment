package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.MemberCreateRequest;
import roomescape.member.exception.DuplicateEmailException;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;

@Service
public class UserMemberService {

    private final MemberRepository memberRepository;

    public UserMemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMyMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member createMember(final MemberCreateRequest request) {
        Credentials credentials = request.toCredentials();
        String email = credentials.getEmail();
        if (memberRepository.existsByCredentials_Email(email)) {
            throw new DuplicateEmailException();
        }
        Member member = Member.createWithoutIdWhenUserSignUp(request.name(), request.email(), request.password());
        return memberRepository.save(member);
    }
}
