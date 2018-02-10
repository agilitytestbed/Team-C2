package nl.ing.honours;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DPAApplication {

    public static void main(String[] args) {
        SpringApplication.run(DPAApplication.class, args);
    }
}
