package roomescape.theme.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeCreateRequest;
import roomescape.theme.infrastructure.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(final ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    public Theme createTheme(final ThemeCreateRequest request) {
        Theme theme = request.toTheme();
        return themeRepository.save(theme);
    }
}
