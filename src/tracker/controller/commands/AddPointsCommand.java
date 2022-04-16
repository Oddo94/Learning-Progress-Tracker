package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public class AddPointsCommand implements Command {
    private Receiver receiver;

    public AddPointsCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        int executionResult;

        System.out.println("Enter an id and points or 'back' to return");
        while(true) {
            executionResult =  receiver.addPoints(scanner, userRepository);

            if(executionResult == -1) {
                break;
            }
        }

        return executionResult;
    }
}
