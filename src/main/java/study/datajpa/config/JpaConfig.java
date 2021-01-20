package study.datajpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "study.datajpa.repository",
        repositoryImplementationPostfix = "Impl")
public class JpaConfig {
}

/* EnableJpaRepositories을 xml에서 설정하는 방법
<repositories base-package="study.datajpa.repository"
 repository-impl-postfix="Impl" />
 */