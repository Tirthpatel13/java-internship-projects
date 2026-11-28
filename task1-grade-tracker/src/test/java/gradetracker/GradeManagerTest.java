package gradetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void emptyClassHasNoStatistics() {
        assertTrue(manager.classAverage().isEmpty());
        assertTrue(manager.highestScore().isEmpty());
        assertTrue(manager.lowestScore().isEmpty());
        assertTrue(manager.topStudent().isEmpty());
        assertTrue(manager.bottomStudent().isEmpty());
    }

    @Test
    void calculatesClassStatistics() {
        Student asha = manager.addStudent("Asha");
        asha.addScore(90);
        asha.addScore(100);
        Student ravi = manager.addStudent("Ravi");
        ravi.addScore(40);
        ravi.addScore(70);
        manager.addStudent("Meera"); // no scores yet

        assertEquals(75.0, manager.classAverage().getAsDouble(), 1e-9);

        assertEquals(100.0, manager.highestScore().get().score());
        assertSame(asha, manager.highestScore().get().student());
        assertEquals(40.0, manager.lowestScore().get().score());
        assertSame(ravi, manager.lowestScore().get().student());

        assertSame(asha, manager.topStudent().get());
        assertSame(ravi, manager.bottomStudent().get());
    }

    @Test
    void ranksStudentsByAverageWithUnscoredLast() {
        Student asha = manager.addStudent("Asha");
        asha.addScore(60);
        Student meera = manager.addStudent("Meera");
        Student ravi = manager.addStudent("Ravi");
        ravi.addScore(85);

        assertEquals(List.of(ravi, asha, meera), manager.rankedByAverage());
    }

    @Test
    void studentListIsReadOnly() {
        manager.addStudent("Asha");
        assertThrows(UnsupportedOperationException.class,
                () -> manager.getStudents().add(new Student(5, "X")));
    }
}
