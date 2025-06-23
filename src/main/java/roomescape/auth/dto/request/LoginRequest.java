package roomescape.auth.dto.request;

import roomescape.member.domain.Credentials;

public record LoginRequest(String email, String password) {
    public Credentials toCredentials() {
        return new Credentials(email, password);
    }
}
