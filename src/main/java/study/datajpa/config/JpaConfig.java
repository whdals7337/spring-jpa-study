package study.datajpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "study.datajpa.repository",
        repositoryImplementationPostfix = "Impl")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return  () -> Optional.of(UUID.randomUUID().toString());
    }
}

/* EnableJpaRepositories을 xml에서 설정하는 방법
<repositories base-package="study.datajpa.repository"
 repository-impl-postfix="Impl" />
 */