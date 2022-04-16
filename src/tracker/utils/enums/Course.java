package tracker.utils.enums;

public enum Course {
    JAVA("Java", 600),
    DSA("DSA", 400),
    DATABASES("Databases", 480),
    SPRING("Spring", 550);

    private String courseName;
    private int graduationPoints;

    Course(String courseName, int graduationPoints) {
        this.courseName = courseName;
        this.graduationPoints = graduationPoints;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public int getGraduationPoints() {
        return this.graduationPoints;
    }

}
