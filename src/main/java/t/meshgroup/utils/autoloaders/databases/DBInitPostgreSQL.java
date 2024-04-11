package t.meshgroup.utils.autoloaders.databases;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import t.meshgroup.database.repo.AccountRepository;
import t.meshgroup.database.repo.EmailRepository;
import t.meshgroup.database.repo.PhoneRepository;
import t.meshgroup.database.repo.UserRepository;
import t.meshgroup.models.Account;
import t.meshgroup.models.Email;
import t.meshgroup.models.Phone;
import t.meshgroup.models.User;
import t.meshgroup.service.BalanceStatService;
import t.meshgroup.utils.auth.JwtUtils;
import t.meshgroup.utils.autoloaders.databases.enums.UtilPostgreSQLDomains;

import javax.annotation.Nullable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * If you are need init DataBases tables separate from the application you can use sql scripts.
 *
 * @see [src/main/resources/databases/init/postgresql]
 **/
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DBInitPostgreSQL implements CommandLineRunner {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final AccountRepository accountRepository;
    private final BalanceStatService balanceStatService;

    public static void initDomain(@Nullable String path) throws IOException, SQLException {
        Resource resource = new ClassPathResource(Objects.requireNonNullElse(path, "application.properties"));
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        Connection conn = DriverManager.getConnection(
                props.getProperty("spring.datasource.url"),
                props.getProperty("spring.datasource.username"),
                props.getProperty("spring.datasource.password")
        );

        Statement statement = conn.createStatement();
        initPhoneHandler(conn, statement);
        initAccountBalanceHandler(conn, statement);

        statement.closeOnCompletion();
        conn.close();
    }

    private static void initAccountBalanceHandler(Connection conn, Statement statement) throws SQLException, IOException {


        ResultSet rs = statement.executeQuery(
                "select exists(select 1 from information_schema.domains where domain_name = 'account_balance_domain')"
        );
        rs.next();
        if (rs.getBoolean(1))
            return;

        Resource initScriptResource = new ClassPathResource("databases/init/postgresql/initAccountHandler.sql");
        if (initScriptResource.contentLength() > 0 && !conn.getTypeMap().containsKey(UtilPostgreSQLDomains.ACCOUNT_BALANCE_DOMAIN))
            ScriptUtils.executeSqlScript(conn, initScriptResource);

    }

    private static void initPhoneHandler(Connection conn, Statement statement) throws SQLException, IOException {
        ResultSet rs = statement.executeQuery(
                "select exists(select 1 from information_schema.domains where domain_name = 'phone_number_domain')"
        );
        rs.next();
        if (rs.getBoolean(1))
            return;

        Resource initScriptResource = new ClassPathResource("databases/init/postgresql/initPhoneHandler.sql");
        if (initScriptResource.contentLength() > 0 && !conn.getTypeMap().containsKey(UtilPostgreSQLDomains.PHONE_NUMBER_DOMAIN))
            ScriptUtils.executeSqlScript(conn, initScriptResource);
    }

    @Override
    public void run(String... args) {
        if (repositoriesIsExist())
            return;

        List<User> users = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String password = "test" + i + "test";

            User newUser = User.builder()
                    .name("test" + i)
                    .dateOfBirth(new Date(ThreadLocalRandom.current().nextInt(100000, 1000000000)))
                    .password(JwtUtils.passwordEncoder.encode(password))
                    .build();

            Account newAccount = Account.builder()
                    .balance(new BigDecimal(ThreadLocalRandom.current().nextInt(0, 10000)))
                    .user(newUser)
                    .build();
            accounts.add(newAccount);
            newUser.setAccount(newAccount);

            for (int j = 0; j < 2; j++) {
                Email newEmail = Email.builder()
                        .email("test" + ThreadLocalRandom.current().nextInt(0, 1000000))
                        .user(newUser)
                        .build();

                StringBuilder phoneNumber = new StringBuilder("7967" +
                        ThreadLocalRandom.current().nextInt(0, 1000000)
                );
                while (phoneNumber.length() < 11) {
                    phoneNumber.append("0");
                }

                Phone newPhone = Phone.builder()
                        .phone(String.valueOf(phoneNumber))
                        .user(newUser)
                        .build();

                emails.add(newEmail);
                newUser.getEmails().add(newEmail);
                if (!phones.contains(newPhone)) {
                    phones.add(newPhone);
                    newUser.getPhones().add(newPhone);
                }
            }
            users.add(newUser);
        }

        userRepository.saveAll(users);
        accountRepository.saveAll(accounts);

        balanceStatService.saveBalancesByAccounts(accountRepository.findAll());

        emailRepository.saveAll(emails);
        phoneRepository.saveAll(phones);
    }

    private boolean repositoriesIsExist() {
        return userRepository.count() > 0
                && accountRepository.count() > 0
                && emailRepository.count() > 0
                && phoneRepository.count() > 0;
    }
}
