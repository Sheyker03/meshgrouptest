package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t.meshgroup.database.repo.AccountRepository;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.User;

import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    @Transactional
    public void transferMoney(Long toUser, double money) throws IncorrectDataByClassException {
        if (money < 0)
            throw new IncorrectDataByClassException(Double.class, "Money cannot be less 0", HttpStatus.CONFLICT);

        User fromUser = userService.getCurrentUser();
        Long fromAccountId = fromUser.getAccount().getId();
        Long toAccountId = userService.getUserById(toUser).getAccount().getId();

        try {
            accountRepository.minusMoney(fromAccountId, BigDecimal.valueOf(money));
            accountRepository.addMoney(toAccountId, BigDecimal.valueOf(money));
            log.info(String.format("User %d transfer %1.2f money to user %d", fromAccountId, money, toAccountId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }
}
