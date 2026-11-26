package gradetracker;

/**
 * Converts numeric scores to letter grades.
 */
public final class Grades {

    private Grades() {
    }

    public static String letterFor(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}
