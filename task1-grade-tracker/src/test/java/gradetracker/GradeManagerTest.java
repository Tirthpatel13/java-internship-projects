package gradetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeManagerTest {

    private GradeManager manager;

    @BeforeEach
    void setUp() {
        manager = new GradeManager();
    }

    @Test
    void startsEmpty() {
        assertTrue(manager.isEmpty());
        assertEquals(0, manager.getStudentCount());
        assertEquals(0, manager.getTotalScoreCount());
    }

    @Test
    void assignsIncreasingIdsThatAreNotReused() {
        Student a = manager.addStudent("Asha");
        Student b = manager.addStudent("Ravi");
        assertEquals(1, a.getId());
        assertEquals(2, b.getId());

        assertTrue(manager.removeStudent(2));
        Student c = manager.addStudent("Meera");
        assertEquals(3, c.getId());
    }

    @Test
    void findsAndRemovesStudents() {
        manager.addStudent("Asha");
        manager.addStudent("Ravi Kumar");

        assertTrue(manager.findById(2).isPresent());
        assertTrue(manager.findById(99).isEmpty());
        assertEquals(1, manager.searchByName("kumar").size());

        assertTrue(manager.removeStudent(1));
        assertFalse(manager.removeStudent(1));
        assertEquals(1, manager.getStudentCount());
    }

    @Test
    void countsScoresAcrossStudents() {
        manager.addStudent("Asha").addScore(80);
        Student ravi = manager.addStudent("Ravi");
        ravi.addScore(70);
        ravi.addScore(90);

        assertEquals(3, manager.getTotalScoreCount());
    }

    @Test
    void studentListIsReadOnly() {
        manager.addStudent("Asha");
        assertThrows(UnsupportedOperationException.class,
                () -> manager.getStudents().add(new Student(5, "X")));
    }
}
