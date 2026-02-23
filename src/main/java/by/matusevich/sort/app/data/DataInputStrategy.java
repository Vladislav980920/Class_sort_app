package by.matusevich.sort.app.data;

import by.matusevich.sort.app.model.User;

import java.util.List;

public interface DataInputStrategy {
    List<User> getUsers();
    String getDescription();
}
