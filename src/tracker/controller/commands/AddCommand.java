package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class AddCommand implements Command {
    private Receiver receiver;

    public AddCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {

        int executionResult = receiver.addStudent(scanner, userRepository);

        return executionResult;
    }
}
