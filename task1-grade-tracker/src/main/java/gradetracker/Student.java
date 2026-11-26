package gradetracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

/**
 * A student with a name, a unique id and a list of scores (0-100).
 */
public class Student {

    public static final double MIN_SCORE = 0.0;
    public static final double MAX_SCORE = 100.0;

    private final int id;
    private String name;
    private final ArrayList<Double> scores = new ArrayList<>();

    public Student(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student id must be positive");
        }
        this.id = id;
        setName(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        this.name = name.trim();
    }

    /** Returns a read-only view of the scores, in the order they were added. */
    public List<Double> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public int getScoreCount() {
        return scores.size();
    }

    public boolean hasScores() {
        return !scores.isEmpty();
    }

    public void addScore(double score) {
        validateScore(score);
        scores.add(score);
    }

    /**
     * Replaces the score at the given position.
     *
     * @param index zero-based position in the score list
     */
    public void updateScore(int index, double newScore) {
        checkIndex(index);
        validateScore(newScore);
        scores.set(index, newScore);
    }

    /**
     * Removes the score at the given position and returns it.
     *
     * @param index zero-based position in the score list
     */
    public double removeScore(int index) {
        checkIndex(index);
        return scores.remove(index);
    }

    public OptionalDouble average() {
        return scores.stream().mapToDouble(Double::doubleValue).average();
    }

    public OptionalDouble highest() {
        return scores.stream().mapToDouble(Double::doubleValue).max();
    }

    public OptionalDouble lowest() {
        return scores.stream().mapToDouble(Double::doubleValue).min();
    }

    /** Letter grade based on the average, or "-" when there are no scores yet. */
    public String letterGrade() {
        OptionalDouble avg = average();
        return avg.isPresent() ? Grades.letterFor(avg.getAsDouble()) : "-";
    }

    public static boolean isValidScore(double score) {
        return !Double.isNaN(score) && score >= MIN_SCORE && score <= MAX_SCORE;
    }

    private static void validateScore(double score) {
        if (!isValidScore(score)) {
            throw new IllegalArgumentException(
                    "Score must be between " + (int) MIN_SCORE + " and " + (int) MAX_SCORE);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= scores.size()) {
            throw new IndexOutOfBoundsException("No score at position " + (index + 1));
        }
    }

    @Override
    public String toString() {
        return "#" + id + " " + name;
    }
}
