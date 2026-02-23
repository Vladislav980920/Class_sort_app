package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDataGenerator {
    private static final Random random = new Random();

    private static final String[] FIRST_NAMES = {
            "Александр", "Дмитрий", "Максим", "Сергей", "Андрей",
            "Алексей", "Артём", "Илья", "Кирилл", "Михаил",
            "Никита", "Павел", "Роман", "Тимофей", "Юрий",
            "Анна", "Мария", "Елена", "Наталья", "Ольга",
            "Екатерина", "Татьяна", "Ирина", "Юлия", "Светлана"
    };

    private static final String[] EMAIL_DOMAINS = {
            "gmail.com", "yandex.ru", "mail.ru", "yahoo.com",
            "outlook.com", "inbox.ru", "list.ru", "bk.ru"
    };

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    public List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                User user = User.builder()
                        .setName(generateName())
                        .setPassword(generateStrongPassword())
                        .setEmail(generateEmail())
                        .build();
                users.add(user);
            } catch (Exception e) {
                System.err.println("Ошибка генерации пользователя #" + (i + 1) + ": " + e.getMessage());
            }
        }

        return users;
    }

    public String generateName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

    public String generateEmail() {
        String name = generateName().toLowerCase()
                .replace("ё", "e")
                .replace("й", "i")
                .replace("ь", "")
                .replace("ъ", "");

        name = name.replace("а", "a")
                .replace("б", "b")
                .replace("в", "v")
                .replace("г", "g")
                .replace("д", "d")
                .replace("е", "e")
                .replace("ж", "zh")
                .replace("з", "z")
                .replace("и", "i")
                .replace("к", "k")
                .replace("л", "l")
                .replace("м", "m")
                .replace("н", "n")
                .replace("о", "o")
                .replace("п", "p")
                .replace("р", "r")
                .replace("с", "s")
                .replace("т", "t")
                .replace("у", "u")
                .replace("ф", "f")
                .replace("х", "kh")
                .replace("ц", "ts")
                .replace("ч", "ch")
                .replace("ш", "sh")
                .replace("щ", "sch")
                .replace("ы", "y")
                .replace("э", "e")
                .replace("ю", "yu")
                .replace("я", "ya");

        int num = random.nextInt(999);
        String domain = EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];

        return name + num + "@" + domain;
    }

    public String generatePassword() {
        int length = 6 + random.nextInt(6); // длина от 6 до 12

        StringBuilder password = new StringBuilder();

        password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));

        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));

        for (int i = 2; i < length; i++) {
            String chars = LOWER_CASE + (random.nextBoolean() ? UPPER_CASE : "") +
                    (random.nextBoolean() ? DIGITS : "");
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        char[] array = password.toString().toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return new String(array);
    }

    public String generateStrongPassword() {
        int length = 8 + random.nextInt(5); // длина 8-13

        String upper = UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())) + "";
        String digit = DIGITS.charAt(random.nextInt(DIGITS.length())) + "";

        StringBuilder middle = new StringBuilder();
        for (int i = 0; i < length - 2; i++) {
            String chars = LOWER_CASE +
                    (random.nextInt(100) > 70 ? UPPER_CASE : "") +
                    (random.nextInt(100) > 70 ? DIGITS : "");
            middle.append(chars.charAt(random.nextInt(chars.length())));
        }

        String password = upper + middle + digit;

        List<Character> chars = new ArrayList<>();
        for (char c : password.toCharArray()) {
            chars.add(c);
        }
        java.util.Collections.shuffle(chars);

        StringBuilder result = new StringBuilder();
        for (char c : chars) {
            result.append(c);
        }

        return result.toString();
    }

    public User generateUser(String namePrefix, String emailDomain) {
        String name = namePrefix + "_" + random.nextInt(100);
        String email = name.toLowerCase() + "@" + emailDomain;
        String password = generateStrongPassword();

        return User.builder()
                .setName(name)
                .setPassword(password)
                .setEmail(email)
                .build();
    }

    public List<User> generateUsersWithVariety(int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String name;
            if (i % 3 == 0) {
                name = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            } else if (i % 3 == 1) {
                name = "User" + (100 + i);
            } else {
                name = "Тест" + (i + 1);
            }

            String domain = EMAIL_DOMAINS[i % EMAIL_DOMAINS.length];
            String email = name.toLowerCase().replace(" ", "") +
                    (100 + i) + "@" + domain;

            String password;
            if (i % 2 == 0) {
                password = generateStrongPassword();
            } else {
                password = "Pass" + (1000 + i) + "!";
            }

            try {
                User user = User.builder()
                        .setName(name)
                        .setPassword(password)
                        .setEmail(email)
                        .build();
                users.add(user);
            } catch (Exception e) {

            }
        }

        return users;
    }
}
