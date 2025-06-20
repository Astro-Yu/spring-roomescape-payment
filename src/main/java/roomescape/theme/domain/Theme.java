package roomescape.theme.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Theme {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @Embedded
    @Column(nullable = false)
    private ThemeName name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnail;

    public Theme(final Long id, final ThemeName name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme() {

    }
}
