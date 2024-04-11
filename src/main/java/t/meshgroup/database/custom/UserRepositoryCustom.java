package t.meshgroup.database.custom;

import t.meshgroup.exceptions.IncorrectDataByClassException;
import t.meshgroup.models.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findUsers(String dateOfBirth, String phone, String email, String name, Integer limit, Integer offset) throws IncorrectDataByClassException;
}
