package nagikh.dev.account;

import nagikh.dev.config.AccountProperties;
import nagikh.dev.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Component
public class AccountService {
    private int idCounter = 0;
    private final Map<Integer, Account> accountById;
    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.accountById = new HashMap<>();
        this.accountProperties = accountProperties;
    }

    public Account create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        idCounter++;
        accountById.put(idCounter, new Account(idCounter, user.getId(), accountProperties.getDefaultAmount()));
        return accountById.get(idCounter);
    }

    public Account close(int id) {
        validatePositiveId(id);
        Account account = ofNullable(accountById.get(id))
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(id)));

        var accountToTransferMoney = accountById.values().stream()
                .filter(it -> it.getId() != account.getId()).findFirst().orElseThrow(() -> new IllegalStateException("Can't close the only one account"));
        var newAmount = Math.addExact(accountToTransferMoney.getMoneyAmount(), account.getMoneyAmount());
        accountToTransferMoney.setMoneyAmount(newAmount);
        accountById.remove(id);
        return account;
    }

    public void deposit(int id, int amount) {
        validatePositiveId(id);
        validatePositiveAmount(amount);
        Account account = ofNullable(accountById.get(id))
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(id)));

        account.setMoneyAmount(accountById.get(id).getMoneyAmount() + amount);
    }

    public void withdraw(int id, int amount) {
        validatePositiveId(id);
        validatePositiveAmount(amount);
        Account account = ofNullable(accountById.get(id))
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(id)));

        if (amount > account.getMoneyAmount()) {
            throw new IllegalArgumentException(
                    "insufficient funds on account id=%s, moneyAmount=%s, attempted withdraw=%s"
                            .formatted(account.getId(), account.getMoneyAmount(), amount)
            );
        }
        account.setMoneyAmount(account.getMoneyAmount() - amount);
    }

    public void transfer(int fromAccountId, int toAccountId, int amount) {
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }
        Account accountFrom = ofNullable(accountById.get(fromAccountId))
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));
        Account accountTo = ofNullable(accountById.get(toAccountId))
                .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));

        if (amount > accountFrom.getMoneyAmount()) {
            throw new IllegalArgumentException(
                    "insufficient funds on account id=%s, moneyAmount=%s, attempted transfer=%s"
                            .formatted(accountFrom.getId(), accountFrom.getMoneyAmount(), amount)
            );
        }
        int amountToTransfer = accountTo.getUserId() == accountFrom.getUserId()
                ? amount
                : (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));
        int sourceAfter = Math.subtractExact(accountFrom.getMoneyAmount(), amount);
        int targetAfter = Math.addExact(accountTo.getMoneyAmount(), amountToTransfer);
        accountFrom.setMoneyAmount(sourceAfter);
        accountTo.setMoneyAmount(targetAfter);
    }

    private void validatePositiveId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("account id" + " must be > 0");
        }
    }

    private void validatePositiveAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
    }
}
