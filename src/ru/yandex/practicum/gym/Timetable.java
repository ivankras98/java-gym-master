package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    // Основное расписание
    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    // Счётчик тренировок по тренерам (оптимизация)
    private Map<Coach, Integer> coachesCounter = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {

        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // Добавляем день, если его ещё нет
        timetable.putIfAbsent(day, new TreeMap<>());

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);

        // Добавляем время, если его ещё нет
        dayMap.putIfAbsent(time, new ArrayList<>());

        // Добавляем тренировку
        dayMap.get(time).add(trainingSession);

        // 🔥 СРАЗУ считаем тренировки тренера (оптимизация)
        Coach coach = trainingSession.getCoach();
        coachesCounter.put(coach, coachesCounter.getOrDefault(coach, 0) + 1);
    }

    // O(1) — возвращаем готовую структуру
    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {

        TreeMap<TimeOfDay, List<TrainingSession>> trainingsForDay = timetable.get(dayOfWeek);

        if (trainingsForDay == null) {
            return new TreeMap<>();
        }

        return trainingsForDay;
    }

    // O(log n)
    public List<TrainingSession> getTrainingSessionsForDayAndTime(
            DayOfWeek dayOfWeek,
            TimeOfDay timeOfDay
    ) {

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return new ArrayList<>();
        }

        return dayMap.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {

        List<CounterOfTrainings> result = new ArrayList<>();

        // Создаём список из готового счётчика
        for (Coach coach : coachesCounter.keySet()) {
            result.add(new CounterOfTrainings(coach, coachesCounter.get(coach)));
        }

        // Сортировка по убыванию
        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        return result;
    }
}