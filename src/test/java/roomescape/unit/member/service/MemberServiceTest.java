package roomescape.unit.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Optional;
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
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member1 = new Member(1L, new Name("이름1"), new Credentials("이메일1", "비번1111111"), Role.ADMIN, true);
    private Member member2 = new Member(2L, new Name("이름2"), new Credentials("이메일2", "비번2222222"), Role.USER, true);
    private Member member3 = new Member(3L, new Name("이름3"), new Credentials("이메일3", "비번3333333"), Role.USER, false);
    private Member member4 = new Member(4L, new Name("이름4"), new Credentials("이메일4", "비번4444444"), Role.USER, false);

    @Test
    @DisplayName("id로 특정 회원을 삭제합니다.")
    void deleteMemberById() {
        //given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member1));

        // when
        memberService.deleteMemberById(1L);

        // then
        then(memberRepository).should(times(1)).findById(1L);
        assertThat(member1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("id로 삭제 시도 시, 회원이 없으면 예외가 발생합니다.")
    void deleteMemberByIdWithException() {
        //given
        given(memberRepository.findById(5L)).willReturn(Optional.empty());

        // when & then
        assertThatCode(() -> memberService.deleteMemberById(5L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다.");

        then(memberRepository).should(times(1)).findById(5L);
    }
}
