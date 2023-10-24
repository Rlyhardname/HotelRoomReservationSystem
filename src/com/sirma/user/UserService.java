package com.sirma.user;


import java.io.*;
import java.nio.file.Path;

public class UserService {
    public static void saveToFile(User user) {
        Path path = Path.of("resources\\users\\"+ user.getUsername() + ".txt");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toString()))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static User readFromFile(String user) {
        Path path = Path.of("resources\\users\\"+ user + ".txt");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
            return (User) ois.readObject();

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
