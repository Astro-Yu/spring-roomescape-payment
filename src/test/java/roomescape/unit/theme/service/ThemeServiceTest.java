package roomescape.unit.theme.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.dto.request.ThemeCreateRequest;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.theme.service.ThemeService;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    private final Theme theme1 = new Theme(1L, new ThemeName("이름1"), "설명1", "섬네일1");
    private final Theme theme2 = new Theme(2L, new ThemeName("이름2"), "설명2", "섬네일2");
    private final Theme theme3 = new Theme(3L, new ThemeName("이름3"), "설명3", "섬네일3");

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("모든 테마를 조회합니다.")
    void getAllThemes() {
        // given
        List<Theme> themes = List.of(theme1, theme2, theme3);
        given(themeRepository.findAll()).willReturn(themes);

        // when
        List<Theme> foundThemes = themeService.findAllThemes();
        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(foundThemes).hasSize(3);
        soft.assertThat(foundThemes.getFirst().getId()).isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @DisplayName("테마를 생성합니다.")
    void createTheme() {
        // given
        ThemeCreateRequest request = new ThemeCreateRequest("이름1", "설명1", "섬네일1");
        Theme theme = new Theme(1L, new ThemeName("이름1"), "설명1", "섬네일1");
        given(themeRepository.save(any())).willReturn(theme);

        // when
        Theme savedTheme = themeService.createTheme(request);

        // then
        assertThat(savedTheme.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테마를 삭제합니다.")
    void deleteTheme() {
        // given
        given(themeRepository.existsById(any())).willReturn(true);
        // when
        themeService.deleteTheme(1L);
        // then
        verify(themeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("없는 테마 삭제 시, 예외가 발생합니다.")
    void deleteThemeException() {
        // given
        given(themeRepository.existsById(any())).willReturn(false);

        // when & then
        assertThatCode(() -> themeService.deleteTheme(any()))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }
}
