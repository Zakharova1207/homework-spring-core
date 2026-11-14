package nagikh.dev.console;

import nagikh.dev.command.OperationCommand;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConsoleListener {
    private final Map<ConsoleOperationType, OperationCommand> commandByType;
    private final ConsoleScanner scanner;

    public ConsoleListener(ConsoleScanner scanner, List<OperationCommand> operationCommandList) {
        this.scanner = scanner;
        this.commandByType = operationCommandList.stream().collect(Collectors.toMap(OperationCommand::getOperationType, Function.identity()));
    }

    public void run() {
        scanner.init();
        try {
            while (true) {
                var nextOperation = scanner.readCommand();
                if (commandByType.get(nextOperation) == null) {
                    throw new IllegalStateException("No command handler for " + nextOperation);
                }
                commandByType.get(nextOperation).execute();
                if (nextOperation == ConsoleOperationType.EXIT) {
                    break;
                }
            }
        } catch (EndOfInputException e) {
            System.out.println("MiniBank stopped.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            System.out.println("Error: " + message);
        }
    }
}
