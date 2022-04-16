package tracker.controller.commands;

import tracker.repository.UserRepository;

import java.util.Scanner;

public interface Command {

    public int execute(Scanner scanner, UserRepository userRepository);
}
