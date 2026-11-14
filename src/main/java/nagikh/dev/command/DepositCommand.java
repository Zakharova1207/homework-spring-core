package nagikh.dev.command;

import nagikh.dev.account.AccountService;
import nagikh.dev.console.ConsoleOperationType;
import nagikh.dev.console.ConsoleScanner;
import org.springframework.stereotype.Component;

@Component
public class DepositCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleScanner consoleInput;

    public DepositCommand(AccountService accountService, ConsoleScanner consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int accountId = consoleInput.readInt("Enter account id:", "account id");
        int amount = consoleInput.readInt("Enter amount:", "amount");

        accountService.deposit(accountId, amount);
        System.out.println("Deposited " + amount + " to account " + accountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
