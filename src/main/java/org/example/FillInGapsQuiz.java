package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


public class FillInGapsQuiz extends JFrame {


    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = kit.getScreenSize();
    private final int screenHeight = screenSize.height;
    private final int screenWidth = screenSize.width;

    private final Color menuTextColor = Color.WHITE;
    private final Color menuBackgroundColor = new Color(0, 187, 94);
    private final Color menuBorderColor = new Color(0, 112, 55);
    private final Color buttonBackgroundColor = new Color(0, 89, 255);
    private final Color buttonTextColor = Color.WHITE;

    private QuizDataFill quizData;
    private JLabel titleLabel;
    private JTextArea stimulusTextArea;
    private ButtonGroup answerButtonGroup;
    private JPanel menuPanel;
    private UserLogin.User currentUser;


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

    public FillInGapsQuiz(UserLogin.User currentUser) {
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());
        this.currentUser = currentUser;
        // Create and configure the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(menuBackgroundColor);
        menuBar.setBorder(BorderFactory.createLineBorder(menuBorderColor));
        menuBar.setPreferredSize(new Dimension(200, 40));

        // Create and configure the difficulty menu
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setForeground(menuTextColor);
        difficultyMenu.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyMenu.setOpaque(true);
        difficultyMenu.setBackground(menuBackgroundColor);
        difficultyMenu.setBorderPainted(false);

        // ActionListener for difficulty menu items
        ActionListener difficultyActionListener = e -> {
            JMenuItem source = (JMenuItem) e.getSource();
            String difficulty = source.getText();

            // Load and process the JSON file
            String filePath = "src/main/java/org/example/Jsonfiles/Fill-in-gaps/fill-in-the-gaps-" + difficulty.toLowerCase() + ".json";
            try {
                Gson gson = new Gson();
                quizData = gson.fromJson(new FileReader(filePath), QuizDataFill.class);
                // Perform the desired operations with the loaded quiz data
                System.out.println("Loaded quiz data: " + quizData);
                showQuiz();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            System.out.println("After choosing difficulty: " + difficulty);
            System.out.println("Open file: fill-in-the-gaps" + difficulty.toLowerCase() + ".json");
            System.out.println("From path: " + filePath);
        };

        // Create difficulty menu items
        JMenuItem easyItem = createItem("Easy", 'A', difficultyActionListener);
        JMenuItem mediumItem = createItem("Medium", 'A', difficultyActionListener);
        JMenuItem hardItem = createItem("Hard", 'A', difficultyActionListener);

        difficultyMenu.add(easyItem);
        difficultyMenu.add(mediumItem);
        difficultyMenu.add(hardItem);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(difficultyMenu);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(Box.createHorizontalGlue());

        // Create and configure the menu panel
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        menuPanel.add(menuBar);

        add(menuPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationRelativeTo(null);
        setTitle("QuizApp - Difficulty");
        setVisible(true);
    }

    private void showQuiz() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Create and configure the title label
        titleLabel = new JLabel("Quiz Title");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(titleLabel, constraints);

        // Create and configure the stimulus text area
        stimulusTextArea = new JTextArea();
        stimulusTextArea.setEditable(false);
        stimulusTextArea.setLineWrap(true);
        stimulusTextArea.setWrapStyleWord(true);
        stimulusTextArea.setPreferredSize(new Dimension(400, 100));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(stimulusTextArea, constraints);

        // Create and configure the answer field
        JTextField answerField = new JTextField();
        answerField.setPreferredSize(new Dimension(200, 25));
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(answerField, constraints);

        List<String> answerOptions = quizData.getAnswers().get(0); // Assuming there is only one set of answers
        int answerCount = answerOptions.size();
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionListener answerActionListener = e -> {
            AbstractButton button = (AbstractButton) e.getSource();
            String userAnswer = button.getText();
            checkAnswer(userAnswer);
        };

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.MAGENTA);
        submitButton.setForeground(buttonTextColor);
        constraints.gridx = 0;
        constraints.gridy = answerCount / 2 + 3; // Move down by one more row
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        contentPanel.add(submitButton, constraints);
        submitButton.addActionListener(e -> {
            // Retrieve the selected answer
            ButtonModel selectedButton = buttonGroup.getSelection();
            if (selectedButton != null) {
                String userAnswer = selectedButton.getActionCommand();
                checkAnswer(userAnswer);
            } else {
                String userAnswer = answerField.getText();
                checkAnswer(userAnswer);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setBackground(buttonBackgroundColor);
        backButton.setForeground(buttonTextColor);
        backButton.addActionListener(e -> {
            // Instantiate the Menu class and display it
            Menu menu = new Menu();
            menu.setVisible(true);
            FillInGapsQuiz.this.dispose(); // Close the current FillInGapsQuiz frame
        });
        constraints.gridx = 1;
        constraints.gridy = answerCount / 2 + 3; // Move down by one more row
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        contentPanel.add(backButton, constraints);

        add(contentPanel, BorderLayout.CENTER);

        constraints.gridx = 2; // Adjust the column index based on your desired layout
        constraints.gridy = answerCount / 2 + 3; // Adjust the row index based on your desired layout
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        contentPanel.add(backButton, constraints);

        updateQuizData();
        setTitle("QuizApp - Quiz");
        revalidate(); // Added to update the layout
    }

    private void updateQuizData() {
        if (quizData != null) {
            titleLabel.setText(quizData.getTitle());
            stimulusTextArea.setText(quizData.getStimulus());
        }
    }

    private void checkAnswer(String userAnswer) {
        if (quizData != null && quizData.getInteractions() != null) {
            for (QuizDataFill.Interaction interaction : quizData.getInteractions()) {
                String placeholder = interaction.getPlaceholder();
                List<String> answers = interaction.getAnswers();
                if (answers.contains(userAnswer)) {
                    JOptionPane.showMessageDialog(this, "Correct answer!");
                    if (currentUser == null) {
                        JOptionPane.showMessageDialog(this, "Error: User not logged in.");
                        return;
                    }
                    currentUser.incrementScore(); // Increment score
                    currentUser.incrementCorrectAnswers(); // Increment correct answers count
                    resetAnswerButtons();

                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Incorrect answer!");
        currentUser.incrementIncorrectAnswers(); // Increment incorrect answers count
        resetAnswerButtons();
    }


    private void resetAnswerButtons() {
        if (answerButtonGroup != null) {
            answerButtonGroup.clearSelection();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            UserLogin.User currentUser = new UserLogin.User();
            new FillInGapsQuiz(currentUser);
        });
    }


    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

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

class QuizDataFill {
    private String difficulty;
    private String title;
    private String category;
    private String stimulus;
    private String prompt;
    private List<Interaction> interactions;
    private List<List<String>> answers;

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

    public String getStimulus() {
        return stimulus;
    }

    public void setStimulus(String stimulus) {
        this.stimulus = stimulus;
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

    public List<List<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(List<List<String>> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QuizData{" +
                "difficulty='" + difficulty + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", stimulus='" + stimulus + '\'' +
                ", prompt='" + prompt + '\'' +
                ", interactions=" + interactions +
                ", answers=" + answers +
                '}';
    }

    static class Interaction {
        private String placeholder;
        private String category;
        private List<String> answers;

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        @Override
        public String toString() {
            return "Interaction{" +
                    "placeholder='" + placeholder + '\'' +
                    ", category='" + category + '\'' +
                    ", answers=" + answers +
                    '}';
        }
    }

}
