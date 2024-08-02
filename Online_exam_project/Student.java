import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Student extends JFrame {
    private JTextArea questionArea;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton submitButton;
    private JLabel timerLabel;
    private int timeLeft;

    public Student() {
        setTitle("Student - Take Exam");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();
        optionsGroup = new ButtonGroup();
        submitButton = new JButton("Submit");
        timerLabel = new JLabel("Time left: ");

        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        add(new JScrollPane(questionArea));
        add(optionA);
        add(optionB);
        add(optionC);
        add(optionD);
        add(submitButton);
        add(timerLabel);

        try {
            Socket socket = new Socket("localhost", 1234);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ExamQuestion question = (ExamQuestion) in.readObject();
            socket.close();

            questionArea.setText(question.getQuestion());
            optionA.setText(question.getOptionA());
            optionB.setText(question.getOptionB());
            optionC.setText(question.getOptionC());
            optionD.setText(question.getOptionD());
            timeLeft = question.getTime();

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (timeLeft > 0) {
                        timeLeft--;
                        timerLabel.setText("Time left: " + timeLeft);
                    } else {
                        timer.cancel();
                        checkAnswer(question.getCorrectOption());
                    }
                }
            }, 1000, 1000);

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.cancel();
                    checkAnswer(question.getCorrectOption());
                }
            });
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer(String correctOption) {
        String selectedOption = "";
        if (optionA.isSelected()) selectedOption = "A";
        if (optionB.isSelected()) selectedOption = "B";
        if (optionC.isSelected()) selectedOption = "C";
        if (optionD.isSelected()) selectedOption = "D";

        if (selectedOption.equals(correctOption)) {
            JOptionPane.showMessageDialog(null, "Correct Answer!");
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Answer!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Student().setVisible(true);
            }
        });
    }
}
//java Student