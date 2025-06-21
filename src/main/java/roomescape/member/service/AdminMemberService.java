package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;

@Service
public class AdminMemberService {
    private final MemberRepository memberRepository;

    public AdminMemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
