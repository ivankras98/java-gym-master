package ru.yandex.practicum.gym;
import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        timetable.putIfAbsent(day, new TreeMap<>());
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);

        dayMap.putIfAbsent(time, new ArrayList<>());
        dayMap.get(time).add(trainingSession);
    }

    // O(1)
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (!timetable.containsKey(dayOfWeek)) {
            return new ArrayList<>();
        }

        List<TrainingSession> result = new ArrayList<>();

        for (List<TrainingSession> sessions : timetable.get(dayOfWeek).values()) {
            result.addAll(sessions);
        }

        return result;
    }

    // O(log n)
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetable.containsKey(dayOfWeek)) {
            return new ArrayList<>();
        }

        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counter = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    counter.put(coach, counter.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : counter.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));

        return result;
    }
}