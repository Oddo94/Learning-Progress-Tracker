package tracker.model;

public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private GradeBook gradeBook;

    public Student(String firstName, String lastName, String email, GradeBook gradeBook) {
        this.firstName = firstName;
        this.lastName= lastName;
        this.email = email;
        this.gradeBook = gradeBook;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public GradeBook getGradeBook() {
        return this.gradeBook;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGradeBook(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
    }
}

