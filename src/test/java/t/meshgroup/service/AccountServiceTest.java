package t.meshgroup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import t.meshgroup.database.repo.AccountRepository;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @WithMockUser("1")
    void transferMoney() throws IncorrectDataByClassException {
        jdbcTemplate.execute("insert into users (id, date_of_birth, name, password) VALUES (1, '1970-01-03', 'test', 'test1test')");
        jdbcTemplate.execute("insert into users (id, date_of_birth, name, password) VALUES (2, '1970-01-03', 'test', 'test1test')");

        jdbcTemplate.execute("insert into accounts (balance, user_id) VALUES (100, 1)");
        jdbcTemplate.execute("insert into accounts (balance, user_id) VALUES (100, 2)");


        assertThrows(IncorrectDataByClassException.class, () -> accountService.transferMoney(2L, -110));
        accountService.transferMoney(2L, 100);

        Account acc1 = accountRepository.findById(1L).get();
        Account acc2 = accountRepository.findById(2L).get();

        assertEquals(0, acc1.getBalance().doubleValue());
        assertEquals(200, acc2.getBalance().doubleValue());
    }
}