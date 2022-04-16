package tracker.controller.commands;

import tracker.controller.commands.Command;
import tracker.repository.UserRepository;

import java.util.Scanner;

public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public int executeCommand(Scanner scanner, UserRepository userRepository) {
        if (scanner == null) {
            return -1;
        }

       return this.command.execute(scanner, userRepository);
    }
}
