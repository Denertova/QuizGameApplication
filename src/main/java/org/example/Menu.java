package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = kit.getScreenSize();
    private final int screenHeight = screenSize.height;
    private final int screenWidth = screenSize.width;
    private JPanel topPanel;

    // ActionListener for menu items
    private final ActionListener defaultAction = actionEvent -> {
        JMenuItem temp = (JMenuItem) actionEvent.getSource();
        System.out.println(temp.getText());

        // Handle different menu item actions
        if (temp.getText().equals("Fill-in gaps")) {
            // Handle Fill-in Gaps Quiz
            // Check if user is logged in
            UserLogin.User currentUser = UserLogin.UserSession.getCurrentUser();
            if (currentUser != null) {
                EventQueue.invokeLater(() -> {
                    FillInGapsQuiz quiz = new FillInGapsQuiz(currentUser);
                    quiz.setVisible(true);
                });
                dispose(); // Close the menu window
            } else {
                // Display an error message or handle the case when currentUser is null
                System.out.println("Error: User not logged in.");
                JOptionPane.showMessageDialog(this, "Please log in to the system :)", "Logged Out", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (temp.getText().equals("Matching question")) {
            // Handle Matching Question Quiz
            // Check if user is logged in
            UserLogin.User currentUser = UserLogin.UserSession.getCurrentUser();
            if (currentUser != null) {
                EventQueue.invokeLater(() -> {
                    MatchingQuestionsQuiz quiz = new MatchingQuestionsQuiz(currentUser);
                    quiz.setVisible(true);
                });
                dispose(); // Close the menu window
            } else {
                // Display an error message or handle the case when currentUser is null
                System.out.println("Error: User not logged in.");
                JOptionPane.showMessageDialog(this, "Please log in to the system :)", "Logged Out", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (temp.getText().equals("Multiple choice")) {
            // Handle Multiple Choice Quiz
            // Check if user is logged in
            UserLogin.User currentUser = UserLogin.UserSession.getCurrentUser();
            if (currentUser != null) {
                EventQueue.invokeLater(() -> {
                    MultipleChoiceQuiz quiz = new MultipleChoiceQuiz(currentUser);
                    quiz.setVisible(true);
                });
                dispose(); // Close the menu window
            } else {
                // Display an error message or handle the case when currentUser is null
                System.out.println("Error: User not logged in.");
                JOptionPane.showMessageDialog(this, "Please log in to the system :)", "Logged Out", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (temp.getText().equals("Open ended question")) {
            // Handle Open-Ended Question Quiz
            // Check if user is logged in
            UserLogin.User currentUser = UserLogin.UserSession.getCurrentUser();
            if (currentUser != null) {
                EventQueue.invokeLater(() -> {
                    OpenEnded quiz = new OpenEnded(currentUser);
                    quiz.setVisible(true);
                });
                dispose(); // Close the menu window
            } else {
                // Display an error message or handle the case when currentUser is null
                System.out.println("Error: User not logged in.");
                JOptionPane.showMessageDialog(this, "Please log in to the system :)", "Logged Out", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    // Colors
    private final Color menuTextColor = new Color(255, 255, 255);
    private final Color menuBackgroundColor = new Color(0, 187, 94);
    private final Color menuBorderColor = new Color(0, 112, 55);
    private final Color buttonBackgroundColor = new Color(0, 89, 255);
    public JMenuItem createItem(String name, char mnemonic) {
        JMenuItem temp;
        if (mnemonic != ' ') {
            temp = new JMenuItem(name, mnemonic);
        } else {
            temp = new JMenuItem(name);
        }
        temp.addActionListener(defaultAction);
        return temp;
    }

    public Menu() {
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setOpaque(false);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton createQuizButton = createButton("Create a quiz");
        createQuizButton.setBackground(buttonBackgroundColor);
        createQuizButton.setForeground(Color.white);
        createQuizButton.addActionListener(e -> {
            // Handle Create Quiz button action
            SwingUtilities.invokeLater(() -> {
                QuizCreator quizCreator = new QuizCreator();
                quizCreator.setVisible(true);
            });
        });

        buttonPanel.add(createQuizButton);

        JButton browseButton = createButton("Browse");
        browseButton.setBackground(buttonBackgroundColor);
        browseButton.setForeground(Color.white);
        browseButton.addActionListener(e -> {
            // Handle Browse button action
            SwingUtilities.invokeLater(() -> {
                String directory = "src/main/java/org/example/Jsonfiles";
                QuizFileBrowser fileBrowser = new QuizFileBrowser(directory);
                fileBrowser.setVisible(true);
            });
        });

        buttonPanel.add(browseButton);
        add(buttonPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(menuBackgroundColor);
        menuBar.setBorder(BorderFactory.createLineBorder(menuBorderColor));
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(200, 40));

        JMenu quizTypeMenu = new JMenu("Quiz Type");
        quizTypeMenu.setForeground(menuTextColor);
        quizTypeMenu.setFont(new Font("Arial", Font.BOLD, 14));
        quizTypeMenu.setOpaque(true);
        quizTypeMenu.setBackground(menuBackgroundColor);
        quizTypeMenu.setBorderPainted(false);

        JMenuItem closeQuestionItem = createItem("Fill-in gaps", ' ');
        JMenuItem matchingQuestionItem = createItem("Matching question", ' ');
        JMenuItem multipleChoiceQuestionItem = createItem("Multiple choice", ' ');
        JMenuItem openEndedQuestionItem = createItem("Open ended question", ' ');

        quizTypeMenu.add(closeQuestionItem);
        quizTypeMenu.add(matchingQuestionItem);
        quizTypeMenu.add(multipleChoiceQuestionItem);
        quizTypeMenu.add(openEndedQuestionItem);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(quizTypeMenu);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(Box.createHorizontalGlue());

        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        menuPanel.add(menuBar);

        add(menuPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationRelativeTo(null);
        setTitle("QuizApp");
        setVisible(true);

        JButton userLoginButton = createUserLoginButton();
        userLoginButton.setBackground(menuBorderColor);
        userLoginButton.setForeground(Color.white);
        buttonPanel.add(userLoginButton);
    }

    private JButton createUserLoginButton() {
        JButton button = createButton("User Login");

        button.addActionListener(e -> {
            // Handle User Login button action
            UserLogin userLogin = new UserLogin();
            userLogin.setVisible(true);
        });

        return button;
    }

    private JButton createButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setSize(800, 500);
            menu.setVisible(true);
        });
    }

    static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = Toolkit.getDefaultToolkit().createImage("src/main/java/org/example/images/background.jpg");
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
