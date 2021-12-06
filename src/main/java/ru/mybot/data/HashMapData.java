package ru.mybot.data;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Repository
public class HashMapData implements BaseData {
    private Map<LocalDate, List<String>> localData = new HashMap<>();

    @Override
    public void save(LocalDate key, String deal) {
        if (localData.containsKey(key)) {
            ArrayList<String> alreadyExistDeals = new ArrayList<>(localData.get(key));
            alreadyExistDeals.add(deal);
            localData.put(key, alreadyExistDeals);
        } else {
            localData.put(key, asList(deal));
        }
    }

    @Override
    public List<String> selectAll(LocalDate key) {
        return localData.get(key);
    }
}
