package gradetracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void newStudentHasNoScores() {
        Student s = new Student(1, "Asha");
        assertFalse(s.hasScores());
        assertTrue(s.average().isEmpty());
        assertTrue(s.highest().isEmpty());
        assertTrue(s.lowest().isEmpty());
        assertEquals("-", s.letterGrade());
    }

    @Test
    void calculatesAverageHighestAndLowest() {
        Student s = new Student(1, "Asha");
        s.addScore(80);
        s.addScore(95);
        s.addScore(65);

        assertEquals(80.0, s.average().getAsDouble(), 1e-9);
        assertEquals(95.0, s.highest().getAsDouble());
        assertEquals(65.0, s.lowest().getAsDouble());
        assertEquals("B", s.letterGrade());
    }

    @Test
    void rejectsScoresOutsideRange() {
        Student s = new Student(1, "Asha");
        assertThrows(IllegalArgumentException.class, () -> s.addScore(-1));
        assertThrows(IllegalArgumentException.class, () -> s.addScore(100.5));
        assertThrows(IllegalArgumentException.class, () -> s.addScore(Double.NaN));
        s.addScore(0);
        s.addScore(100);
        assertEquals(2, s.getScoreCount());
    }

    @Test
    void updatesAndRemovesScores() {
        Student s = new Student(1, "Asha");
        s.addScore(50);
        s.addScore(70);

        s.updateScore(0, 90);
        assertEquals(90.0, s.getScores().get(0));

        assertEquals(70.0, s.removeScore(1));
        assertEquals(1, s.getScoreCount());

        assertThrows(IndexOutOfBoundsException.class, () -> s.updateScore(5, 50));
        assertThrows(IndexOutOfBoundsException.class, () -> s.removeScore(-1));
    }

    @Test
    void scoresListIsReadOnly() {
        Student s = new Student(1, "Asha");
        s.addScore(50);
        assertThrows(UnsupportedOperationException.class, () -> s.getScores().add(99.0));
    }

    @Test
    void rejectsBlankNameAndTrimsName() {
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "  "));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, null));
        assertEquals("Ravi", new Student(2, "  Ravi ").getName());
    }

    @Test
    void letterGradeBoundaries() {
        assertEquals("A", Grades.letterFor(90));
        assertEquals("B", Grades.letterFor(89.99));
        assertEquals("C", Grades.letterFor(70));
        assertEquals("D", Grades.letterFor(60));
        assertEquals("F", Grades.letterFor(59.9));
    }
}
