package tracker.controller.commands;

import tracker.controller.UIManager;
import tracker.repository.UserRepository;
import tracker.utils.enums.Course;

import java.util.Scanner;

public class StatisticsCommand implements Command {
    private Receiver receiver;

    public StatisticsCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public int execute(Scanner scanner, UserRepository userRepository) {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        int executionResult = receiver.displayGeneralStatistics(userRepository);

        while(true) {
            String userInput = scanner.nextLine().replaceAll("> ", "").trim();
            if(UIManager.BACK_COMMAND.equals(userInput)) {
                break;
            } else {
                String processedUserInput = userInput.toLowerCase();

                switch(processedUserInput) {
                    case "java":
                        receiver.displayStatisticsPerCourse(userRepository, Course.JAVA);
                        break;

                    case "dsa":
                        receiver.displayStatisticsPerCourse(userRepository, Course.DSA);
                        break;

                    case "databases":
                        receiver.displayStatisticsPerCourse(userRepository, Course.DATABASES);
                        break;

                    case "spring":
                        receiver.displayStatisticsPerCourse(userRepository, Course.SPRING);
                        break;

                    default:
                        System.out.println("Unknown course.");
                }
            }

        }
        return executionResult;
    }
}
