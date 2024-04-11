package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import t.meshgroup.database.custom.UserRepositoryCustomImpl;
import t.meshgroup.database.repo.PhoneRepository;
import t.meshgroup.exceptions.ExceptionLevel;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.Phone;
import t.meshgroup.models.User;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final UserService userService;

    private final UserRepositoryCustomImpl userRepositoryCustom;

    public void changePhone(String deprecatedPhone, String newPhone) throws IncorrectDataByClassException {
        long currentUserId = userService.getCurrentUser().getId();

        checkPhoneForUpdateAndDelete(deprecatedPhone, currentUserId);

        int changedFields = phoneRepository.changePhone(currentUserId, deprecatedPhone, newPhone);

        if (changedFields < 1)
            throw new IncorrectDataByClassException(Phone.class, "Error with data", HttpStatus.UNPROCESSABLE_ENTITY, ExceptionLevel.CRITICAL);

        userRepositoryCustom.deleteFromCache(currentUserId);
    }

    public void deletePhone(String phone) throws IncorrectDataByClassException {
        long currentUserId = userService.getCurrentUser().getId();
        checkPhoneForUpdateAndDelete(phone, currentUserId);

        if (phoneRepository.countByUserId(currentUserId) <= 1)
            throw new IncorrectDataByClassException(Phone.class, "User need have 1 phone is required!", HttpStatus.CONFLICT);

        phoneRepository.deletePhoneByPhoneAndUserId(phone, currentUserId);
        userRepositoryCustom.deleteFromCache(currentUserId);
    }

    public void addPhone(String phone) throws IncorrectDataByClassException {
        User currentUser = userService.getCurrentUser();

        if (phoneRepository.existsByPhone(phone))
            throw new IncorrectDataByClassException(Phone.class, "Phone already registered", HttpStatus.CONFLICT);

        phoneRepository.save(
                Phone.builder()
                        .user(currentUser)
                        .phone(phone)
                        .build()
        );
        userRepositoryCustom.deleteFromCache(currentUser.getId());
    }

    private void checkPhoneForUpdateAndDelete(String phone, Long userId) throws IncorrectDataByClassException {
        if (phoneRepository.existsByPhoneAndUserId(phone, userId))
            return;
        throw new IncorrectDataByClassException(Phone.class, "Incorrect phone - " + phone, HttpStatus.NOT_FOUND);
    }
}
