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
                            User user = User.builder()
                                    .setName(parts[0])
                                    .setPassword(parts[1])
                                    .setEmail(parts[2])
                                    .build();
                            userList.add(user);
                            addedCount++;
                            System.out.println("✓ Пользователь " + user.getName() + " добавлен");
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
                    userList.addAll(generatedUsers);
                    System.out.println("✓ Добавлено " + generatedUsers.size() + " случайных пользователей");
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
                        for (int i = 0; i < userList.size(); i++) {
                            User u = userList.get(i);
                            System.out.printf("%d. Имя: %-15s | Email: %-25s | Пароль: %s%n",
                                    i + 1,
                                    u.getName(),
                                    u.getEmail(),
                                    "*".repeat(u.getPassword().length()));
                        }
                    }
                    break;

                case 5:
                    if (userList.isEmpty()) {
                        System.out.println("Сортировать нечего");
                    } else {
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
                        System.out.println("✓ Список отсортирован!");

                        System.out.println("Первые 20 элементов после сортировки:");
                        for (int i = 0; i < Math.min(20, userList.size()); i++) {
                            System.out.println((i + 1) + ". " + userList.get(i));
                        }
                    }
                    break;

                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }
}