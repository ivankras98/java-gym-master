package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(session);

        TreeMap<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        TreeMap<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertEquals(1, monday.size());
        Assertions.assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        timetable.addNewTrainingSession(new TrainingSession(
                groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(
                groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(
                groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(
                groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        // Проверка понедельника
        TreeMap<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, monday.size());

        // Проверка четверга
        TreeMap<TimeOfDay, List<TrainingSession>> thursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Assertions.assertEquals(2, thursday.size());

        // 🔥 Проверка сортировки
        List<TimeOfDay> times = new ArrayList<>(thursday.keySet());

        Assertions.assertEquals(13, times.get(0).getHours());
        Assertions.assertEquals(20, times.get(1).getHours());

        // Проверка вторника
        TreeMap<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        Assertions.assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(session);

        Assertions.assertEquals(1,
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY, new TimeOfDay(13, 0)
                ).size());

        Assertions.assertTrue(
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY, new TimeOfDay(14, 0)
                ).isEmpty()
        );
    }

    // ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ

    @Test
    void testMultipleSessionsSameTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Дети", Age.CHILD, 60);

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        TimeOfDay time = new TimeOfDay(15, 0);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.MONDAY, time));

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.MONDAY, time));

        Assertions.assertEquals(2,
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, time).size());
    }

    @Test
    void testEmptyDay() {
        Timetable timetable = new Timetable();

        TreeMap<TimeOfDay, List<TrainingSession>> sunday =
                timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);

        Assertions.assertTrue(sunday.isEmpty());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");

        Group group = new Group("Дети", Age.CHILD, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(
                new TrainingSession(group, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> stats = timetable.getCountByCoaches();

        Assertions.assertEquals(2, stats.get(0).getCount());
        Assertions.assertEquals(1, stats.get(1).getCount());
    }
}