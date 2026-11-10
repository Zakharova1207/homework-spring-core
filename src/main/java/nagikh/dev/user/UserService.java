package nagikh.dev.user;

import nagikh.dev.account.AccountService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {
    private final AccountService accountService;
    private int idCounter = 0;
    private final Map<Integer, User> userById;
    private final Set<String> logins;

    public UserService(AccountService accountService) {
        this.logins = new HashSet<>();
        this.userById = new HashMap<>();
        this.accountService = accountService;
    }

    public User create(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be blank");
        }
        if (logins.contains(login)) {
            throw new IllegalArgumentException("User already exists with login=%s".formatted(login));
        }
        idCounter++;
        User newUser = new User(idCounter, login, new ArrayList<>());
        newUser.getAccountList().add(accountService.create(newUser));

        userById.put(idCounter, newUser);
        logins.add(login);
        return userById.get(idCounter);
    }

    public User findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("user id must be > 0");
        }
        User user = userById.get(id);
        if (user == null) {
            throw new IllegalArgumentException("No such user with id=%s".formatted(id));
        }
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(userById.values());
    }
}
