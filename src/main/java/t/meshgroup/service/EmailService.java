package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import t.meshgroup.database.custom.UserRepositoryCustomImpl;
import t.meshgroup.database.repo.EmailRepository;
import t.meshgroup.exceptions.ExceptionLevel;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.Email;
import t.meshgroup.models.Phone;
import t.meshgroup.models.User;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final UserService userService;
    private final UserRepositoryCustomImpl userRepositoryCustom;

    public void changeEmail(String deprecatedEmail, String newEmail) throws IncorrectDataByClassException { //todo интерфейс
        long currentUserId = userService.getCurrentUser().getId();

        checkEmailForUpdateAndDelete(deprecatedEmail, currentUserId);

        int changedFields = emailRepository.changeEmail(currentUserId, deprecatedEmail, newEmail);

        if (changedFields < 1)
            throw new IncorrectDataByClassException(Email.class, "Error with data", HttpStatus.UNPROCESSABLE_ENTITY, ExceptionLevel.CRITICAL);

        userRepositoryCustom.deleteFromCache(currentUserId);
    }

    public void deleteEmail(String email) throws IncorrectDataByClassException {
        long currentUserId = userService.getCurrentUser().getId();
        checkEmailForUpdateAndDelete(email, currentUserId);

        if (emailRepository.countByUserId(currentUserId) <= 1)
            throw new IncorrectDataByClassException(Phone.class, "User need have 1 email is required!", HttpStatus.CONFLICT);

        emailRepository.deleteEmailByEmailAndUserId(email, currentUserId);
        userRepositoryCustom.deleteFromCache(currentUserId);
    }

    public void addEmail(String email) throws IncorrectDataByClassException {
        User currentUser = userService.getCurrentUser();

        if (emailRepository.existsByEmail(email))
            throw new IncorrectDataByClassException(Phone.class, "Email already registered", HttpStatus.CONFLICT);

        emailRepository.save(
                Email.builder()
                        .user(currentUser)
                        .email(email)
                        .build()
        );

        userRepositoryCustom.deleteFromCache(currentUser.getId());
    }

    private void checkEmailForUpdateAndDelete(String email, Long userId) throws IncorrectDataByClassException {
        if (emailRepository.existsByEmailAndUserId(email, userId))
            return;
        throw new IncorrectDataByClassException(Phone.class, "Incorrect email - " + email, HttpStatus.NOT_FOUND);
    }
}
