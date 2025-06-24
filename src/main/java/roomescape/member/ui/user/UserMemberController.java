package roomescape.member.ui.user;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.dto.SessionMember;
import roomescape.config.resolver.Login;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.MemberCreateRequest;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.service.MemberService;
import roomescape.member.service.UserMemberService;

@RestController
@RequestMapping("/api/members")
public class UserMemberController {

    private final UserMemberService userMemberService;
    private final MemberService memberService;

    public UserMemberController(final UserMemberService userMemberService, final MemberService memberService) {
        this.userMemberService = userMemberService;
        this.memberService = memberService;
    }

    @GetMapping("/my")
    public MemberResponse getMyMember(@Login SessionMember sessionMember) {
        Member member = userMemberService.getMyMember(sessionMember.id());
        return MemberResponse.from(member);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        Member member = userMemberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/sign-up" + member.getId())).build();
    }

    @DeleteMapping("/my")
    public ResponseEntity<Void> deleteMyMember(@Login SessionMember sessionMember) {
        memberService.deleteMemberById(sessionMember.id());

        return ResponseEntity.noContent().build();
    }
}
