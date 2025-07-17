package forun.hub.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"forun.hub.api.domain.topico", "forun.hub.api.domain.usuarios", "forun.hub.api.domain.curso","forun.hub.api.domain.resposta", "forun.hub.api.domain.perfis"})
@EnableJpaRepositories(basePackages = "forun.hub.api.domain")
public class ApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiApplication.class, args);
	}

}
