package t.meshgroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.service.AccountService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Account controller")
@Validated
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Transfer money")
    @PostMapping("/transfer-money")
    public ResponseEntity<String> transferMoney(
            @RequestParam Long toUser,
            @RequestParam @Positive(message = "Money for transfer need be positive") Double money
    ) throws IncorrectDataByClassException {
        accountService.transferMoney(toUser, money);
        return new ResponseEntity<>(
                "You transfer money to user - " + toUser,
                HttpStatus.OK
        );
    }
}
