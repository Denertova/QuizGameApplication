package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class UserLogin extends JFrame {
    // GUI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel messageLabel;

    // User data
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser;

    public static void main(String[] args) {

        UserLogin userLogin = new UserLogin();
        userLogin.login();
        // Load users from file
        loadUsersFromFile();

        SwingUtilities.invokeLater(() -> {
            userLogin.setVisible(true);
        });
    }

    public UserLogin() {
        super("User Login");

        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");
        messageLabel = new JLabel();

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(signupButton);

        add(formPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        // Register action listeners
        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> signup());
    }

    // Signup method
    private void signup() {
        String username = usernameField.getText();

        if (users.containsKey(username)) {
            messageLabel.setText("Username already exists. Please choose a different username.");
        } else {
            User user = new User();
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.saveUser(users);
            users.put(username, user);
            currentUser = user;
            messageLabel.setText("Signup successful!");

            saveUsersToFile();
        }
    }
    // Login method
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User loadedUser = users.get(username);

        if (loadedUser != null && loadedUser.getPassword().equals(password)) {
            currentUser = loadedUser;

            // Display user information in a separate window
            JFrame userInfoFrame = new JFrame("User Information");
            userInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userInfoFrame.setSize(300, 200);
            userInfoFrame.setLocationRelativeTo(this);

            JTextArea userInfoTextArea = new JTextArea();
            userInfoTextArea.setEditable(false);
            userInfoTextArea.append("Username: " + currentUser.getUsername() + "\n");
            userInfoTextArea.append("Score: " + currentUser.getScore() + "\n");
            userInfoTextArea.append("Correct Answers: " + currentUser.getCorrectAnswers() + "\n");
            userInfoTextArea.append("Incorrect Answers: " + currentUser.getIncorrectAnswers() + "\n");
            userInfoTextArea.append("Open ended Answers: " + currentUser.getOpenEnded() + "\n");
            JScrollPane scrollPane = new JScrollPane(userInfoTextArea);

            userInfoFrame.getContentPane().add(scrollPane);
            userInfoFrame.setVisible(true);
            // End of displaying user information

            messageLabel.setText("Login successful!");

            UserSession.setCurrentUser(currentUser);
            saveUsersToFile();
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    // Load users from file
    private static void loadUsersFromFile() {
        String filepath = "src/main/java/org/example/users/" + currentUser.username + ".txt";
        File file = new File(filepath);

        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(filepath);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                Object object = objectInputStream.readObject();
                if (object instanceof Map<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Map<String, User> loadedUsers = (Map<String, User>) object;
                    users = loadedUsers;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading users: " + e.getMessage());
            }
        }
    }
    // Save users to file
    public static void saveUsersToFile() {
        String filepath = "src/main/java/org/example/users/" + currentUser.username + ".txt";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filepath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static class User implements java.io.Serializable {

        private String username;
        private String password;
        private int score;
        private int correctAnswers;
        private int incorrectAnswers;
        private String openEnded;

        public User() {
            this.username = "";
            this.password = "";
            this.score = 0;
            this.correctAnswers = 0;
            this.incorrectAnswers = 0;
            this.openEnded = "";
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getScore() {
            return score;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public int getIncorrectAnswers() {
            return incorrectAnswers;
        }

        public String getOpenEnded() {
            return openEnded;
        }
        public String setOpenEnded(String OpenEnded) {
            this.openEnded = openEnded;
            return OpenEnded;
        }

        public void incrementScore() {
            score++;
        }

        public void incrementCorrectAnswers() {
            correctAnswers++;
        }

        public void incrementIncorrectAnswers() {
            incorrectAnswers++;
        }

        public void saveUser(Map<String, User> users) {
            users.put(username, this);
        }
    }

    public static class UserSession {
        private static UserLogin.User currentUser;

        public static UserLogin.User getCurrentUser() {
            return currentUser;
        }

        public static void setCurrentUser(UserLogin.User user) {
            currentUser = user;
        }
    }
}
