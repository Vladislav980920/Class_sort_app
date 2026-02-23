package by.matusevich.sort.app;

import by.matusevich.sort.app.data.*;
import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.SortingService;
import by.matusevich.sort.app.service.UserInputService;
import by.matusevich.sort.app.strategy.*;
import by.matusevich.sort.app.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        UserInputService input = new UserInputService();
        int operation;

        while (true) {
            operation = input.readInt("""
                   \s
                    === МЕНЮ ===
                    1 - Заполнение из файла
                    2 - Заполнение рандом
                    3 - Заполнение вручную
                    4 - Список пользователей
                    5 - Сортировка
                    6 - Статистика
                    7 - Очистить список
                    0 - Выход из программы
                    Выбор:\s""");

            if (operation == 0) {
                input.close();
                System.out.println("Программа завершена");
                break;
            }

            switch (operation) {
                case 1:
                    String filePath = input.readString("Введите путь к файлу: ");
                    List<String[]> lines = FileUtils.readAndValidateFile(filePath);
                    int addedCount = 0;
                    for (String[] parts : lines) {
                        try {
                            UserValidator.validateBeforeBuild(parts[0], parts[1], parts[2]);

                            User user = User.builder()
                                    .setName(parts[0])
                                    .setPassword(parts[1])
                                    .setEmail(parts[2])
                                    .build();
                            userList.add(user);
                            addedCount++;
                            System.out.println("✓ Пользователь " + user.getName() + " добавлен");
                        } catch (IllegalArgumentException e) {
                            System.out.println("✗ Ошибка валидации: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("✗ Ошибка при создании пользователя: " + e.getMessage());
                        }
                    }
                    System.out.println("Загружено " + addedCount + " пользователей из файла");
                    break;

                case 2:
                    int count = input.readInt("Сколько пользователей сгенерировать? ");
                    RandomInputStrategy randomStrategy = new RandomInputStrategy(new UserDataGenerator());
                    List<User> generatedUsers = randomStrategy.generateUsers(count);

                    int validCount = 0;
                    for (User user : generatedUsers) {
                        try {
                            UserValidator.validateUser(user);
                            userList.add(user);
                            validCount++;
                        } catch (IllegalArgumentException e) {
                            System.out.println("✗ Сгенерирован некорректный пользователь: " + e.getMessage());
                        }
                    }

                    System.out.println("✓ Добавлено " + validCount + " случайных пользователей");
                    if (validCount < generatedUsers.size()) {
                        System.out.println("⚠ Пропущено " + (generatedUsers.size() - validCount) + " некорректных записей");
                    }
                    break;

                case 3:
                    String name;
                    do {
                        name = input.readString("Введите имя пользователя (2-50 символов, буквы): ");
                        if (!UserValidator.validateName(name)) {
                            System.out.println("✗ Недопустимое имя! Попробуйте снова.");
                        }
                    } while (!UserValidator.validateName(name));

                    String password;
                    do {
                        password = input.readString("Введите пароль (мин. 6 символов, заглавная буква и цифра): ");
                        if (!UserValidator.validatePassword(password)) {
                            System.out.println("✗ Недопустимый пароль! Попробуйте снова.");
                        }
                    } while (!UserValidator.validatePassword(password));

                    String email;
                    do {
                        email = input.readString("Введите почту: ");
                        if (!UserValidator.validateEmail(email)) {
                            System.out.println("✗ Недопустимый email! Попробуйте снова.");
                        }
                    } while (!UserValidator.validateEmail(email));

                    try {
                        User user = User.builder()
                                .setName(name)
                                .setPassword(password)
                                .setEmail(email)
                                .build();
                        userList.add(user);
                        System.out.println("✓ Пользователь " + user.getName() + " создан");
                    } catch (Exception e) {
                        System.out.println("✗ Ошибка создания пользователя: " + e.getMessage());
                    }
                    break;

                case 4:
                    if (userList.isEmpty()) {
                        System.out.println("Список пользователей пуст");
                    } else {
                        System.out.println("\n=== СПИСОК ПОЛЬЗОВАТЕЛЕЙ (" + userList.size() + ") ===");
                        System.out.println("№  | Имя                | Email                          | Пароль");
                        System.out.println("---|--------------------|--------------------------------|--------");

                        for (int i = 0; i < userList.size(); i++) {
                            User u = userList.get(i);
                            System.out.printf("%-3d| %-18s | %-30s | %s%n",
                                    i + 1,
                                    truncate(u.getName(), 18),
                                    truncate(u.getEmail(), 30),
                                    "*".repeat(Math.min(u.getPassword().length(), 10)));
                        }
                    }
                    break;

                case 5:
                    if (userList.isEmpty()) {
                        System.out.println("Сортировать нечего");
                    } else {
                        List<User> beforeSort = new ArrayList<>(userList);

                        int fieldChoice = input.readInt("""
                                Выберите поле для сортировки:
                                1 - По имени
                                2 - По паролю (лексикографически)
                                3 - По длине пароля
                                4 - По почте
                                Выбор:\s""");

                        int directionChoice = input.readInt("""
                                Выберите направление:
                                1 - По возрастанию
                                2 - По убыванию
                                Выбор:\s""");

                        SortingService sortingService = new SortingService();

                        try {
                            switch (fieldChoice) {
                                case 1:
                                    if (directionChoice == 1) {
                                        sortingService.setStrategy(new FirstNameByAscStrategy());
                                    } else {
                                        sortingService.setStrategy(new FirstNameByDescStrategy());
                                    }
                                    break;
                                case 2:
                                    if (directionChoice == 1) {
                                        sortingService.setStrategy(new PasswordByAscStrategy());
                                    } else {
                                        sortingService.setStrategy(new PasswordByDescStrategy());
                                    }
                                    break;
                                case 3:
                                    if (directionChoice == 1) {
                                        sortingService.setStrategy(new PasswordLengthByAscStrategy());
                                    } else {
                                        sortingService.setStrategy(new PasswordLengthByDescStrategy());
                                    }
                                    break;
                                case 4:
                                    if (directionChoice == 1) {
                                        sortingService.setStrategy(new EmailByAscStrategy());
                                    } else {
                                        sortingService.setStrategy(new EmailByDescStrategy());
                                    }
                                    break;
                                default:
                                    System.out.println("Неверный выбор поля!");
                                    break;
                            }

                            userList = sortingService.insertionSort(userList);

                            System.out.println("\n✓ Список отсортирован!");

                            System.out.println("\nПервые 10 элементов после сортировки:");
                            for (int i = 0; i < Math.min(10, userList.size()); i++) {
                                User u = userList.get(i);
                                String value;
                                switch (fieldChoice) {
                                    case 1: value = "Имя: " + u.getName(); break;
                                    case 2: value = "Пароль: " + u.getPassword(); break;
                                    case 3: value = "Длина пароля: " + u.getPassword().length(); break;
                                    case 4: value = "Email: " + u.getEmail(); break;
                                    default: value = u.toString();
                                }
                                System.out.printf("%3d. %s%n", i + 1, value);
                            }

                        } catch (Exception e) {
                            System.out.println("✗ Ошибка при сортировке: " + e.getMessage());
                        }
                    }
                    break;

                case 6:
                    if (userList.isEmpty()) {
                        System.out.println("Нет данных для статистики");
                    } else {
                        printStatistics(userList);
                    }
                    break;

                case 7:
                    userList.clear();
                    System.out.println("✓ Список очищен");
                    break;

                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }

    private static void printStatistics(List<User> users) {
        System.out.println("\n=== СТАТИСТИКА ===");
        System.out.println("Всего пользователей: " + users.size());

        long uniqueNames = users.stream()
                .map(User::getName)
                .distinct()
                .count();
        System.out.println("Уникальных имен: " + uniqueNames);

        double avgPasswordLength = users.stream()
                .mapToInt(u -> u.getPassword().length())
                .average()
                .orElse(0);
        System.out.printf("Средняя длина пароля: %.2f%n", avgPasswordLength);

        System.out.println("\nПочтовые домены:");
        users.stream()
                .map(u -> u.getEmail().substring(u.getEmail().indexOf("@") + 1))
                .distinct()
                .sorted()
                .forEach(domain -> {
                    long count = users.stream()
                            .filter(u -> u.getEmail().endsWith(domain))
                            .count();
                    System.out.printf("  %-15s: %d пользователей%n", domain, count);
                });

        System.out.println("\nСамое популярное имя:");
        users.stream()
                .map(User::getName)
                .collect(java.util.stream.Collectors.groupingBy(
                        name -> name,
                        java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .ifPresent(entry ->
                        System.out.printf("  %s (%d раз)%n", entry.getKey(), entry.getValue()));
    }
}