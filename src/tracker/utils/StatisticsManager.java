package tracker.utils;

import tracker.model.Student;
import tracker.repository.UserRepository;
import tracker.utils.enums.ActivityLevel;
import tracker.utils.enums.Course;
import tracker.utils.enums.DifficultyLevel;
import tracker.utils.enums.PopularityLevel;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsManager {
    private static final int JAVA_GRADUATION_POINTS = 600;
    private static final int DSA_GRADUATION_POINTS = 400;
    private static final int DATABASES_GRADUATION_POINTS = 480;
    private static final int SPRING_GRADUATION_POINTS = 550;

    public static int javaCompletedTasks = 0;
    public static int dsaCompletedTasks = 0;
    public static int databaseCompletedTasks = 0;
    public static int springCompletedTasks = 0;

    private static Long maxEntryValue = 0L;
    private static Long minEntryValue = 0L;
    private static int maxSubmissions = 0;
    private static int minSubmissions = 0;


    private UserRepository userRepository;

    public StatisticsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public long calculateEnrolledStudents(Course course) {
        Map<String, Student> studentList = userRepository.getStudentList();
        long totalEnrolledStudents = 0;

        switch(course) {
            case JAVA:
                totalEnrolledStudents = studentList.entrySet().stream()
                        .map(entry -> entry.getValue().getGradeBook().getJavaPoints())
                        .filter(javaPoints -> javaPoints > 0)
                        .count();
                break;

            case DSA:
                totalEnrolledStudents = studentList.entrySet().stream()
                        .map(entry -> entry.getValue().getGradeBook().getDataStructuresPoints())
                        .filter(dataStructuresPoints -> dataStructuresPoints > 0)
                        .count();
                break;

            case DATABASES:
                totalEnrolledStudents = studentList.entrySet().stream()
                        .map(entry -> entry.getValue().getGradeBook().getDatabasePoints())
                        .filter(databasePoints -> databasePoints > 0)
                        .count();
                break;

            case SPRING:
                totalEnrolledStudents = studentList.entrySet().stream()
                        .map(entry -> entry.getValue().getGradeBook().getSpringPoints())
                        .filter(springPoints -> springPoints > 0)
                        .count();
                break;

            default:
                break;
        }

        return totalEnrolledStudents;
    }

    public Map<String, Integer> getCourseActivityMap() {

        Optional<Integer> javaTotalPoints = userRepository.getStudentList().entrySet()
                .stream()
                .map(mapEntry -> mapEntry.getValue().getGradeBook().getJavaPoints())
                .reduce(Integer::sum);

        Optional<Integer> dsaTotalPoints = userRepository.getStudentList().entrySet()
                .stream()
                .map(mapEntry -> mapEntry.getValue().getGradeBook().getDataStructuresPoints())
                .reduce(Integer::sum);

        Optional<Integer> databasesTotalPoints = userRepository.getStudentList().entrySet()
                .stream()
                .map(mapEntry -> mapEntry.getValue().getGradeBook().getDatabasePoints())
                .reduce(Integer::sum);

        Optional<Integer> springTotalPoints = userRepository.getStudentList().entrySet()
                .stream()
                .map(mapEntry -> mapEntry.getValue().getGradeBook().getSpringPoints())
                .reduce(Integer::sum);

        Map<String, Integer> totalPointsMap = new HashMap<>();
        totalPointsMap.put("Java", javaTotalPoints.orElse(0));
        totalPointsMap.put("DSA", dsaTotalPoints.orElse(0));
        totalPointsMap.put("Databases", databasesTotalPoints.orElse(0));
        totalPointsMap.put("Spring", springTotalPoints.orElse(0));

        return totalPointsMap;

    }

    //Method for updating the number of completed tasks for each course
    public static void updateSubmissions(Course course) {
        switch (course) {
            case JAVA:
                javaCompletedTasks++;
                break;

            case DSA:
                dsaCompletedTasks++;
                break;

            case DATABASES:
                databaseCompletedTasks++;
                break;

            case SPRING:
                springCompletedTasks++;
                break;

            default:
                break;
        }
    }

    private static String getCourseName(int index) {
        switch (index) {
            case 0:
                return "Java";

            case 1:
                return "DSA";

            case 2:
                return "Databases";

            case 3:
                return "Spring";

            default:
                return null;
        }
    }

    public String getCourseBasedOnPopularity(PopularityLevel popularityLevel) {
        Map<String,Long> enrolledStudentsCountMap = getEnrolledStudentsStatistics(userRepository);

        if (popularityLevel == PopularityLevel.MOST_POPULAR) {
            maxEntryValue = enrolledStudentsCountMap
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> entry.getValue())
                    .orElse(0L);

            String topCourses;
            Long maxValue = maxEntryValue;
            if(maxEntryValue > 0L) {
                topCourses = enrolledStudentsCountMap
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() > 0 && entry.getValue() == maxValue)
                        .map(entry -> entry.getKey())
                        .collect(Collectors.joining(", "));
            } else {
                topCourses = "n/a";
            }

            return topCourses;
        } else if (popularityLevel == PopularityLevel.LEAST_POPULAR) {
            minEntryValue = enrolledStudentsCountMap
                    .entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .map(entry -> entry.getValue())
                    .orElse(0L);

            Optional<String> leastPopular;
            Long minValue = minEntryValue;
            if (maxEntryValue != minEntryValue) {
                leastPopular = enrolledStudentsCountMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() > 0 && entry.getValue() == minValue)
                        .map(entry -> entry.getKey())
                        .findFirst();
            } else {
                leastPopular = Optional.of("n/a");
            }

            return leastPopular.orElse("n/a");
        }

        return "n/a";

    }

    public String getCourseBasedOnActivity(ActivityLevel activityLevel) {
        String courseName;
        ArrayList<Integer> completedTasksArray;

        Map<String, Integer> totalSubmissionsMap = new LinkedHashMap<>();
        totalSubmissionsMap.put("Java", javaCompletedTasks);
        totalSubmissionsMap.put("DSA", dsaCompletedTasks);
        totalSubmissionsMap.put("Databases", databaseCompletedTasks);
        totalSubmissionsMap.put("Spring", springCompletedTasks);

        if (activityLevel == ActivityLevel.HIGHEST_ACTIVITY) {
            completedTasksArray = new ArrayList<>(List.of(javaCompletedTasks, dsaCompletedTasks, databaseCompletedTasks, springCompletedTasks));

            maxSubmissions = totalSubmissionsMap
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> entry.getValue())
                    .orElse(0);



            if (maxSubmissions == 0) {
                return courseName = "n/a";
            }

            courseName = String.join(", ", totalSubmissionsMap
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == maxSubmissions)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList()));

        } else if (activityLevel == ActivityLevel.LOWEST_ACTIVITY) {
            completedTasksArray = new ArrayList<>(List.of(javaCompletedTasks, dsaCompletedTasks, databaseCompletedTasks, springCompletedTasks));

            minSubmissions = totalSubmissionsMap
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> entry.getValue())
                    .orElse(0);

            if (minSubmissions == 0) {
                return courseName = "n/a";
            }


            if(maxSubmissions != minSubmissions) {
                courseName = String.join(", ", totalSubmissionsMap
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() == minSubmissions)
                        .map(entry -> entry.getKey())
                        .collect(Collectors.toList()));

            } else {
                courseName = "n/a";
            }
        } else {
            courseName = "n/a";
        }


        return courseName;

    }

    public String getCourseBasedOnDifficulty(DifficultyLevel difficultyLevel) {
        String courseName;
        Map<String,Integer> totalPointsMap = getCourseActivityMap();

        double javaAverageGrade =  totalPointsMap.get("Java") != 0 && javaCompletedTasks != 0 ? (double) totalPointsMap.get("Java") / javaCompletedTasks : 0;
        double dsaAverageGrade = totalPointsMap.get("DSA") != 0  && dsaCompletedTasks != 0 ?  (double) totalPointsMap.get("DSA") / dsaCompletedTasks : 0;
        double databaseAverageGrade = totalPointsMap.get("Databases") != 0  && databaseCompletedTasks != 0 ?  (double) totalPointsMap.get("Databases") / databaseCompletedTasks : 0;
        double springAverageGrade = totalPointsMap.get("Spring") != 0  && springCompletedTasks != 0 ?  (double) totalPointsMap.get("Spring") / springCompletedTasks : 0;

        ArrayList averageGradesArray = new ArrayList<>(List.of(javaAverageGrade, dsaAverageGrade, databaseAverageGrade, springAverageGrade));

        Map<String, Double> averageGradesMap = new HashMap<>();
        averageGradesMap.put("Java", javaAverageGrade);
        averageGradesMap.put("DSA", dsaAverageGrade);
        averageGradesMap.put("Database", databaseAverageGrade);
        averageGradesMap.put("Spring", springAverageGrade);

        if (difficultyLevel == DifficultyLevel.EASIEST) {
            Optional<String> maxPointsCourse = averageGradesMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 0)
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            courseName = maxPointsCourse.orElse("n/a");


        } else if  (difficultyLevel == DifficultyLevel.HARDEST) {
            Optional<String> minPointsCourse = averageGradesMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > 0)
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            courseName = minPointsCourse.orElse("n/a");

        } else {
            courseName = "n/a";
        }

        return courseName;
    }

    public Map<String, Double> getCompletionPercentage(Course course) {
        double completionPerecentage;
        Map<String, Student> studentMap = userRepository.getStudentList();
        Map<String, Double> resultMap;

        switch(course) {
            case JAVA:
                resultMap =  studentMap.entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey(), (entry.getValue().getGradeBook().getJavaPoints() * 100 / ((double)course.getGraduationPoints()))))
                        .filter(entry -> entry.getValue() > 0)
                        .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            case DSA:
                resultMap = studentMap.entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey(), (entry.getValue().getGradeBook().getDataStructuresPoints() * 100 / ((double)course.getGraduationPoints()))))
                        .filter(entry -> entry.getValue() > 0)
                        .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            case DATABASES:
                resultMap = studentMap.entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey(), (entry.getValue().getGradeBook().getDatabasePoints() * 100 / ((double)course.getGraduationPoints()))))
                        .filter(entry -> entry.getValue() > 0)
                        .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            case SPRING:
                resultMap = studentMap.entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey(), (entry.getValue().getGradeBook().getSpringPoints() * 100 / ((double)course.getGraduationPoints()))))
                        .filter(entry -> entry.getValue() > 0)
                        .sorted(Comparator.comparing(Map.Entry<String, Double>::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                break;

            default:
                resultMap = null;

        }

        return resultMap;
    }

    public int getTotalPointsForStudent(String uuid, Map<String, Student> studentMap, Course course) {
        if (uuid == null || studentMap == null) {
            return 0;
        }
        int totalPoints;

        switch(course) {
            case JAVA:
                totalPoints = studentMap.get(uuid).getGradeBook().getJavaPoints();
                break;

            case DSA:
                totalPoints = studentMap.get(uuid).getGradeBook().getDataStructuresPoints();
                break;

            case DATABASES:
                totalPoints = studentMap.get(uuid).getGradeBook().getDatabasePoints();
                break;

            case SPRING:
                totalPoints = studentMap.get(uuid).getGradeBook().getSpringPoints();
                break;

            default:
                totalPoints = 0;
        }

        return totalPoints;
    }

    private Map<String,Long> getEnrolledStudentsStatistics(UserRepository userRepository) {
        StatisticsManager statisticsManager = new StatisticsManager(userRepository);

        long javaEnrolledStudentsCount = statisticsManager.calculateEnrolledStudents(Course.JAVA);
        long dsaEnrolledStudentsCount = statisticsManager.calculateEnrolledStudents((Course.DSA));
        long databasesEnrolledStudentsCount = statisticsManager.calculateEnrolledStudents((Course.DATABASES));
        long springEnrolledStudentsCount = statisticsManager.calculateEnrolledStudents((Course.SPRING));

        Map<String, Long> enrolledStudentsCountMap = new LinkedHashMap<>();

        enrolledStudentsCountMap.put("Java", javaEnrolledStudentsCount);

        enrolledStudentsCountMap.put("DSA", dsaEnrolledStudentsCount);

        enrolledStudentsCountMap.put("Databases", databasesEnrolledStudentsCount);

        enrolledStudentsCountMap.put("Spring", springEnrolledStudentsCount);

        return enrolledStudentsCountMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


}
