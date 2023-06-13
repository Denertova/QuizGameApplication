package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;


public class MatchingQuestionsQuiz extends JFrame {

    private JComboBox<String> leftComboBox;
    private JComboBox<String> rightComboBox;
    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = kit.getScreenSize();

    private final int screenHeight = screenSize.height;
    private final int screenWidth = screenSize.width;

    private final Color menuTextColor = Color.WHITE;
    private final Color menuBackgroundColor = new Color(0, 187, 94);

    private UserLogin.User currentUser;
    private QuizDataMatching quizData;


    private DifficultyPanel difficultyPanel;
    private JPanel quizPanel;

    public JMenuItem createItem(String name, char mnemonic, ActionListener actionListener) {
        JMenuItem temp;
        if (mnemonic != ' ') {
            temp = new JMenuItem(name, mnemonic);
        } else {
            temp = new JMenuItem(name);
        }
        temp.addActionListener(actionListener);
        return temp;
    }

    public MatchingQuestionsQuiz(UserLogin.User currentUser) {
        this.currentUser = currentUser;
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());
        difficultyPanel = new DifficultyPanel(new StartQuizButtonListener());
        add(difficultyPanel, BorderLayout.CENTER);
        setSize(screenWidth / 3, screenHeight / 3);
        setLocationRelativeTo(null);
        setTitle("QuizApp - Difficulty");
        setVisible(true);
        JPanel mainPanel = new JPanel(new BorderLayout());

        difficultyPanel = new DifficultyPanel(new StartQuizButtonListener());
        mainPanel.add(difficultyPanel, BorderLayout.CENTER);

        // Create and add the BackToMenuPanel to the mainPanel
        BackToMenuPanel backToMenuPanel = new BackToMenuPanel();
        mainPanel.add(backToMenuPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        class BackgroundPanel extends JPanel {
            private Image backgroundImage;

            public BackgroundPanel() {
                String imagePath = "D:/QuizApp/QuizApp/src/main/java/org/example/images/background.jpg";
                backgroundImage = new ImageIcon(imagePath).getImage();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private class StartQuizButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String difficulty = difficultyPanel.getSelectedDifficulty();

            // Load and process the JSON file
            String filePath = "src/main/java/org/example/Jsonfiles/Matching-question/Matching-Questions-" + difficulty.toLowerCase() + ".json";
            try {
                Gson gson = new Gson();
                quizData = gson.fromJson(new FileReader(filePath), QuizDataMatching.class);
                showQuiz();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            difficultyPanel.setVisible(false);
        }
    }

    private void showQuiz() {
        try {
            List<QuizDataMatching.Interaction> interactions = quizData.getInteractions();
            List<String> leftOptions = new ArrayList<>();
            List<String> rightOptions = new ArrayList<>();
            initializeUI(); // Move the initialization here
            updateUI(leftOptions, rightOptions);

            if (interactions.size() > 0) {
                QuizDataMatching.Interaction interaction = interactions.get(0);
                List<QuizDataMatching.Pair> pairs = interaction.getPairs();

                leftOptions = new ArrayList<>();
                rightOptions = new ArrayList<>();

                for (QuizDataMatching.Pair pair : pairs) {
                    leftOptions.add(pair.getLeft());
                    rightOptions.add(pair.getRight());
                }

                updateUI(leftOptions, rightOptions);
            } else {
                System.out.println("No questions found in the JSON file.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    class BackToMenuPanel extends JPanel {

        public BackToMenuPanel() {
            setLayout(new BorderLayout());

            JButton backButton = new JButton("Back to Menu");
            backButton.addActionListener(new BackToMenuButtonListener());

            add(backButton, BorderLayout.WEST);
        }

        class BackToMenuButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Forward the user to the menu class in another file
                Menu.main(null); // Assuming the menu class has a main method to start the menu

            }
        }
    }

    private void initializeUI() {
        quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(quizData.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quizPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel questionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        leftComboBox = new JComboBox<>();
        rightComboBox = new JComboBox<>();
        questionPanel.add(leftComboBox);
        questionPanel.add(rightComboBox);

        quizPanel.add(questionPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        submitButton.setBackground(Color.PINK);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        quizPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(quizPanel, BorderLayout.CENTER);
    }

    private void updateUI(List<String> leftOptions, List<String> rightOptions) {
        DefaultComboBoxModel<String> leftModel = new DefaultComboBoxModel<>(leftOptions.toArray(new String[0]));
        leftComboBox.setModel(leftModel);

        DefaultComboBoxModel<String> rightModel = new DefaultComboBoxModel<>(rightOptions.toArray(new String[0]));
        rightComboBox.setModel(rightModel);

        // Customize the UI of the JComboBox
        leftComboBox.setRenderer(new CustomComboBoxRenderer());
        leftComboBox.setPreferredSize(new Dimension(screenWidth / 4, screenHeight / 2));

        rightComboBox.setRenderer(new CustomComboBoxRenderer());
        rightComboBox.setPreferredSize(new Dimension(screenWidth / 4, screenHeight / 2));
    }

    // Custom renderer class for JComboBox
    class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Customize the appearance of the JComboBox items
            if (renderer instanceof JLabel) {
                JLabel label = (JLabel) renderer;
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setForeground(Color.darkGray);
            }

            return renderer;
        }
    }

    private class SubmitButtonListener implements ActionListener {
        private int attempts;
        private int score;
        private List<String> selectedPairs;

        public SubmitButtonListener() {
            attempts = 0;
            score = 0;
            selectedPairs = new ArrayList<>();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String leftOption = (String) leftComboBox.getSelectedItem();
            String rightOption = (String) rightComboBox.getSelectedItem();

            if (leftOption != null && rightOption != null && !selectedPairs.contains(leftOption + rightOption)) {
                submitAnswer(leftOption, rightOption);
                attempts++;

                if (attempts == 3) {
                    showScore();
                }

                selectedPairs.add(leftOption + rightOption);
            }
        }

        private void submitAnswer(String leftOption, String rightOption) {
            boolean isCorrect = false;

            for (QuizDataMatching.Interaction interaction : quizData.getInteractions()) {
                List<QuizDataMatching.Pair> pairs = interaction.getPairs();
                for (QuizDataMatching.Pair pair : pairs) {
                    if (pair.getLeft().equals(leftOption) && pair.getRight().equals(rightOption)) {
                        isCorrect = true;
                        break;
                    }
                }
            }

            if (isCorrect) {
                score++;
                System.out.println("Correct answer: " + leftOption + " - " + rightOption);
            } else {
                System.out.println("Incorrect answer: " + leftOption + " - " + rightOption);
            }
        }

        private void showScore() {
            JOptionPane.showMessageDialog(quizPanel, "Your score: " + score + " out of 3", "Score", JOptionPane.INFORMATION_MESSAGE);
            int incorrectAnswers = 3 - score;
            int correctAnswers = score;
            // Increment the score and update correct/incorrect answers
            currentUser.incrementScore();
            currentUser.incrementCorrectAnswers();
            currentUser.incrementIncorrectAnswers();
        }
    }

    public static void main(String[] args) {
        UserLogin.User currentUser = new UserLogin.User();
        new MatchingQuestionsQuiz(currentUser);
    }


class DifficultyPanel extends JPanel {

    private JComboBox<String> difficultyComboBox;
    private JButton startButton;

    private ActionListener startButtonListener;

    public DifficultyPanel(ActionListener startButtonListener) {
        this.startButtonListener = startButtonListener;
        initializeUI();

        class BackToMenuPanel extends JPanel {

            public BackToMenuPanel() {
                setLayout(new BorderLayout());

                JButton backButton = new JButton("Back to Menu");
                backButton.addActionListener(new BackToMenuButtonListener());

                add(backButton, BorderLayout.CENTER);
            }

            class BackToMenuButtonListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EventQueue.invokeLater(Menu::new);
                    MatchingQuestionsQuiz.this.dispose();
                }
            }
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Difficulty");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyComboBox.setForeground(menuTextColor);
        difficultyComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyComboBox.setOpaque(true);
        difficultyComboBox.setBackground(menuBackgroundColor);

        startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.addActionListener(startButtonListener);
        startButton.setForeground(menuTextColor);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setOpaque(true);
        startButton.setBackground(Color.BLUE);
        startButton.setBorderPainted(false);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        contentPanel.add(titleLabel, gbc);
        contentPanel.add(difficultyComboBox, gbc);
        contentPanel.add(startButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    public String getSelectedDifficulty() {
        return (String) difficultyComboBox.getSelectedItem();
    }
}
class QuizDataMatching {
    private String difficulty;
    private String title;
    private String prompt;
    private String category;
    private List<Interaction> interactions;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    public int getInteractionsSize() {
        return interactions != null ? interactions.size() : 0;
    }

    @Override
    public String toString() {
        return "QuizData{" +
                "difficulty='" + difficulty + '\'' +
                ", title='" + title + '\'' +
                ", prompt='" + prompt + '\'' +
                ", category='" + category + '\'' +
                ", interactions=" + interactions +
                '}';
    }

    static class Interaction {
        private List<Pair> pairs;

        public List<Pair> getPairs() {
            return pairs;
        }

        public void setPairs(List<Pair> pairs) {
            this.pairs = pairs;
        }

        @Override
        public String toString() {
            return "Interaction{" +
                    "pairs=" + pairs +
                    '}';
        }
    }

    static class Pair {
        private String left;
        private String right;

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "left='" + left + '\'' +
                    ", right='" + right + '\'' +
                    '}';
            }
        }
    }
}
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        String imagePath = "src/main/java/org/example/images/background.jpg";
        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}