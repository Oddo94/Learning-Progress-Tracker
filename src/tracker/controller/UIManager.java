package tracker.controller;
import tracker.controller.commands.*;
import tracker.model.GradeBook;
import tracker.model.Student;
import tracker.repository.UserRepository;
import tracker.utils.UUIDGenerator;
import tracker.utils.UserInputChecker;

import java.util.*;

public class UIManager {

    private static final String ADD_STUDENT_COMMAND = "add students";
    private static final String ADD_POINTS_COMMAND = "add points";
    public static final String BACK_COMMAND = "back";
    private static final String EXIT_COMMAND = "exit";
    private static final String LIST_COMMAND =  "list";
    private static final String FIND_COMMAND = "find";
    private static final String STATISTICS = "statistics";
    private static final String NOTIFY = "notify";
    private static final String SETUP_TEST_DATA_COMMAND = "setup test";
    private static final String NO_COMMAND = "";

    private Scanner scanner;
    private UserRepository userRepository;
    public static String receiverUserInput;
    public static boolean hasNotifiedStudents;
    public boolean hasTriedToAddStudent;
    public boolean hasTriedToUpdateStudent;




    public UIManager(Scanner inputScanner) {
        this.scanner = inputScanner;
        this.userRepository = new UserRepository();
    }

    //ONLY FOR TESTING PURPOSES!!
    public boolean getHasTriedToAddStudent() {
        return this.hasTriedToAddStudent;
    }

    public boolean getHasTriedToUpdateStudent() {
        return this.hasTriedToUpdateStudent;
    }

    //ONLY FOR TESTING PURPOSES!!
    public void setHasTriedToAddStudent(boolean value) {
        this.hasTriedToAddStudent = value;
    }

    public void setHasTriedToUpdateStudent(boolean value) {
        this.hasTriedToUpdateStudent = value;
    }
    public void manageUI() {
        Invoker commandInvoker = new Invoker();
        Receiver commandReceiver = new Receiver(this);

        System.out.println("Learning Progress Tracker");

        String currentInput;
        String userInput = scanner.nextLine().replaceAll("> ", "");
        currentInput = userInput.trim();

        while(!currentInput.equalsIgnoreCase(EXIT_COMMAND)) {
            switch (currentInput) {
                case ADD_STUDENT_COMMAND:
                    AddCommand addCommand = new AddCommand(commandReceiver);
                    commandInvoker.setCommand(addCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case ADD_POINTS_COMMAND:
                    Command addPointsCommand = new AddPointsCommand(commandReceiver);
                    commandInvoker.setCommand(addPointsCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case BACK_COMMAND:
                    Command backCommand = new BackCommand(commandReceiver);
                    commandInvoker.setCommand(backCommand);
                    int backCommandExecutionResult = commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case LIST_COMMAND:
                    Command displayStudents= new ListCommand(commandReceiver);
                    commandInvoker.setCommand(displayStudents);

                    int listCommandExecutionResult = commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case FIND_COMMAND:
                    Command findCommand = new FindCommand(commandReceiver);
                    commandInvoker.setCommand(findCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case STATISTICS:
                    Command statisticsCommand = new StatisticsCommand(commandReceiver);
                    commandInvoker.setCommand(statisticsCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case NOTIFY:
                    Command notificationCommand = new NotificationCommand(commandReceiver);
                    commandInvoker.setCommand(notificationCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case SETUP_TEST_DATA_COMMAND:
                    Command setupTestDataCommand = new SetupTestDataCommand(commandReceiver);
                    commandInvoker.setCommand(setupTestDataCommand);
                    commandInvoker.executeCommand(this.scanner, this.userRepository);
                    break;

                case NO_COMMAND:
                    System.out.println("No input.");
                    break;

                default:
                    System.out.println("Error: unknown command!");
            }

            //If the add students was the last command the program will not wait for new input and will instead use the value set to userInput static variable by the addStudent method of the Receiver class
            if(BACK_COMMAND.equals(receiverUserInput)) {
                currentInput = receiverUserInput;
                receiverUserInput = "";
            } else {
                userInput = scanner.nextLine().replaceAll("> ", "");
                currentInput = userInput.trim();
            }
        }

        System.out.println("Bye!");

    }

    private int addStudent(String studentCredentials, UserRepository userRepository) {
        String[] processedCredentials = processStudentCredentials(studentCredentials);

        if(studentCredentials == null || processedCredentials.length < 3) {
            System.out.println("Incorrect credentials.");
            return -1;
        }

        String[] sortedCredentials = sortCredentials(processedCredentials);
        String firstName = sortedCredentials[0];
        String lastName = sortedCredentials[1];
        String email = sortedCredentials[2];

        boolean isValidLastName = UserInputChecker.isValidName(lastName);

        if (!UserInputChecker.isValidName(firstName)) {
            System.out.println("Incorrect first name.");
            return -1;
        }

        if (!UserInputChecker.isValidName(lastName)) {
            System.out.println("Incorrect last name.");
            return -1;
        }

        if (!UserInputChecker.isValidEmail(email))  {
            System.out.println("Incorrect email.");
            return -1;
        }

        if(userRepository == null) {
            return -1;
        }

        Student student = new Student(firstName, lastName, email, new GradeBook());
        String uniqueIdentifier = UUIDGenerator.generateUUID();

        userRepository.addStudent(uniqueIdentifier, student);

        return 0;
    }

    private void displayAddedStudents() {
        Map<String, Student> studentMap = userRepository.getStudentList();

        if (studentMap.size() == 0) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("Students:");
        for(Map.Entry<String,Student> currentStudent : studentMap.entrySet()) {
            System.out.println(currentStudent.getKey());
        }
    }

    private int addPoints() {
        System.out.println("Enter and id and points or 'back' to return");
        //String input = scanner.nextLine().replaceAll("> ", "").trim();
        String[] input = scanner.nextLine().replaceAll("> ", "").split("\\s");

        if (input.length > 0 && "back".equals(input[0])) {
            return -1;
        }

        int numberOfRequiredElements = 5;
        if (input.length == numberOfRequiredElements) {
            String studentUUID = input[0];

            if (!UserInputChecker.existsStudent(studentUUID, userRepository.getStudentList())) {
                System.out.println(String.format("No student is found for id=%s", studentUUID));
                return -1;
            }

            int[] inputPoints = Arrays.stream(input).skip(1).mapToInt(Integer::parseInt).toArray();
            int javaPoints = inputPoints[0];
            int dataStructuresPoints = inputPoints[1];
            int databasesPoints = inputPoints[2];
            int springPoints = inputPoints[3];
            if (javaPoints < 0 || dataStructuresPoints < 0 || databasesPoints < 0 || springPoints < 0) {
                System.out.println("Incorrect points format");
                return -1;
            }

            //Creates a grade book object containing the points for each subject and then updates the corresponding student object from the map
            GradeBook gradeBook = new GradeBook(javaPoints, dataStructuresPoints, databasesPoints, springPoints);
            userRepository.getStudentList().get(studentUUID).setGradeBook(gradeBook);

            System.out.println("Points updated");
            return 0;


        } else {
            System.out.println("Incorrect points format");
            return -1;
        }

    }

    private String[] processStudentCredentials(String input) {
        if (input == null) {
            return null;
        }

        String[] studentCredentials = input.split("\\s+");

        return studentCredentials;
    }

    private boolean validateCredentials(String[] studentCredentials) {
        if(studentCredentials == null | studentCredentials.length < 3) {
            return false;
        }

        boolean isValidFirstName = UserInputChecker.isValidName(studentCredentials[0]);
        boolean isValidLastName = UserInputChecker.isValidName(studentCredentials[1]);
        boolean isValidEmail = UserInputChecker.isValidEmail(studentCredentials[2]);

        return isValidFirstName && isValidLastName && isValidEmail;

    }

    private String[] sortCredentials(String[] processedCredentials) {
        if(processedCredentials == null) {
            return null;
        }

        if (processedCredentials.length == 3) {
            return processedCredentials;
        }

        String firstName = processedCredentials[0];

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < processedCredentials.length - 1; i++) {
            sb.append(processedCredentials[i] + " ");
        }

        String lastName = sb.toString().trim();
        String email = processedCredentials[processedCredentials.length - 1];

        String[] sortedCredentials = new String[]{firstName, lastName, email};

        return sortedCredentials;

    }

}
