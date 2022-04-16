package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class BackCommand implements Command {
    private Receiver receiver;

    public BackCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {

        int executionResult = receiver.getBack(userRepository);
        return executionResult;
    }
}
