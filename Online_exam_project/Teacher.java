import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Teacher extends JFrame {
    private JTextArea questionArea;
    private JTextField optionA, optionB, optionC, optionD, correctOption, examTime;
    private JButton setQuestionButton;

    public Teacher() {
        setTitle("Teacher - Set Exam Question");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        questionArea = new JTextArea();
        optionA = new JTextField();
        optionB = new JTextField();
        optionC = new JTextField();
        optionD = new JTextField();
        correctOption = new JTextField();
        examTime = new JTextField();
        setQuestionButton = new JButton("Set Question");

        add(new JLabel("Question:"));
        add(new JScrollPane(questionArea));
        add(new JLabel("Option A:"));
        add(optionA);
        add(new JLabel("Option B:"));
        add(optionB);
        add(new JLabel("Option C:"));
        add(optionC);
        add(new JLabel("Option D:"));
        add(optionD);
        add(new JLabel("Correct Option:"));
        add(correctOption);
        add(new JLabel("Exam Time (in seconds):"));
        add(examTime);
        add(setQuestionButton);

        setQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionArea.getText();
                String optA = optionA.getText();
                String optB = optionB.getText();
                String optC = optionC.getText();
                String optD = optionD.getText();
                String correctOpt = correctOption.getText();
                int time = Integer.parseInt(examTime.getText());

                try {
                    ServerSocket serverSocket = new ServerSocket(1234);
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(new ExamQuestion(question, optA, optB, optC, optD, correctOpt, time));
                    socket.close();
                    serverSocket.close();
                    JOptionPane.showMessageDialog(null, "Question set successfully!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Teacher().setVisible(true);
            }
        });
    }
}

class ExamQuestion implements Serializable {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private int time;

    public ExamQuestion(String question, String optionA, String optionB, String optionC, String optionD, String correctOption, int time) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.time = time;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public int getTime() {
        return time;
    }
}
