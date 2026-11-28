package gradetracker;

import java.util.Scanner;

/**
 * Entry point for the Student Grade Tracker (Task 1).
 */
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new ConsoleApp(new GradeManager(), scanner, System.out).run();
        }
    }
}
