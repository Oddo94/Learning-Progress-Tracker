package tracker.controller.commands;

import tracker.controller.UIManager;
import tracker.model.GradeBook;
import tracker.model.Notification;
import tracker.model.Student;
import tracker.repository.UserRepository;
import tracker.utils.*;
import tracker.utils.enums.ActivityLevel;
import tracker.utils.enums.Course;
import tracker.utils.enums.DifficultyLevel;
import tracker.utils.enums.PopularityLevel;

import java.util.*;
import java.util.stream.Collectors;

public class Receiver {
    private UIManager userInterfaceManager;

    public Receiver(UIManager userInterfaceManager) {
        this.userInterfaceManager = userInterfaceManager;
    }

    public int addStudent(Scanner scanner, UserRepository userRepository) {
        System.out.println("Enter student credentials or 'back' to return.");
        userInterfaceManager.setHasTriedToAddStudent(true);


        String credentialsInput = scanner.nextLine().replaceAll("> ", "").trim();
        while(true) {
            if(UIManager.BACK_COMMAND.equals(credentialsInput)) {
                UIManager.receiverUserInput = credentialsInput;
                return -1;
            }

            int result = insertStudent(credentialsInput, userRepository);
            if(result == 0) {
                System.out.println("The student has been added.");
            }

            credentialsInput = scanner.nextLine().replaceAll("> ", "").trim();
        }
    }


    public int getBack(UserRepository userRepository) {
        if(userRepository == null) {
            return -1;
        }

        int studentListSize = userRepository.getStudentList().size();
        if(userInterfaceManager.getHasTriedToAddStudent()) {
            System.out.printf("Total %d students have been added.", studentListSize);
            System.out.println();
            userInterfaceManager.setHasTriedToAddStudent(false);
            return 0;
        } else if(userInterfaceManager.getHasTriedToUpdateStudent()) {
            userInterfaceManager.setHasTriedToUpdateStudent(false);
            return 0;
        } else {
            System.out.println("Enter 'exit' to exit the program.");
            return 0;
        }
    }

    public int addPoints(Scanner scanner, UserRepository userRepository) {
        String[] input = scanner.nextLine().replaceAll("> ", "").split("\\s");
        userInterfaceManager.setHasTriedToUpdateStudent(true);

        if (input.length > 0 && "back".equals(input[0])) {
            UIManager.receiverUserInput = input[0];
            return -1;
        }

        int numberOfRequiredElements = 5;
        String studentUUID;
        if (input.length == numberOfRequiredElements) {
            studentUUID = input[0];

            if (!UserInputChecker.existsStudent(studentUUID, userRepository.getStudentList())) {
                System.out.println(String.format("No student is found for id=%s", studentUUID));
                return 0;
            }

            if(UserInputChecker.containsIllegalCharacters(Arrays.copyOfRange(input,1,5))) {
                System.out.println("Incorrect points format");
                return 0;
            }

            int[] inputPoints = Arrays.stream(input).skip(1).mapToInt(Integer::parseInt).toArray();
            int javaPoints = inputPoints[0];
            int dataStructuresPoints = inputPoints[1];
            int databasesPoints = inputPoints[2];
            int springPoints = inputPoints[3];
            if (javaPoints < 0 || dataStructuresPoints < 0 || databasesPoints < 0 || springPoints < 0) {
                System.out.println("Incorrect points format");
                return 0;
            }

            //Updates the number of completed tasks for each course if the number of points is higher than 0
            updateCompletedTasksCount(javaPoints, dataStructuresPoints, databasesPoints, springPoints);

            Student student = getStudent(studentUUID, userRepository.getStudentList());
            GradeBook gradeBook = getUpdatedGradeBook(student.getGradeBook(), javaPoints, dataStructuresPoints, databasesPoints, springPoints);
            userRepository.getStudentList().get(studentUUID).setGradeBook(gradeBook);

            System.out.println("Points updated");
            return 0;


        } else {
            studentUUID = input[0];
            if (!UserInputChecker.existsStudent(studentUUID, userRepository.getStudentList())) {
                System.out.println(String.format("No student is found for id=%s", studentUUID));
                return 0;
            }

            System.out.println("Incorrect points format");
            return 0;
        }


    }

    public int displayStudents(Scanner scanner, UserRepository userRepository) {
        if (userRepository == null) {
            return -1;
        }

        Map<String, Student> studentMap = userRepository.getStudentList();

        if (studentMap.size() == 0) {
            System.out.println("No students found.");
            return 0;
        }

        System.out.println("Students:");
        userRepository.getStudentList().entrySet()
                .stream()
                .forEach(x -> System.out.println(x.getKey()));

        return 0;
    }

    public int findStudent(Scanner scanner, UserRepository userRepository) {
        if(scanner == null || userRepository == null) {
            return -1;
        }

        System.out.println("Enter an id or 'back' to return.");
        while(true) {
            String userInput = scanner.nextLine().replaceAll("> ", "").trim();

            if(UIManager.BACK_COMMAND.equals(userInput)) {
                UIManager.receiverUserInput = userInput;
                return -1;
            }

            Student student = getStudent(userInput, userRepository.getStudentList());

            if(student == null) {
                System.out.println(String.format("No student is found for id=%s.", userInput));
                continue;
            }

            int javaPoints = student.getGradeBook().getJavaPoints();
            int dataStructuresPoints = student.getGradeBook().getDataStructuresPoints();
            int databasesPoints = student.getGradeBook().getDatabasePoints();
            int springPoints = student.getGradeBook().getSpringPoints();

            System.out.println(String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d", userInput, javaPoints, dataStructuresPoints, databasesPoints, springPoints));
        }

    }

    private int insertStudent(String studentCredentials, UserRepository userRepository) {
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

        if (!UserInputChecker.isAvailableEmail(email, userRepository.getStudentList())) {
            System.out.println("This email is already taken.");
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

    private String[] processStudentCredentials(String input) {
        if (input == null) {
            return null;
        }

        String[] studentCredentials = input.split("\\s+");

        return studentCredentials;
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

    public Student getStudent(String uniqueIdentifier, Map<String, Student> userRepository) {
        if(uniqueIdentifier == null || userRepository == null) {
            return null;
        }

        for(Map.Entry<String, Student> currentStudent : userRepository.entrySet()) {
            if (uniqueIdentifier.equals(currentStudent.getKey())) {
                return currentStudent.getValue();
            }
        }

        return null;
    }

    public GradeBook getUpdatedGradeBook(GradeBook gradeBook, int javaPoints, int dataStructuresPoints, int databasesPoints, int springPoints) {
        if(gradeBook == null) {
            return null;
        }

        int updatedJavaPoints = gradeBook.getJavaPoints() + javaPoints;
        int updatedDataStructuresPoints = gradeBook.getDataStructuresPoints() + dataStructuresPoints;
        int updatedDatabasesPoints = gradeBook.getDatabasePoints() + databasesPoints;
        int updatedSpringPoints = gradeBook.getSpringPoints() + springPoints;

        return new GradeBook(updatedJavaPoints, updatedDataStructuresPoints, updatedDatabasesPoints, updatedSpringPoints);

    }

    public int displayGeneralStatistics(UserRepository userRepository) {
        if(userRepository == null) {
            return -1;
        }

        StatisticsManager statisticsManager = new StatisticsManager(userRepository);

        System.out.print("Most popular:");
        String topCourses = statisticsManager.getCourseBasedOnPopularity(PopularityLevel.MOST_POPULAR);
        System.out.println(topCourses);

        System.out.print("Least popular:");
        String leastPopular = statisticsManager.getCourseBasedOnPopularity(PopularityLevel.LEAST_POPULAR);
        System.out.println(leastPopular);


        Map<String, Integer> courseActivityMap = getCourseActivityStatistics(userRepository);
        int firstElementIndex = 0;
        int secondElementIndex = courseActivityMap.entrySet().size() - 1;

        System.out.print("Highest activity:");
        String highestActivityCourse = statisticsManager.getCourseBasedOnActivity(ActivityLevel.HIGHEST_ACTIVITY);
        System.out.println(highestActivityCourse);

        System.out.print("Lowest activity:");
        String lowestActivityCourse = statisticsManager.getCourseBasedOnActivity(ActivityLevel.LOWEST_ACTIVITY);
        System.out.println(lowestActivityCourse);

        System.out.print("Easiest course:");
        String easiestCourse = statisticsManager.getCourseBasedOnDifficulty(DifficultyLevel.EASIEST);
        System.out.println(easiestCourse);

        System.out.print("Hardest course:");
        String hardestCourse = statisticsManager.getCourseBasedOnDifficulty(DifficultyLevel.HARDEST);
        System.out.println(hardestCourse);

        return 0;
    }

    public int displayStatisticsPerCourse(UserRepository userRepository, Course course) {
        StatisticsManager statisticsManager = new StatisticsManager(userRepository);

        Map<String, Double> completionPercentagesMap = statisticsManager.getCompletionPercentage(course);
        Map<String, Student> studentMap = userRepository.getStudentList();

        System.out.println(course.getCourseName());
        System.out.println("id                                   points     completed");

        completionPercentagesMap.entrySet()
                .stream()
                .forEach(entry -> System.out.println(String.format("%s %d         %.1f%%", entry.getKey().toString(), statisticsManager.getTotalPointsForStudent(entry.getKey(), studentMap, course), entry.getValue().doubleValue())));

        return 0;
    }

    public int displayNotifications(UserRepository userRepository) {
        if(userRepository == null) {
            return -1;
        }

        NotificationManager notificationManger = new NotificationManager(userRepository);
        List<List<Notification>> notificationList = notificationManger.getNotificationList();
        if(notificationList == null) {
            return -1;
        }

        int notifiedStudents = 0;
        if(!UIManager.hasNotifiedStudents) {
            if (notificationList.size() > 0) {
                for (int i = 0; i < notificationList.size(); i++) {
                    for (int j = 0; j < notificationList.get(i).size(); j++) {
                        System.out.println(notificationList.get(i).get(j).getRecipient());
                        System.out.println(notificationList.get(i).get(j).getSubject());
                        System.out.println(notificationList.get(i).get(j).getMessage());
                    }
                }
            }
            UIManager.hasNotifiedStudents = true;
            notifiedStudents = notificationList.size();
        }

        System.out.println(String.format("Total %d students have been notified.", notifiedStudents));

        return 0;
    }

    public int setupTestData(Scanner scanner, UserRepository userRepository) {
        GradeBook gradeBook1 = new GradeBook(600, 200, 150, 380);
        GradeBook gradeBook2 = new GradeBook(600, 400, 250, 110);
        GradeBook gradeBook3 = new GradeBook(600, 400, 480, 399);
        GradeBook gradeBook4 = new GradeBook(600, 400, 480, 550);


        Student student1 = new Student("John", "Doe", "johnd@email.net", gradeBook1);
        Student student2 = new Student("Jane", "Spark", "jspark@yahoo.com", gradeBook2);
        Student student3 = new Student("Michael", "Matthews", "michael.mathews@yahoo.com", gradeBook3);
        Student student4 = new Student("Nicole", "Lewis", "nlewis@yahoo.com", gradeBook4);

        Map<String,Student> testDataMap= new HashMap<>();
        testDataMap.put("TestStudent1", student1);
        testDataMap.put("TestStudent2", student2);
        testDataMap.put("TestStudent3", student3);
        testDataMap.put("TestStudent4", student4);

        userRepository.setStudentList(testDataMap);
        StatisticsManager.javaCompletedTasks = 6;
        StatisticsManager.dsaCompletedTasks = 3;
        StatisticsManager.databaseCompletedTasks = 4;
        StatisticsManager.springCompletedTasks = 4;

        System.out.println("The test data was successfully set up!");

        return 0;
    }

    private Map<String, Integer> getCourseActivityStatistics(UserRepository userRepository) {
        StatisticsManager statisticsManager = new StatisticsManager(userRepository);
        Map<String,Integer> totalPointsMap = statisticsManager.getCourseActivityMap();

        Map<String, Integer> sortedMap = totalPointsMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        return sortedMap;
    }

    private Map.Entry<String, Integer> getNthElementFromMap(Map<String, Integer> inputMap, int index) {
        if (inputMap == null) {
            return null;
        }

        if (index < 0 || index > inputMap.entrySet().size() - 1) {
            return null;
        }

        Map.Entry<String,Integer> resultEntry = null;
        int count = 0;
        for(Map.Entry<String,Integer> currentEntry : inputMap.entrySet()) {
            if (count == index) {
                resultEntry =  currentEntry;
                break;
            }

            count++;
        }

        return resultEntry;
    }

    private void updateCompletedTasksCount(int javaPoints, int dataStructuresPoints, int databasesPoints, int springPoints) {

        if (javaPoints > 0) {
            StatisticsManager.updateSubmissions(Course.JAVA);
        }

        if (dataStructuresPoints > 0) {
            StatisticsManager.updateSubmissions(Course.DSA);
        }

        if (databasesPoints > 0) {
            StatisticsManager.updateSubmissions(Course.DATABASES);
        }

        if (springPoints > 0) {
            StatisticsManager.updateSubmissions(Course.SPRING);
        }
    }
}
