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

    public List<User> insertionSort(List<User> users) {
        
        for (int i = 1; i < users.size(); i++) {
            User key = users.get(i);
            int j = i - 1;
            while (j >= 0 && strategy.compare(users.get(j), key) > 0) {
                users.set(j + 1, users.get(j));
                j--;
            }
            users.set(j + 1, key);
        }

        return users;
    }
}
