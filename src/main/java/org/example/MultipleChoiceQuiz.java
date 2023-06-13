package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultipleChoiceQuiz extends JFrame {
    private JLabel questionLabel;
    private JButton optionButton1;
    private JButton optionButton2;
    private JButton optionButton3;
    private JButton optionButton4;
    private UserLogin.User currentUser;
    private int currentQuestion = 0;
    private int score = 0;

    private int incorrectAnswers;

    private String[] questions = {
            "Question 1: What is the capital of France?",
            "Question 2: Who painted the Mona Lisa?",
            "Question 3: What is the largest planet in our solar system?",
            "Question 4: What year was the Declaration of Independence signed?"
    };

    private String[][] options = {
            {"London", "Paris", "Berlin", "Rome"},
            {"Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Michelangelo"},
            {"Mars", "Jupiter", "Saturn", "Earth"},
            {"1776", "1789", "1492", "1812"}
    };

    private int[] answers = {1, 1, 1, 0};

    public static void main(String[] args) {
        UserLogin.User currentUser = new UserLogin.User();
        new MultipleChoiceQuiz(currentUser);
    }

    public MultipleChoiceQuiz(UserLogin.User currentUser) {
        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());
        this.currentUser = currentUser;

        BackToMenuPanel backToMenuPanel = new BackToMenuPanel();
        add(backToMenuPanel, BorderLayout.SOUTH);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 2, 50, 50));
        optionButton1 = new JButton();
        optionButton2 = new JButton();
        optionButton3 = new JButton();
        optionButton4 = new JButton();
        optionsPanel.add(optionButton1);
        optionsPanel.add(optionButton2);
        optionsPanel.add(optionButton3);
        optionsPanel.add(optionButton4);

        add(questionPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);



        updateQuestion();

        optionButton1.addActionListener(new OptionButtonListener(0));
        optionButton2.addActionListener(new OptionButtonListener(1));
        optionButton3.addActionListener(new OptionButtonListener(2));
        optionButton4.addActionListener(new OptionButtonListener(3));
    }

    private void updateQuestion() {
        if (currentQuestion < questions.length) {
            questionLabel.setText(questions[currentQuestion]);
            optionButton1.setText(options[currentQuestion][0]);
            optionButton2.setText(options[currentQuestion][1]);
            optionButton3.setText(options[currentQuestion][2]);
            optionButton4.setText(options[currentQuestion][3]);
        } else {
            showResult();
        }
    }

    private void showResult() {
        double percentage = (double) score / questions.length * 100;
        String message = "Quiz completed!\nYour score: " + score + "/" + questions.length +
                "\nPercentage: " + String.format("%.2f", percentage) + "%";
        JOptionPane.showMessageDialog(this, message, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        EventQueue.invokeLater(Menu::new);
        dispose();
    }
    private class OptionButtonListener implements ActionListener {
        private int selectedOptionIndex;

        public OptionButtonListener(int selectedOptionIndex) {
            this.selectedOptionIndex = selectedOptionIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedOptionIndex == answers[currentQuestion]) {
                score++;
                if (currentUser != null) {
                    currentUser.incrementScore(); // Increment the user's score
                    currentUser.incrementCorrectAnswers(); // Increment the user's correct answers
                }
            } else {
                if (currentUser != null) {
                    currentUser.incrementIncorrectAnswers(); // Increment the user's incorrect answers
                }
            }

            currentQuestion++;
            updateQuestion();
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
                EventQueue.invokeLater(Menu::new); // Start the menu
                MultipleChoiceQuiz.this.dispose();
            }
        }
    }
}
