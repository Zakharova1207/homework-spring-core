package nagikh.dev.console;

import java.util.Arrays;

public enum ConsoleOperationType {
    USER_CREATE,
    SHOW_ALL_USERS,
    ACCOUNT_CREATE,
    ACCOUNT_DEPOSIT,
    ACCOUNT_WITHDRAW,
    ACCOUNT_TRANSFER,
    ACCOUNT_CLOSE,
    EXIT;

    public static boolean contains(String input) {
        return Arrays.stream(ConsoleOperationType.values())
                .anyMatch(status -> status.name().equals(input));
    }
}
