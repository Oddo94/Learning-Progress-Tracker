package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class ListCommand implements Command {
    private Receiver receiver;

    public ListCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        int executionResult = receiver.displayStudents(scanner, userRepository);

        return executionResult;
    }
}
