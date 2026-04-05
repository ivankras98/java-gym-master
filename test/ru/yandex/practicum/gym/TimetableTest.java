package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        // Проверки
        Assertions.assertEquals(1,
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        Assertions.assertTrue(
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty()
        );
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdult = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdult);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession thursdayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));

        TrainingSession saturdayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChild);
        timetable.addNewTrainingSession(thursdayChild);
        timetable.addNewTrainingSession(saturdayChild);

        // Проверки
        Assertions.assertEquals(1,
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        List<TrainingSession> thursdayList =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Assertions.assertEquals(2, thursdayList.size());

        // Проверка сортировки
        Assertions.assertEquals(13, thursdayList.get(0).getTimeOfDay().getHours());
        Assertions.assertEquals(20, thursdayList.get(1).getTimeOfDay().getHours());

        Assertions.assertTrue(
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty()
        );
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

        // Проверки
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

    //  ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ

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

        Assertions.assertTrue(
                timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).isEmpty()
        );
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

        Assertions.assertEquals(2, stats.get(0).getCount()); // coach1
        Assertions.assertEquals(1, stats.get(1).getCount()); // coach2
    }
}