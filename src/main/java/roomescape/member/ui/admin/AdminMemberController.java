package roomescape.member.ui.admin;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.Member;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.service.AdminMemberService;

@RestController
@RequestMapping("/api/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    public AdminMemberController(final AdminMemberService adminMemberService) {
        this.adminMemberService = adminMemberService;
    }

    @GetMapping()
    public List<MemberResponse> getAllMembersByStatus(@RequestParam(defaultValue = "all") String status) {
        List<Member> members = adminMemberService.findMembersByStatus(status);

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        adminMemberService.deleteMemberById(id);

        return ResponseEntity.noContent().build();
    }
}
