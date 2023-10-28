package ewm.main.user.service;

import ewm.main.error.NotFoundException;
import ewm.main.user.dto.NewUserRequest;
import ewm.main.user.dto.UserDto;
import ewm.main.user.map.UserMapper;
import ewm.main.user.model.User;
import ewm.main.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(NewUserRequest newUser) {
        log.info("Saving user with email {}", newUser.getEmail());
        return userRepository.save(UserMapper.toUser(newUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            log.info("User with id={} deleted", userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("User with id={} was not found", userId);
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids != null) {
            log.info("Getting users with ids param...");
            return userRepository.findAllByIdIn(ids)
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        } else {
            Long start = Long.valueOf(from);
            Long end = (long) (from + size);
            log.info("Getting users without ids param...");
            return userRepository.findByIdBetween(start, end)
                    .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
    }
}
