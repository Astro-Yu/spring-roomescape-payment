package roomescape.member.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDeleted(boolean deleted);
}
