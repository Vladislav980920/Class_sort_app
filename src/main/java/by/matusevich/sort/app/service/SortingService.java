package by.matusevich.sort.app.service;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.strategy.ComparingStrategy;

import java.util.ArrayList;
import java.util.List;

public class SortingService {
    private ComparingStrategy strategy;

    public void setStrategy(ComparingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия не может быть null");
        }
        this.strategy = strategy;
        System.out.println("✓ Стратегия установлена: " + strategy.getClass().getSimpleName());
    }

    public List<User> insertionSort(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Список пользователей не может быть null");
        }

        if (strategy == null) {
            System.out.println("✗ Ошибка: стратегия сортировки не установлена!");
            return new ArrayList<>(users);
        }

        List<User> sortedList = new ArrayList<>(users);

        if (sortedList.size() <= 1) {
            System.out.println("Список содержит 0 или 1 элемент, сортировка не требуется");
            return sortedList;
        }

        System.out.println("Сортировка вставками... Размер списка: " + sortedList.size());

        System.out.println("До сортировки (первые 3):");
        for (int i = 0; i < Math.min(3, sortedList.size()); i++) {
            User u = sortedList.get(i);
            System.out.printf("  %d. Имя: %-10s | Email: %-20s | Пароль: %s%n",
                    i + 1, u.getName(), u.getEmail(), "*".repeat(u.getPassword().length()));
        }

        for (int i = 1; i < sortedList.size(); i++) {
            User key = sortedList.get(i);
            int j = i - 1;

            while (j >= 0 && strategy.compare(sortedList.get(j), key) > 0) {
                sortedList.set(j + 1, sortedList.get(j));
                j--;
            }
            sortedList.set(j + 1, key);
        }

        System.out.println("После сортировки (первые 3):");
        for (int i = 0; i < Math.min(3, sortedList.size()); i++) {
            User u = sortedList.get(i);
            System.out.printf("  %d. Имя: %-10s | Email: %-20s | Пароль: %s%n",
                    i + 1, u.getName(), u.getEmail(), "*".repeat(u.getPassword().length()));
        }

        if (isSorted(sortedList)) {
            System.out.println("✓ Проверка пройдена: список действительно отсортирован");
        } else {
            System.out.println("⚠ Внимание: список может быть не полностью отсортирован!");
        }

        return sortedList;
    }

    /**
     * Проверяет, отсортирован ли список согласно текущей стратегии
     */
    private boolean isSorted(List<User> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (strategy.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    public ComparingStrategy getStrategy() {
        return strategy;
    }
}