package roomescape.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @Column(nullable = false)
    private Name name;

    @Embedded
    @Column(nullable = false)
    private Credentials credentials;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean deleted;

    public Member(final Long id, final Name name, final Credentials credentials, final Role role,
                  final boolean deleted) {
        this.id = id;
        this.name = name;
        this.credentials = credentials;
        this.role = role;
        this.deleted = deleted;
    }

    public Member() {
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
