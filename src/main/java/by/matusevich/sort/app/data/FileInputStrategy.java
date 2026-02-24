package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;
import by.matusevich.sort.app.model.UserValidator;
import by.matusevich.sort.app.service.UserInputService;
import by.matusevich.sort.app.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class FileInputStrategy implements DataInputStrategy {
    private final UserInputService inputService;

    public FileInputStrategy(UserInputService inputService) {
        this.inputService = inputService;
    }

    @Override
    public List<User> getUsers() {
        String filePath = inputService.readString("Введите путь к файлу: ");
        List<User> users = new ArrayList<>();

        try {
            List<String[]> dataLines = FileUtils.readAndValidateFile(filePath);

            if (dataLines.isEmpty()) {
                System.out.println("✗ Файл не содержит валидных данных");
                return users;
            }

            int validCount = 0;
            int invalidCount = 0;

            for (String[] parts : dataLines) {
                try {
                    UserValidator.validateBeforeBuild(parts[0], parts[1], parts[2]);

                    User user = User.builder()
                            .setName(parts[0])
                            .setPassword(parts[1])
                            .setEmail(parts[2])
                            .build();

                    users.add(user);
                    validCount++;
                } catch (IllegalArgumentException e) {
                    invalidCount++;
                    System.out.println("✗ Пропущена некорректная запись: " +
                            String.join(",", parts) + " - " + e.getMessage());
                }
            }

            System.out.println("✓ Загружено пользователей: " + validCount);
            if (invalidCount > 0) {
                System.out.println("⚠ Пропущено некорректных записей: " + invalidCount);
            }

        } catch (Exception e) {
            System.out.println("✗ Ошибка при чтении файла: " + e.getMessage());
        }

        return users;
    }

    @Override
    public String getDescription() {
        return "Загрузка из файла";
    }
}
