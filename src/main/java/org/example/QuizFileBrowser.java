package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class QuizFileBrowser extends JFrame {
    private JList<String> fileList;
    private DefaultListModel<String> listModel;

    public QuizFileBrowser(String directory) {
        setTitle("Quiz File Browser");
        setSize(400, 300);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane, BorderLayout.CENTER);

        JButton openButton = new JButton("Open Quiz");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    String filePath = directory + File.separator + selectedFile;
                    openQuiz(filePath);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        add(buttonPanel, BorderLayout.SOUTH);

        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                openButton.setEnabled(fileList.getSelectedIndex() != -1);
            }
        });

        loadFileList(directory);
    }

    private void loadFileList(String directory) {
        listModel.clear();
        List<String> jsonFiles = findJsonFiles(directory);
        for (String file : jsonFiles) {
            listModel.addElement(file);
        }
    }

    private List<String> findJsonFiles(String directory) {
        List<String> jsonFiles = new ArrayList<>();
        Path path = Paths.get(directory);
        try {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        if (fileName.toLowerCase().endsWith(".json")) {
                            jsonFiles.add(fileName);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonFiles;
    }

    private void openQuiz(String filePath) {
        // Add your logic to open and run the quiz from the JSON file
        System.out.println("Opening quiz from file: " + filePath);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String directory = "D:\\QuizApp\\QuizApp\\";
            QuizFileBrowser fileBrowser = new QuizFileBrowser(directory);
            fileBrowser.setVisible(true);
        });
    }
}

