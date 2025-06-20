package roomescape.theme.ui;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeService.findAllThemes();

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
