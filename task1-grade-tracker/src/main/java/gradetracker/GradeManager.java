package gradetracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Stores students in an ArrayList and manages adding, finding and removing them.
 */
public class GradeManager {

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
}
