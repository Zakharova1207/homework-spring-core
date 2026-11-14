package nagikh.dev.command;

import nagikh.dev.account.AccountService;
import nagikh.dev.console.ConsoleOperationType;
import nagikh.dev.console.ConsoleScanner;
import org.springframework.stereotype.Component;

@Component
public class AccountTransferCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleScanner consoleInput;

    public AccountTransferCommand(AccountService accountService, ConsoleScanner consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        int fromAccountId = consoleInput.readInt("Enter source account id:", "source account id");
        int toAccountId = consoleInput.readInt("Enter target account id:", "target account id");
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }
        int amount = consoleInput.readInt("Enter amount:", "amount");
        accountService.transfer(fromAccountId, toAccountId, amount);
        System.out.println("Transfer completed from account " + fromAccountId + " to account " + toAccountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}