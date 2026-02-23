package by.matusevich.sort.app;

import by.matusevich.sort.app.data.*;
import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.SortingService;
import by.matusevich.sort.app.service.UserInputService;
import by.matusevich.sort.app.strategy.*;
import by.matusevich.sort.app.util.FileUtils;
import by.matusevich.sort.app.util.RandomGeneratorUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        UserInputService input = new UserInputService();
        int operation;

        while (true) {
            operation = input.readInt("Выберите операцию\n1-Заполнение из файла\n2-Заполнение рандомом\n3-Заполнение вручную\n4-список пользователей\n5-Сортировка\n0-Выход из программы\n");
            if (operation == 0)
                break;
            }

            switch (operation) {
                case 1:
                    List<String[]> lines = FileUtils.readAndValidateFile("Указать путь файла");
                    for(String[] parts: lines){
                        User user = User.builder()
                                .setName(parts[0])
                                .setPassword(parts[1])
                                .setEmail(parts[2])
                                .build();
                        userList.add(user);
                        System.out.println("Пользователь " + user.getName() + " добавлен");
                    }
                    System.out.println("Загружено " + addedCount + " пользователей из файла");
                    break;

                case 2:
                    String nameRand = RandomGeneratorUtils.generateName();
                    String passwordRand = RandomGeneratorUtils.generatePassword();
                    String emailRand = RandomGeneratorUtils.generateEmail();
                    User userRand = User.builder()
                            .setName(nameRand)
                            .setPassword(passwordRand)
                            .setEmail(emailRand)
                            .build();
                    userList.add(userRand);
                    System.out.println("Пользователь " + userRand.getName() + " сгенерирован");
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
                        System.out.println(user.getName() + " создан");
                    } else {
                        UserValidator.validateUser(user);
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
                    else {
                        int typeOfStrategy;
                        SortingService service = new SortingService();
                        int typeOfSort = input.readInt("Выберите способ сортировки\n1-По имени \n2-По паролю \n3-По почте \n");
                        switch (typeOfSort) {
                            case 1:
                                typeOfStrategy = input.readInt("1-По возрастанию \n2-По убыванию\n");
                                switch (typeOfStrategy){
                                    case 1:
                                        service.setStrategy(new PasswordLengthByAscStrategy());
                                        service.insertionSort(userList);
                                        break;
                                    case 2:
                                        service.setStrategy(new PasswordLengthByDescStrategy());
                                        service.insertionSort(userList);
                                }
                                break;
                            case 2:
                                typeOfStrategy = input.readInt("1-По возрастанию \n2-По убыванию\n");
                                switch (typeOfStrategy){
                                    case 1:
                                        service.setStrategy(new EmailByAscStrategy());
                                        service.insertionSort(userList);
                                        break;
                                    case 2:
                                        service.setStrategy(new EmailByDescStrategy());
                                        service.insertionSort(userList);
                                }
                                break;
                            case 3:
                                typeOfStrategy = input.readInt("1-По возрастанию \n2-По убыванию\n");
                                switch (typeOfStrategy){
                                    case 1:
                                        service.setStrategy(new FirstNameByAscStrategy());
                                        service.insertionSort(userList);
                                        break;
                                    case 2:
                                        service.setStrategy(new FirstNameByDescStrategy());
                                        service.insertionSort(userList);
                                }
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