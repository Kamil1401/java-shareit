package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public UserDto createUser(UserDto dto) {
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());

        if (userOpt.isPresent()) {
            throw new DuplicateException("Пользователь с таким Email уже существует");
        }
        User user = UserMapper.toEntity(dto);
        userRepository.save(user);

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto dto) {
        User user = getUserById(userId);

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            Optional<User> userWithSameEmail = userRepository.findByEmail(dto.getEmail());

            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(userId)) {
                throw new DuplicateException("Пользователь с таким Email уже существует");
            }
            user.setEmail(dto.getEmail());
        }
        userRepository.save(user);

        return UserMapper.toDto(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDto getAboutUser(Long id) {
        User user = getUserById(id);

        return UserMapper.toDto(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        List<Item> items = itemRepository.findByOwner_Id(id);
        items.forEach(item -> itemRepository.deleteById(item.getId()));
        userRepository.deleteById(id);
    }
}