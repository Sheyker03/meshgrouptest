package t.meshgroup.database.custom;

import org.springframework.http.HttpStatus;
import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In future use Cacheable from Spring + add cache for everyone email and phone by dateOfBirthRequest
 */
@SuppressWarnings("unused")
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final HashMap<String, User> uniqueCache = new HashMap<>();

    /**
     * cacheable annotation is not working
     */
    @Override
    public List<User> findUsers(String dateOfBirth, String phone, String email, String name, Integer limit, Integer offset) throws IncorrectDataByClassException {
        if (phone != null && uniqueCache.containsKey(phone))
            return List.of(uniqueCache.get(phone));

        if (email != null && uniqueCache.containsKey(email))
            return List.of(uniqueCache.get(email));

        StringBuilder s = new StringBuilder();

        if (phone != null) {
            s.append(" and (")
                    .append(String.format("(select phone from phones p where p.phone = '%s' and p.user_id = u.id) is not null", phone))
                    .append(")");
        }


        if (email != null) {
            s.append(" and (")
                    .append(String.format("(select email from emails e where e.email = '%s' and e.user_id = u.id) is not null", email))
                    .append(")");
        }

        /*
         * mb need some more interest cache
         * */
        if (dateOfBirth != null) {
            s.append(" and (")
                    .append(String.format("u.date_of_birth >= to_date('%s', 'DD.MM.YYYY')", dateOfBirth))
                    .append(")");
        }

        if (name != null)
            s.append(" and (")
                    .append(String.format("(u.name ~~ '%s", name) + "%') is not null")
                    .append(")");


        List<User> resulSet = new ArrayList<>();
        try {
            resulSet.addAll(entityManager.createNativeQuery("select * from users u where 1 = 1" + s + String.format(" limit %d offset %d", limit, offset), User.class).getResultList());
        } catch (RuntimeException e) {
            throw new IncorrectDataByClassException(User.class, e.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (email != null)
            uniqueCache.put(email, resulSet.stream().findFirst().get());

        if (phone != null)
            uniqueCache.put(phone, resulSet.stream().findFirst().get());

        return resulSet;
    }

    public void deleteFromCache(Long userId) {
        Optional<User> user = uniqueCache.values().stream().filter(it -> it.getId() == userId).collect(Collectors.toList()).stream().findFirst();
        user.ifPresent(value -> uniqueCache.values().remove(value));
    }
}
