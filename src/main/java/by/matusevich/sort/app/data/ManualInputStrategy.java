package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.UserInputService;

import java.util.ArrayList;
import java.util.List;

public class ManualInputStrategy implements DataInputStrategy {
    private final UserInputService inputService;

    public ManualInputStrategy(UserInputService inputService) {
        this.inputService = inputService;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        int count = inputService.readInt("Сколько пользователей ввести? ");

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Пользователь #" + (i + 1) + " ---");

            String name;
            do {
                name = inputService.readString("Введите имя (2-50 символов, только буквы): ");
                if (!UserValidator.validateName(name)) {
                    System.out.println("Ошибка: недопустимое имя!");
                }
            } while (!UserValidator.validateName(name));

            String password;
            do {
                password = inputService.readString("Введите пароль (мин. 6 символов, заглавная буква и цифра): ");
                if (!UserValidator.validatePassword(password)) {
                    System.out.println("Ошибка: недопустимый пароль!");
                }
            } while (!UserValidator.validatePassword(password));

            String email;
            do {
                email = inputService.readString("Введите email: ");
                if (!UserValidator.validateEmail(email)) {
                    System.out.println("Ошибка: недопустимый email!");
                }
            } while (!UserValidator.validateEmail(email));

            try {
                User user = User.builder()
                        .setName(name)
                        .setPassword(password)
                        .setEmail(email)
                        .build();
                users.add(user);
                System.out.println("✓ Пользователь добавлен: " + user.getName());
            } catch (IllegalStateException e) {
                System.out.println("✗ Ошибка создания пользователя: " + e.getMessage());
            }
        }

        return users;
    }

    @Override
    public String getDescription() {
        return "Ручной ввод с консоли";
    }
}
