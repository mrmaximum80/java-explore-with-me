package ewm.main.user.map;

import ewm.main.user.dto.NewUserRequest;
import ewm.main.user.dto.UserDto;
import ewm.main.user.dto.UserShortDto;
import ewm.main.user.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User toUser(NewUserRequest newUser) {
        return new User(newUser.getEmail(), newUser.getName());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
