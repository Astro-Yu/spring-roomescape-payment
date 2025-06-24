package roomescape.auth.ui;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.AuthenticatedUserNameResponse;
import roomescape.auth.service.AuthService;
import roomescape.config.dto.SessionMember;
import roomescape.config.resolver.Login;
import roomescape.member.domain.Name;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/check")
    public AuthenticatedUserNameResponse getAuthenticatedUserName(@Login SessionMember sessionMember) {
        Name name = authService.getAuthenticatedMemberName(sessionMember.id());
        return AuthenticatedUserNameResponse.toResponse(name);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest request,
            final HttpSession session
    ) {
        authService.login(request, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok().build();
    }
}
