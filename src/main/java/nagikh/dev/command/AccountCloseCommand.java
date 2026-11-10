package nagikh.dev.command;

import nagikh.dev.account.AccountService;
import nagikh.dev.console.ConsoleOperationType;
import nagikh.dev.console.ConsoleScanner;
import nagikh.dev.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class AccountCloseCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final ConsoleScanner consoleInput;

    public AccountCloseCommand(AccountService accountService, UserService userService, ConsoleScanner consoleInput) {
        this.accountService = accountService;
        this.userService = userService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readInt("Enter account id to close:", "account id");
        var closedAccount = accountService.close(accountId);
        var user = userService.findById(closedAccount.getUserId());
        user.getAccountList().remove(closedAccount);
        System.out.println("Account " + accountId + " closed.");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
