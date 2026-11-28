package gradetracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Stores students in an ArrayList and calculates class-wide statistics.
 */
public class GradeManager {

    /** A single score together with the student who earned it. */
    public record ScoreEntry(Student student, double score) {
    }

    private final ArrayList<Student> students = new ArrayList<>();
    private int nextId = 1;

    public Student addStudent(String name) {
        Student student = new Student(nextId, name);
        students.add(student);
        nextId++;
        return student;
    }

    public Optional<Student> findById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    /** Case-insensitive search for students whose name contains the given text. */
    public List<Student> searchByName(String text) {
        String needle = text == null ? "" : text.trim().toLowerCase();
        List<Student> matches = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(needle)) {
                matches.add(s);
            }
        }
        return matches;
    }

    public boolean removeStudent(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public int getStudentCount() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    /** Total number of scores recorded across all students. */
    public int getTotalScoreCount() {
        int total = 0;
        for (Student s : students) {
            total += s.getScoreCount();
        }
        return total;
    }

    /** Average of every individual score in the class. */
    public OptionalDouble classAverage() {
        return students.stream()
                .flatMap(s -> s.getScores().stream())
                .mapToDouble(Double::doubleValue)
                .average();
    }

    /** The single highest score in the class and who earned it. */
    public Optional<ScoreEntry> highestScore() {
        return allScores().stream().max(Comparator.comparingDouble(ScoreEntry::score));
    }

    /** The single lowest score in the class and who earned it. */
    public Optional<ScoreEntry> lowestScore() {
        return allScores().stream().min(Comparator.comparingDouble(ScoreEntry::score));
    }

    /** Student with the highest average (ignores students without scores). */
    public Optional<Student> topStudent() {
        return studentsWithScores().stream()
                .max(Comparator.comparingDouble(s -> s.average().getAsDouble()));
    }

    /** Student with the lowest average (ignores students without scores). */
    public Optional<Student> bottomStudent() {
        return studentsWithScores().stream()
                .min(Comparator.comparingDouble(s -> s.average().getAsDouble()));
    }

    /** Students sorted by average, best first; students without scores come last. */
    public List<Student> rankedByAverage() {
        List<Student> ranked = new ArrayList<>(students);
        ranked.sort(Comparator.comparingDouble(
                (Student s) -> s.average().orElse(Double.NEGATIVE_INFINITY)).reversed());
        return ranked;
    }

    private List<Student> studentsWithScores() {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.hasScores()) {
                result.add(s);
            }
        }
        return result;
    }

    private List<ScoreEntry> allScores() {
        List<ScoreEntry> entries = new ArrayList<>();
        for (Student s : students) {
            for (double score : s.getScores()) {
                entries.add(new ScoreEntry(s, score));
            }
        }
        return entries;
    }
}
