package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import javax.swing.AbstractButton;

class Question {
    private String question;
    private List<String> options;
    private int correctAnswer;

    public Question(String question, List<String> options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}

class Quiz {

    private String title;
    private List<Question> questions;

    public Quiz(String title) {
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void runQuiz() {
        JFrame frame = new JFrame("Quiz");
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        JLabel titleLabel = new JLabel("Quiz: " + title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

            JLabel questionLabel = new JLabel("Question " + (i + 1) + ": " + question.getQuestion());
            questionPanel.add(questionLabel);

            List<String> options = question.getOptions();
            ButtonGroup buttonGroup = new ButtonGroup();

            for (int j = 0; j < options.size(); j++) {
                JRadioButton radioButton = new JRadioButton((j + 1) + ". " + options.get(j));
                radioButton.setActionCommand(Integer.toString(j + 1));
                buttonGroup.add(radioButton);
                radioButton.putClientProperty("ButtonGroup", buttonGroup); // Set ButtonGroup as client property
                questionPanel.add(radioButton);
            }

            questionsPanel.add(questionPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = 0;

                for (int i = 0; i < questions.size(); i++) {
                    Question question = questions.get(i);
                    JPanel questionPanel = (JPanel) questionsPanel.getComponent(i * 2);
                    ButtonGroup buttonGroup = findButtonGroup(questionPanel);

                    Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
                    while (enumeration.hasMoreElements()) {
                        AbstractButton button = enumeration.nextElement();
                        if (button.isSelected()) {
                            int selectedOptionIndex = Integer.parseInt(button.getActionCommand());
                            if (selectedOptionIndex == question.getCorrectAnswer()) {
                                score++;
                            }
                            break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(frame, "Quiz completed!\nYour score: " + score + " out of " + questions.size());
            }

            private ButtonGroup findButtonGroup(Container container) {
                Component[] components = container.getComponents();
                for (Component component : components) {
                    if (component instanceof JComponent) {
                        ButtonGroup buttonGroup = (ButtonGroup) ((JComponent) component).getClientProperty("ButtonGroup");
                        if (buttonGroup != null) {
                            return buttonGroup;
                        }
                    }
                    if (component instanceof Container) {
                        ButtonGroup buttonGroup = findButtonGroup((Container) component);
                        if (buttonGroup != null) {
                            return buttonGroup;
                        }
                    }
                }
                return null;
            }

        });

        frame.add(submitButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void saveToJSON(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath), this);
        System.out.println("Quiz saved to: " + filePath);
    }
}

public class QuizCreator extends JFrame {

    public QuizCreator() {
        setLocationRelativeTo(null); // Center the QuizCreator window

        JFrame frame = new JFrame("Quiz Creator");
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 3));

        JLabel titleLabel = new JLabel("Quiz Title:");
        JTextField titleField = new JTextField();

        JLabel numQuestionsLabel = new JLabel("Number of Questions:");
        JTextField numQuestionsField = new JTextField();

        JButton createButton = new JButton("Create Quiz");
        createButton.addActionListener(e -> {
            String quizTitle = titleField.getText();
            int numQuestions = Integer.parseInt(numQuestionsField.getText());

            Quiz quiz = new Quiz(quizTitle);

            for (int i = 0; i < numQuestions; i++) {
                String questionText = JOptionPane.showInputDialog(frame, "Enter Question " + (i + 1));
                int numOptions = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the number of options for Question " + (i + 1)));

                List<String> options = new ArrayList<>();
                for (int j = 0; j < numOptions; j++) {
                    String option = JOptionPane.showInputDialog(frame, "Enter option " + (j + 1) + " for Question " + (i + 1));
                    options.add(option);
                }

                int correctAnswer = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the correct answer (option number) for Question " + (i + 1)));

                Question question = new Question(questionText, options, correctAnswer);
                quiz.addQuestion(question);
            }

            try {
                String filePath = "src/main/java/org/example/Json files/" + quizTitle + ".json";
                quiz.saveToJSON(filePath);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while saving the quiz to JSON.");
                ex.printStackTrace();
            }

            quiz.runQuiz();
            frame.dispose();
        });

        inputPanel.add(titleLabel);
        inputPanel.add(titleField);
        inputPanel.add(numQuestionsLabel);
        inputPanel.add(numQuestionsField);
        inputPanel.add(new JLabel());
        inputPanel.add(createButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        QuizCreator quizCreator = new QuizCreator();
    }
}
