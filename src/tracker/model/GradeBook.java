package tracker.model;

public class GradeBook {
    int javaPoints;
    int dataStructuresPoints;
    int databasePoints;
    int springPoints;

    public GradeBook(int javaPoints, int dataStructuresPoints, int databasePoints, int springPoints) {
        this.javaPoints = javaPoints;
        this.dataStructuresPoints = dataStructuresPoints;
        this.databasePoints = databasePoints;
        this.springPoints = springPoints;
    }

    public GradeBook() {

    }

    public int getJavaPoints() {
        return javaPoints;
    }

    public void setJavaPoints(int javaPoints) {
        this.javaPoints = javaPoints;
    }

    public int getDataStructuresPoints() {
        return dataStructuresPoints;
    }

    public void setDataStructuresPoints(int dataStructuresPoints) {
        this.dataStructuresPoints = dataStructuresPoints;
    }

    public int getDatabasePoints() {
        return databasePoints;
    }

    public void setDatabasePoints(int databasePoints) {
        this.databasePoints = databasePoints;
    }

    public int getSpringPoints() {
        return springPoints;
    }

    public void setSpringPoints(int springPoints) {
        this.springPoints = springPoints;
    }
}
