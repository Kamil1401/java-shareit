package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;


public class UserDaoImpl implements UserDao {
    private final Map<Long, User> users = new HashMap<>();


    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(getNextId());
            users.put(user.getId(), user);

            return user;
        }
        User storedUser = users.get(user.getId());

        if (storedUser == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        storedUser.setId(user.getId());
        storedUser.setName(user.getName());
        storedUser.setEmail(user.getEmail());

        users.put(storedUser.getId(), storedUser);

        return storedUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(Long id) {
        User user = users.get(id);
        if (user != null) {
            users.remove(id);
        }
    }


    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}