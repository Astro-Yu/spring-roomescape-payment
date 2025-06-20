package roomescape.theme.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import roomescape.theme.exception.InvalidThemeNameLengthException;

@Embeddable
@Getter
public class ThemeName {
    private static final int MAX_NAME_LENGTH = 20;
    private String name;

    public ThemeName(final String name) {
        validateName(name);
        this.name = name;
    }

    public ThemeName() {
    }

    private void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH || name.isEmpty()) {
            throw new InvalidThemeNameLengthException();
        }
    }
}
