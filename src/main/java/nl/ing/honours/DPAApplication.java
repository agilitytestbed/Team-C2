package nl.ing.honours;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@EnableJpaRepositories
public class DPAApplication {
    public static void main(String[] args) {
        SpringApplication.run(DPAApplication.class, args);
    }
}
