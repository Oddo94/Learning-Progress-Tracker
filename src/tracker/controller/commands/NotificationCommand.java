package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class NotificationCommand implements Command {
    private Receiver receiver;

    public NotificationCommand(Receiver receiver) {
        this.receiver = receiver;
    }


    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        int executionResult = receiver.displayNotifications(userRepository);

        return executionResult;
    }
}
