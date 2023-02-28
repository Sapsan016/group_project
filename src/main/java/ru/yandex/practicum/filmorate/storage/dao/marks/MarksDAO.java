package ru.yandex.practicum.filmorate.storage.dao.marks;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.MarksStorage;

@Component
@RequiredArgsConstructor
public class MarksDAO implements MarksStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addMark(Integer id, Integer userId, Integer mark) {
        String sqlQuery = "insert into marks(FILM_ID, USER_ID, MARK_VALUE) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId, mark);
        setRate(id);
    }



    @Override
    public void removeMark(Integer id, Integer userId) {
        String sqlQuery = "DELETE FROM MARKS WHERE FILM_ID = ? and USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, id, userId) < 1) {
            throw new ObjectNotFoundException(String.format("Пользователь с id=%d не ставил оценку фильму с id=%d.", userId, id));
        }
        setRate(id);
    }

    private void setRate(Integer filmId) {
        String sqlQuery = "update FILMS f set rate = (select (sum(MARK_VALUE) / count(m.user_id)) " +
                "from MARKS m where m.film_id = f.film_id) where film_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }
}
