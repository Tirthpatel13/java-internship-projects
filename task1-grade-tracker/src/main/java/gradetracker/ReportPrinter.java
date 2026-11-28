package gradetracker;

import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;

/**
 * Builds the text reports shown in the console.
 */
public final class ReportPrinter {

    private static final String ROW_FORMAT = "%-4s %-20s %6s %8s %8s %8s %6s%n";

    private ReportPrinter() {
    }

    /** Table of every student, ranked by average, followed by class statistics. */
    public static String summaryReport(GradeManager manager) {
        if (manager.isEmpty()) {
            return "No students yet. Add a student first.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=================== STUDENT SUMMARY REPORT ===================\n");
        sb.append(String.format(Locale.US, ROW_FORMAT,
                "ID", "Name", "Scores", "Average", "Highest", "Lowest", "Grade"));
        sb.append("-".repeat(62)).append('\n');
        for (Student s : manager.rankedByAverage()) {
            sb.append(String.format(Locale.US, ROW_FORMAT,
                    s.getId(),
                    truncate(s.getName(), 20),
                    s.getScoreCount(),
                    format(s.average()),
                    format(s.highest()),
                    format(s.lowest()),
                    s.letterGrade()));
        }
        sb.append("-".repeat(62)).append('\n');
        sb.append(classStatistics(manager));
        return sb.toString();
    }

    /** Class-wide average, highest and lowest scores. */
    public static String classStatistics(GradeManager manager) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLASS STATISTICS\n");
        sb.append(String.format(Locale.US, "  Students        : %d%n", manager.getStudentCount()));
        sb.append(String.format(Locale.US, "  Scores recorded : %d%n", manager.getTotalScoreCount()));

        OptionalDouble avg = manager.classAverage();
        if (avg.isEmpty()) {
            sb.append("  No scores recorded yet.\n");
            return sb.toString();
        }
        sb.append(String.format(Locale.US, "  Class average   : %.2f (%s)%n",
                avg.getAsDouble(), Grades.letterFor(avg.getAsDouble())));
        manager.highestScore().ifPresent(e -> sb.append(String.format(Locale.US,
                "  Highest score   : %.2f by %s%n", e.score(), e.student().getName())));
        manager.lowestScore().ifPresent(e -> sb.append(String.format(Locale.US,
                "  Lowest score    : %.2f by %s%n", e.score(), e.student().getName())));
        manager.topStudent().ifPresent(s -> sb.append(String.format(Locale.US,
                "  Top student     : %s (avg %.2f)%n", s.getName(), s.average().getAsDouble())));
        manager.bottomStudent().ifPresent(s -> sb.append(String.format(Locale.US,
                "  Needs support   : %s (avg %.2f)%n", s.getName(), s.average().getAsDouble())));
        return sb.toString();
    }

    /** Full details of one student, including each numbered score. */
    public static String studentDetails(Student s) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student %s%n", s));
        List<Double> scores = s.getScores();
        if (scores.isEmpty()) {
            sb.append("  No scores yet.\n");
            return sb.toString();
        }
        for (int i = 0; i < scores.size(); i++) {
            sb.append(String.format(Locale.US, "  %2d. %6.2f%n", i + 1, scores.get(i)));
        }
        sb.append(String.format(Locale.US, "  Average: %s | Highest: %s | Lowest: %s | Grade: %s%n",
                format(s.average()), format(s.highest()), format(s.lowest()), s.letterGrade()));
        return sb.toString();
    }

    static String format(OptionalDouble value) {
        return value.isPresent() ? String.format(Locale.US, "%.2f", value.getAsDouble()) : "-";
    }

    private static String truncate(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}
