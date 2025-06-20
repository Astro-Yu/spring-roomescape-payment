package roomescape.theme.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeCreateRequest;
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

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeCreateRequest request) {
        Theme theme = themeService.createTheme(request);
        ThemeResponse response = ThemeResponse.from(theme);

        return ResponseEntity.created(URI.create("/api/themes" + theme.getId())).body(response);
    }
}
