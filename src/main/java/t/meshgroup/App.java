package t.meshgroup;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import t.meshgroup.utils.autoloaders.databases.DBInitPostgreSQL;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class App {
    @SneakyThrows
    public static void main(String[] args) {
        DBInitPostgreSQL.initDomain(null);
        SpringApplication.run(App.class, args);
    }
}
