package tracker.utils;

import tracker.model.Notification;
import tracker.model.Student;
import tracker.repository.UserRepository;
import tracker.utils.enums.Course;

import java.util.*;
import java.util.stream.Collectors;

public class NotificationManager {
    private UserRepository userRepository;

    public NotificationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private List<Student> getStudentsToNotify(Map<String, Student> studentMap) {
        //Map<String, Student> studentMap = userRepository.getStudentList();

        List<Student> studentsToNotifyList =  studentMap
                .entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .filter(student -> student.getGradeBook().getJavaPoints() == Course.JAVA.getGraduationPoints()
                        || student.getGradeBook().getDataStructuresPoints() == Course.DSA.getGraduationPoints()
                        || student.getGradeBook().getDatabasePoints() == Course.DATABASES.getGraduationPoints()
                        || student.getGradeBook().getSpringPoints() == Course.SPRING.getGraduationPoints())
                .collect(Collectors.toList());

        return studentsToNotifyList;
    }

    public List<List<Notification>> getNotificationList() {
        List<Student> studentsToNotifyList = getStudentsToNotify(this.userRepository.getStudentList());

        List<List<Notification>> notificationList = new ArrayList<>();

        for(Student currentStudent : studentsToNotifyList) {
            List<Notification> currentStudentNotificationList = getCorrectNotificationForCourse(currentStudent);
            notificationList.add(currentStudentNotificationList);
        }

        return notificationList;
    }

    private List<Notification> getCorrectNotificationForCourse(Student student) {
        List<Notification> currentStudentNotificationList = new ArrayList<>();

        String recipientTemplate = "To: %s";
        String subjectTemplate = "Re: Your learning progress";
        String messageTemplate = "Hello, %s %s! You have accomplished our %s course!";

        if(student.getGradeBook().getJavaPoints() == Course.JAVA.getGraduationPoints()) {
            Notification notification = new Notification();

            notification.setRecipient(String.format(recipientTemplate, student.getEmail()));
            notification.setSubject(subjectTemplate);
            notification.setMessage(String.format(messageTemplate, student.getFirstName(), student.getLastName(), "Java"));

            currentStudentNotificationList.add(notification);
        }

        if(student.getGradeBook().getDataStructuresPoints() == Course.DSA.getGraduationPoints()) {
            Notification notification = new Notification();

            notification.setRecipient(String.format(recipientTemplate, student.getEmail()));
            notification.setSubject(subjectTemplate);
            notification.setMessage(String.format(messageTemplate, student.getFirstName(), student.getLastName(), "DSA"));

            currentStudentNotificationList.add(notification);
        }

        if(student.getGradeBook().getDatabasePoints() == Course.DATABASES.getGraduationPoints()) {
            Notification notification = new Notification();

            notification.setRecipient(String.format(recipientTemplate, student.getEmail()));
            notification.setSubject(subjectTemplate);
            notification.setMessage(String.format(messageTemplate, student.getFirstName(), student.getLastName(), "Databases"));

            currentStudentNotificationList.add(notification);
        }

        if(student.getGradeBook().getSpringPoints() == Course.SPRING.getGraduationPoints()) {
            Notification notification = new Notification();

            notification.setRecipient(String.format(recipientTemplate, student.getEmail()));
            notification.setSubject(subjectTemplate);
            notification.setMessage(String.format(messageTemplate, student.getFirstName(), student.getLastName(), "Spring"));

            currentStudentNotificationList.add(notification);
        }

        return currentStudentNotificationList;

    }
}
