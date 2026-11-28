package gradetracker;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menu-driven console interface for the grade tracker.
 */
public class ConsoleApp {

    /** Thrown when the input stream ends, so the app can exit cleanly. */
    private static class InputClosedException extends RuntimeException {
    }

    private final GradeManager manager;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleApp(GradeManager manager, Scanner in, PrintStream out) {
        this.manager = manager;
        this.in = in;
        this.out = out;
    }

    public void run() {
        out.println("==============================");
        out.println("   STUDENT GRADE TRACKER");
        out.println("==============================");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt("Choose an option: ", 0, 10);
                out.println();
                running = handle(choice);
                out.println();
            }
        } catch (InputClosedException e) {
            out.println();
        }
        out.println("Goodbye!");
    }

    private void printMenu() {
        out.println("1. Add student");
        out.println("2. Add scores to a student");
        out.println("3. Edit or delete a score");
        out.println("4. Rename student");
        out.println("5. Remove student");
        out.println("6. View student details");
        out.println("7. Search students by name");
        out.println("8. Summary report (all students)");
        out.println("9. Class statistics");
        out.println("10. Load sample data");
        out.println("0. Exit");
    }

    private boolean handle(int choice) {
        switch (choice) {
            case 1 -> addStudent();
            case 2 -> addScores();
            case 3 -> editScore();
            case 4 -> renameStudent();
            case 5 -> removeStudent();
            case 6 -> viewStudent();
            case 7 -> searchStudents();
            case 8 -> out.print(ReportPrinter.summaryReport(manager));
            case 9 -> out.print(ReportPrinter.classStatistics(manager));
            case 10 -> loadSampleData();
            case 0 -> {
                return false;
            }
            default -> out.println("Unknown option.");
        }
        return true;
    }

    private void addStudent() {
        String name = readNonBlank("Student name: ");
        Student s = manager.addStudent(name);
        out.println("Added " + s + ".");
        if (readYesNo("Add scores now? (y/n): ")) {
            enterScores(s);
        }
    }

    private void addScores() {
        selectStudent().ifPresent(this::enterScores);
    }

    private void enterScores(Student s) {
        out.println("Enter scores for " + s.getName() + " (0-100). Press Enter on an empty line to finish.");
        int added = 0;
        while (true) {
            String line = readLine("  Score " + (s.getScoreCount() + 1) + ": ");
            if (line.isBlank()) {
                break;
            }
            Double score = parseScore(line);
            if (score != null) {
                s.addScore(score);
                added++;
            }
        }
        out.println(added + " score(s) added. " + s.getName() + " now has " + s.getScoreCount() + ".");
    }

    private void editScore() {
        Optional<Student> selected = selectStudent();
        if (selected.isEmpty()) {
            return;
        }
        Student s = selected.get();
        if (!s.hasScores()) {
            out.println(s.getName() + " has no scores to edit.");
            return;
        }
        out.print(ReportPrinter.studentDetails(s));
        int position = readInt("Score number: ", 1, s.getScoreCount());
        String action = readLine("Type a new score, or 'd' to delete it: ").trim();
        if (action.equalsIgnoreCase("d")) {
            double removed = s.removeScore(position - 1);
            out.println("Deleted score " + removed + ".");
            return;
        }
        Double score = parseScore(action);
        if (score != null) {
            s.updateScore(position - 1, score);
            out.println("Score " + position + " updated to " + score + ".");
        }
    }

    private void renameStudent() {
        selectStudent().ifPresent(s -> {
            s.setName(readNonBlank("New name for " + s.getName() + ": "));
            out.println("Renamed to " + s + ".");
        });
    }

    private void removeStudent() {
        selectStudent().ifPresent(s -> {
            if (readYesNo("Remove " + s + " and all their scores? (y/n): ")) {
                manager.removeStudent(s.getId());
                out.println("Removed " + s.getName() + ".");
            } else {
                out.println("Cancelled.");
            }
        });
    }

    private void viewStudent() {
        selectStudent().ifPresent(s -> out.print(ReportPrinter.studentDetails(s)));
    }

    private void searchStudents() {
        String text = readLine("Name contains: ");
        List<Student> matches = manager.searchByName(text);
        if (matches.isEmpty()) {
            out.println("No matching students.");
            return;
        }
        for (Student s : matches) {
            out.println("  " + s + " - average " + ReportPrinter.format(s.average())
                    + ", grade " + s.letterGrade());
        }
    }

    private void loadSampleData() {
        Object[][] sample = {
                {"Aarav Shah", new double[]{88, 92, 79, 95}},
                {"Diya Patel", new double[]{72, 68, 80, 75}},
                {"Kabir Mehta", new double[]{55, 61, 48, 70}},
                {"Isha Verma", new double[]{98, 94, 100, 91}},
                {"Rohan Gupta", new double[]{81, 77, 85, 69}},
        };
        for (Object[] row : sample) {
            Student s = manager.addStudent((String) row[0]);
            for (double score : (double[]) row[1]) {
                s.addScore(score);
            }
        }
        out.println("Loaded " + sample.length + " sample students.");
    }

    // ---------- input helpers ----------

    private Optional<Student> selectStudent() {
        if (manager.isEmpty()) {
            out.println("No students yet. Add a student first.");
            return Optional.empty();
        }
        for (Student s : manager.getStudents()) {
            out.println("  " + s);
        }
        int id = readInt("Student ID: ", 1, Integer.MAX_VALUE);
        Optional<Student> found = manager.findById(id);
        if (found.isEmpty()) {
            out.println("No student with ID " + id + ".");
        }
        return found;
    }

    private Double parseScore(String text) {
        try {
            double score = Double.parseDouble(text.trim());
            if (Student.isValidScore(score)) {
                return score;
            }
            out.println("  Score must be between 0 and 100.");
        } catch (NumberFormatException e) {
            out.println("  '" + text.trim() + "' is not a number.");
        }
        return null;
    }

    private String readLine(String prompt) {
        out.print(prompt);
        if (!in.hasNextLine()) {
            throw new InputClosedException();
        }
        // Strip a byte-order mark that some shells add when piping input.
        return in.nextLine().replace("﻿", "");
    }

    private String readNonBlank(String prompt) {
        while (true) {
            String line = readLine(prompt).trim();
            if (!line.isEmpty()) {
                return line;
            }
            out.println("Please enter a value.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt).trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            out.println(max == Integer.MAX_VALUE
                    ? "Please enter a whole number."
                    : "Please enter a number from " + min + " to " + max + ".");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt).trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) return true;
            if (line.equals("n") || line.equals("no")) return false;
            out.println("Please answer y or n.");
        }
    }
}
