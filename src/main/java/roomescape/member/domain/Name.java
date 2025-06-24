package roomescape.member.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import roomescape.member.exception.InvalidNameLengthException;

@Embeddable
@Getter
public class Name implements Serializable {
    private static final int MAX_NAME_LENGTH = 10;

    private String name;

    public Name(final String name) {
        validateNameLength(name);
        this.name = name;
    }

    public Name() {
    }

    private void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH || name.isEmpty()) {
            throw new InvalidNameLengthException();
        }
    }
}
