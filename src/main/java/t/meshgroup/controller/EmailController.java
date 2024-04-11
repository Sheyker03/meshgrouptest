package t.meshgroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.service.EmailService;

import javax.validation.constraints.Size;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
@Tag(name = "Email controller")
@Validated
public class EmailController {
    private final EmailService emailService;

    @Operation(summary = "Change email")
    @PostMapping("/change")
    public ResponseEntity<String> changeEmail(
            @RequestParam String deprecateEmail,
            @RequestParam @Size(min = 4, message = "Email need be more 4 long") String newEmail
    ) throws IncorrectDataByClassException {
        emailService.changeEmail(deprecateEmail, newEmail);
        return new ResponseEntity<>(
                String.format("Email changed - %s.", newEmail),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Add email")
    @PutMapping("/add")
    ResponseEntity<String> addEmail(
            @RequestParam @Size(min = 4, message = "Email need be more 4 long") String email
    ) throws IncorrectDataByClassException {
        emailService.addEmail(email);
        return new ResponseEntity<>(
                String.format("Email added - %s.", email),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Delete email")
    @DeleteMapping("/delete")
    ResponseEntity<String> deleteEmail(
            @RequestParam @Size(min = 4, message = "Email cannot be less 4 long") String email
    ) throws IncorrectDataByClassException {
        emailService.deleteEmail(email);
        return new ResponseEntity<>(
                String.format("Email deleted - %s.", email),
                HttpStatus.OK
        );
    }

    @Deprecated
    @Operation(summary = "Deprecated test auth")
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Emails";
    }
}
