package nagikh.dev.command;

import nagikh.dev.account.Account;
import nagikh.dev.account.AccountService;
import nagikh.dev.console.ConsoleOperationType;
import nagikh.dev.console.ConsoleScanner;
import nagikh.dev.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class AccountCreateCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final ConsoleScanner consoleInput;

    public AccountCreateCommand(AccountService accountService, UserService userService, ConsoleScanner consoleInput) {
        this.accountService = accountService;
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int userId = consoleInput.readInt("Enter user id:", "user id");
        var user = userService.findById(userId);
        Account account = accountService.create(user);
        user.getAccountList().add(account);
        System.out.println("Account created: " + account);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}