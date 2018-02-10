package nl.ing.honours;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableEncryptableProperties
@EnableJpaRepositories
@EnableTransactionManagement
public class AutoConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
