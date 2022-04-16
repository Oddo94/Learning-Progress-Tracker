package tracker.repository;

import tracker.model.Student;

import java.util.*;

public class UserRepository {

    private Map<String, Student> studentList;

    public UserRepository() {
        this.studentList = new HashMap<>();
    }

    public Map<String, Student> getStudentList() {
        return this.studentList;
    }

    public void setStudentList(Map<String, Student> studentList) {
        this.studentList = studentList;
    }

    public int addStudent(String uniqueIdentifier, Student student) {
        if(uniqueIdentifier == null || student == null) {
            return -1;
        }

        this.studentList.put(uniqueIdentifier,student);

        return 0;
    }
}
