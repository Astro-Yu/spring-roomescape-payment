package roomescape.unit.theme.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.exception.InvalidThemeNameLengthException;

public class ThemeNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"21자이름ㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂ", ""})
    @DisplayName("유효하지 않은 이름 길이를 검사합니다.")
    void invalidNameLength(String name) {

        // when & then
        assertThatCode(() -> new ThemeName(name))
                .isInstanceOf(InvalidThemeNameLengthException.class)
                .hasMessage("테마의 이름은 1자 이상, 20자 이하만 가능합니다.");
    }
}
