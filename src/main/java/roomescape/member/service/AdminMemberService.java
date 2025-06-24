package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.domain.Member;
import roomescape.member.exception.InvalidMemberStatusException;
import roomescape.member.infrastructure.MemberRepository;

@Service
@Transactional
public class AdminMemberService {
    private final MemberRepository memberRepository;

    public AdminMemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<Member> findMembersByStatus(String status) {
        if (status.equals("all")) {
            return memberRepository.findAll();
        }
        if (status.equals("deleted")) {
            return memberRepository.findByDeleted(true);
        }
        if (status.equals("active")) {
            return memberRepository.findByDeleted(false);
        }
        throw new InvalidMemberStatusException();
    }
}
