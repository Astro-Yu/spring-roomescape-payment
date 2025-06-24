package roomescape.member.dto.request;

import roomescape.member.domain.Credentials;

public record MemberCreateRequest(String name, String email, String password) {
    public Credentials toCredentials() {
        return new Credentials(email, password);
    }
}
