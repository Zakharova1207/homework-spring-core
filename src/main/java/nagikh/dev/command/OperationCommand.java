package nagikh.dev.command;

import nagikh.dev.console.ConsoleOperationType;

public interface OperationCommand {
    void execute();

    ConsoleOperationType getOperationType();
}
