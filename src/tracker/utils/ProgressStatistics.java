package tracker.utils;

public class ProgressStatistics {
    private int totalPoints;
    private double completionPercentage;

    public ProgressStatistics(int totalPoints, double completionPercentage) {
        this.totalPoints = totalPoints;
        this.completionPercentage = completionPercentage;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }
}
