package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class SetupTestDataCommand implements Command {
    private Receiver receiver;

    public SetupTestDataCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        int executionResult = receiver.setupTestData(scanner,userRepository);

        return executionResult;
    }
}
