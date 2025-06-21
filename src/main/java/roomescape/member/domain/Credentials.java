package roomescape.member.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import roomescape.member.exception.InvalidPasswordLengthException;

@Embeddable
@Getter
public class Credentials {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private String email;
    private String password;

    public Credentials(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Credentials() {
    }

    public void validatePasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordLengthException();
        }
    }
}
