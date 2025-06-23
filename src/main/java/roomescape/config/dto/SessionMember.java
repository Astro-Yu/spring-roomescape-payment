package roomescape.config.dto;

import roomescape.member.domain.Name;
import roomescape.member.domain.Role;

public record SessionMember(Long id, Name name, Role role) {
}
