package roomescape.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.member.domain.Credentials;
import roomescape.member.exception.InvalidPasswordLengthException;

public class CredentialTest {
    @Test
    @DisplayName("동일한 인증 정보인지 확인")
    void sameCredential() {
        // given
        Credentials credentials1 = new Credentials("email@email.com", "goodddddd1");
        Credentials credentials2 = new Credentials("email@email.com", "goodddddd1");
        // when & then
        assertThat(credentials1.equals(credentials2)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "7자비밀번호0", "21자비밀번호00000000000000"})
    void validatePassword(String password) {
        // when & then
        assertThatCode(() -> new Credentials("email@email.com", password))
                .isInstanceOf(InvalidPasswordLengthException.class)
                .hasMessage("비밀번호는 8자 이상, 20자 이하여야 합니다.");

    }
}
