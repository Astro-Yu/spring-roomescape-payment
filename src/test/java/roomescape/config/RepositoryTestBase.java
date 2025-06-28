package roomescape.config;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(TestQueryDslConfig.class)
@DataJpaTest
public class RepositoryTestBase {
}
