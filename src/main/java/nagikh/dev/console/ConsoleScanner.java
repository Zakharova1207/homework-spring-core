package nagikh.dev.console;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Scanner;

import static nagikh.dev.console.ConsoleOperationType.contains;

@Component
public class ConsoleScanner {
    private final Scanner scanner;

    public ConsoleScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readRequiredString(String text, String fieldName) {
        while (true) {
            System.out.println(text);
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("Error: " + fieldName + " must not be blank");
        }
    }

    public Integer readInt(String text, String fieldName) {
        while (true) {
            System.out.println(text);
            String value = scanner.nextLine().trim();
            if (value.isBlank()) {
                System.out.println("Error: " + fieldName + " must not be blank");
                continue;
            }
            int parsed = Integer.parseInt(value);
            if (parsed <= 0) {
                System.out.println("Error: " + fieldName + " must be > 0");
                continue;
            }
            return parsed;
        }
    }

    public ConsoleOperationType readCommand() {
        while (true) {
            if (!scanner.hasNextLine()) {
                throw new EndOfInputException();
            }

            String input = scanner.nextLine();

            if (!contains(input)) {
                System.out.println("Please enter valid command");
                continue;
            }
            try {
                return ConsoleOperationType.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: unknown command");
            }
        }

    }

    public void init() {
        System.out.println("""
                MiniBank started. Type EXIT to stop.
                Available commands: USER_CREATE, SHOW_ALL_USERS, ACCOUNT_CREATE,\s
                ACCOUNT_DEPOSIT, ACCOUNT_WITHDRAW, ACCOUNT_TRANSFER, ACCOUNT_CLOSE, EXIT\s
                Enter command:""");
    }
}
