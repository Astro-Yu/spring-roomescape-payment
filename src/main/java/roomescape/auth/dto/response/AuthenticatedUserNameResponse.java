package roomescape.auth.dto.response;

import roomescape.member.domain.Name;

public record AuthenticatedUserNameResponse(String name) {

    public static AuthenticatedUserNameResponse toResponse(Name name) {
        return new AuthenticatedUserNameResponse(name.getName());
    }
}
