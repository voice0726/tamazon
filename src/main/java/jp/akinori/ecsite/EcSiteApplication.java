package jp.akinori.ecsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = JdbcRepositoriesAutoConfiguration.class)
public class EcSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcSiteApplication.class, args);
    }

}
