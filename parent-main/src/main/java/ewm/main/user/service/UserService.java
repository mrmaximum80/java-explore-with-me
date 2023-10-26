package ewm.main.user.service;

import ewm.main.user.dto.NewUserRequest;
import ewm.main.user.dto.UserDto;
import ewm.main.user.model.User;

import java.util.List;

public interface UserService {

    User createUser(NewUserRequest newUser);

    void deleteUser(Long userId);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}
