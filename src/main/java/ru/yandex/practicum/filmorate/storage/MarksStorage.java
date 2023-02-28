package ru.yandex.practicum.filmorate.storage;

public interface MarksStorage {

    void addMark(Integer id, Integer userId, Integer mark);
    void removeMark(Integer id, Integer userId);
}
