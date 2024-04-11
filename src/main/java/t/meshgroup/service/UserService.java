package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import t.meshgroup.database.repo.UserRepository;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.User;

import javax.annotation.Nullable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsersCommon(
            @Nullable String dateOfBirth,
            @Nullable String phone,
            @Nullable String email,
            @Nullable String name,
            Integer limit,
            Integer offset
    ) throws IncorrectDataByClassException {
        if (name == null && email == null && phone == null && dateOfBirth == null)
            throw new IncorrectDataByClassException(User.class, "For search need some more 0 params", HttpStatus.UNPROCESSABLE_ENTITY);


        return userRepository.findUsers(dateOfBirth, phone, email, name, limit, offset);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found!"));
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    /**
     * --------------------------------------------------------
     * Just for MVP and fast - spring is needed/
     */
    public User getByUsername(String userId) {
        return getUserById(Long.valueOf(userId));
    }


    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * --------------------------------------------------------
     */

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserById(Long.valueOf(username));
    }
}
