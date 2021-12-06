package ru.mybot.data;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BaseData {
    void save(LocalDate date, String deal);

    List<String> selectAll(LocalDate date);
}
