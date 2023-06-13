package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.awt.event.ActionListener;

// I decided to keep the imports but deleting them doesn't cause any problems
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class OpenEnded extends JFrame {

    private UserLogin.User currentUser;
    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = kit.getScreenSize();
    private int submittedAnswerCount = 0;

    private String OpenEnded = "";

    private final int screenHeight = screenSize.height;
    private final int screenWidth = screenSize.width;

    private final Color menuTextColor = Color.WHITE;
    private final Color menuBackgroundColor = new Color(0, 187, 94);
    private final Color menuBorderColor = new Color(0, 112, 55);
    private final Color buttonBackgroundColor = new Color(0, 89, 255);
    private final Color buttonTextColor = Color.WHITE;

    private QuizDataOpen quizData;
    private JLabel titleLabel;

    private ButtonGroup answerButtonGroup;

    private JPanel menuPanel; // Declare menuPanel as an instance variable



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

    public OpenEnded(UserLogin.User currentUser) {
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());
        this.currentUser = currentUser;

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(menuBackgroundColor);
        menuBar.setBorder(BorderFactory.createLineBorder(menuBorderColor));
        menuBar.setPreferredSize(new Dimension(200, 40));

        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setForeground(menuTextColor);
        difficultyMenu.setFont(new Font("Arial", Font.BOLD, 14));
        difficultyMenu.setOpaque(false);
        difficultyMenu.setBackground(menuBackgroundColor);
        difficultyMenu.setBorderPainted(false);

        ActionListener difficultyActionListener = e -> {
            JMenuItem source = (JMenuItem) e.getSource();
            String difficulty = source.getText();

            // Load and process the JSON file
            String filePath = "src/main/java/org/example/Jsonfiles/Open-ended-question\\open-ended-" + difficulty.toLowerCase() + ".json";
            try {
                Gson gson = new Gson();
                quizData = gson.fromJson(new FileReader(filePath), QuizDataOpen.class);
                // Perform the desired operations with the loaded quiz data
                System.out.println("Loaded quiz data: " + quizData);
                showQuiz();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            System.out.println("After choosing difficulty: " + difficulty);
            System.out.println("Open file: Open ended question - " + difficulty.toLowerCase());
            System.out.println("From path: " + filePath);
        };

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

        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        menuPanel.add(menuBar);

        add(menuPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationRelativeTo(null);
        setTitle("QuizApp - Difficulty");
        setVisible(true);
        JButton submitButton = new JButton("Submit");
        JTextField answerField = new JTextField();
        ButtonGroup buttonGroup = new ButtonGroup();

        submitButton.addActionListener(e -> {
            // Retrieve the selected answer
            ButtonModel selectedButton = buttonGroup.getSelection();
            String userAnswer;
            if (selectedButton != null) {
                userAnswer = selectedButton.getActionCommand();
            } else {
                userAnswer = answerField.getText();
            }

        });
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

        titleLabel = new JLabel("Quiz Title");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        contentPanel.add(titleLabel, constraints);


        JTextField answerField = new JTextField();
        answerField.setPreferredSize(new Dimension(200, 25));
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPanel.add(answerField, constraints);


        int answerCount = quizData.getAnswerOptionsCount();
        ButtonGroup buttonGroup = new ButtonGroup();
        ActionListener answerActionListener = e -> {
            AbstractButton button = (AbstractButton) e.getSource();
            String userAnswer = button.getText();
        };

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.MAGENTA);
        submitButton.setForeground(buttonTextColor);
        constraints.gridx = 1;
        constraints.gridy = answerCount / 2 + 3; // Move down by one more row
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        contentPanel.add(submitButton, constraints);

        submitButton.addActionListener(e -> {
            String OpenEnded = answerField.getText();
            currentUser.getOpenEnded();
        submittedAnswerCount++; // Increment the submittedAnswerCount

            // Show a dialog box with the submission message
            JOptionPane.showMessageDialog(this, "Your answer is submitted!", "Answer Submitted", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setBackground(buttonBackgroundColor);
        backButton.setForeground(buttonTextColor);
        backButton.addActionListener(e -> {
            // Instantiate the Menu class and display it
            Menu menu = new Menu();
            menu.setVisible(true);
            dispose();
        });
        constraints.gridx = 1;
        constraints.gridy = answerCount / 2 + 3; // Move down by one more row
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        contentPanel.add(backButton, constraints);

        add(contentPanel, BorderLayout.CENTER);

        int questionCount = quizData.getInteractions().size();

        for (int i = 0; i < questionCount; i++) {
            QuizDataOpen.Interaction interaction = quizData.getInteractions().get(i);
            String question = interaction.getQuestion();

            JLabel questionLabel = new JLabel(question);
            questionLabel.setForeground(Color.WHITE);
            constraints.gridx = 0;
            constraints.gridy = i+1; // Adjust the row index based on your desired layout
            constraints.anchor = GridBagConstraints.WEST; // Align the question label to the left
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
            contentPanel.add(questionLabel, constraints);


        }

        constraints.gridx = 3; // Adjust the column index based on your desired layout
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
        }
    }

    private void resetAnswerButtons() {
        if (answerButtonGroup != null) {
            answerButtonGroup.clearSelection();
        }
    }

    public static void main(String[] args) {

            UserLogin.User currentUser = new UserLogin.User();
            new OpenEnded(currentUser);
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

class QuizDataOpen {
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

    public int getAnswerOptionsCount() {
        int count = 0;
        if (interactions != null) {
            for (Interaction interaction : interactions) {
                if (interaction.getAnswer() != null && !interaction.getAnswer().isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "QuizData{" +
                "difficulty='" + difficulty + '\'' +
                ", title='" + title + '\'' +
                ", interactions=" + interactions +
                '}';
    }

    static class Interaction {

        private String question;
        private String answer;
        private List<String> answers;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        @Override
        public String toString() {
            return "Interaction{" +
                    ", question='" + question + '\'' +
                    ", answers=" + answers +
                    '}';
        }

    }

}