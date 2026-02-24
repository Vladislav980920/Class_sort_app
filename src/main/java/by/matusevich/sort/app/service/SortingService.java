package by.matusevich.sort.app.service;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.strategy.ComparingStrategy;

import java.util.ArrayList;
import java.util.List;

public class SortingService {
    private ComparingStrategy strategy;

    public void setStrategy(ComparingStrategy strategy) {
        this.strategy = strategy;
    }

    public List<User> shellSort(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        if (strategy == null) {
            throw new IllegalStateException("Стратегия сравнения не установлена. Вызовите setStrategy() перед сортировкой.");
        }

        List<User> sortedList = new ArrayList<>(users);
        int n = sortedList.size();

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                User temp = sortedList.get(i);
                int j;
                for (j = i; j >= gap && strategy.compare(sortedList.get(j - gap), temp) > 0; j -= gap) {
                    sortedList.set(j, sortedList.get(j - gap));
                }
                sortedList.set(j, temp);
            }
        }
        return sortedList;
    }
}