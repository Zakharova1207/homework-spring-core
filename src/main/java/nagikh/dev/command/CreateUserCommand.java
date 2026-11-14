package nagikh.dev.command;

import nagikh.dev.console.ConsoleOperationType;
import nagikh.dev.console.ConsoleScanner;
import nagikh.dev.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommand implements OperationCommand {
    private final UserService userService;
    private final ConsoleScanner consoleInput;

    public CreateUserCommand(UserService userService, ConsoleScanner consoleInput) {
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        String login = consoleInput.readRequiredString("Enter login:", "login");
        var user = userService.create(login.trim());
        System.out.println("User created: " + user);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}