package roomescape.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.member.domain.Name;
import roomescape.member.exception.InvalidNameLengthException;

public class NameTest {
    @ParameterizedTest
    @ValueSource(strings = {"11자이름111111", ""})
    @DisplayName("유효하지 않은 사용자 이름 길이를 검사합니다.")
    void invalidNameLength(String name) {
        // when & then
        assertThatCode(() -> new Name(name))
                .isInstanceOf(InvalidNameLengthException.class)
                .hasMessage("이름은 1자 이상, 10자 이하만 가능합니다.");
    }
}
