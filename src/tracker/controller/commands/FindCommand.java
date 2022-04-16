package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class FindCommand implements Command {
    private Receiver receiver;

    public FindCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        int executionResult = receiver.findStudent(scanner, userRepository);

        return executionResult;
    }
}
