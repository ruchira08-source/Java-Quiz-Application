
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class QuizApplication extends JFrame {

    // APPLICATION COLOURS
    private final Color NAVY = new Color(35, 43, 89);
    private final Color BLUE = new Color(69, 91, 220);
    private final Color PURPLE = new Color(119, 76, 196);
    private final Color LIGHT = new Color(241, 243, 255);
    private final Color GREEN = new Color(31, 155, 112);
    private final Color ORANGE = new Color(230, 145, 45);

    private final int TOTAL = 10;

    // QUESTION CLASS
    private static class Question {
        String text;
        String[] options;
        int answer;

        Question(String text, String[] options, int answer) {
            this.text = text;
            this.options = options;
            this.answer = answer;
        }
    }

    private static class QuizQuestion {
        String text;
        ArrayList<String> options;
        int answer;

        QuizQuestion(Question q) {
            text = q.text;
            options = new ArrayList<>(Arrays.asList(q.options));

            String correct = q.options[q.answer];

            Collections.shuffle(options);
            answer = options.indexOf(correct);
        }
    }

    private ArrayList<Question> bank = new ArrayList<>();
    private ArrayList<QuizQuestion> quiz = new ArrayList<>();

    private Set<String> previousQuiz = new HashSet<>();

    private int[] selected;
    private boolean[] visited;

    private int current = 0;
    private long startTime;

    private CardLayout layout = new CardLayout();
    private JPanel main = new JPanel(layout);

    private JLabel timerLabel;
    private JLabel totalLabel;
    private JLabel answeredLabel;
    private JLabel unansweredLabel;
    private JLabel questionNo;
    private JLabel questionText;

    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup group = new ButtonGroup();

    private JPanel navigation;

    private JButton previousButton;
    private JButton nextButton;
    private JButton skipButton;

    private javax.swing.Timer timer;

    // CONSTRUCTOR
    public QuizApplication() {

        setTitle("Java Quiz Application");
        setSize(1350, 900);
        setMinimumSize(new Dimension(950, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createQuestions();

        main.add(homePage(), "HOME");
        main.add(quizPage(), "QUIZ");

        add(main);

        layout.show(main, "HOME");

        timer = new javax.swing.Timer(1000, e -> updateStats());
    }

    // QUESTION BANK
    private void createQuestions() {

        bank.add(new Question(
            "Which programming language is used to develop this application?",
            new String[]{"Python", "Java", "C", "HTML"}, 1));

        bank.add(new Question(
            "Which keyword is used to declare a class in Java?",
            new String[]{"class", "new", "public", "void"}, 0));

        bank.add(new Question(
            "Which method is the entry point of a Java application?",
            new String[]{"start()", "run()", "main()", "init()"}, 2));

        bank.add(new Question(
            "Which Swing component creates a clickable button?",
            new String[]{"JLabel", "JButton", "JTextField", "JPanel"}, 1));

        bank.add(new Question(
            "Which symbol ends most Java statements?",
            new String[]{".", ":", ";", ","}, 2));

        bank.add(new Question(
            "Which keyword creates an object in Java?",
            new String[]{"class", "this", "new", "static"}, 2));

        bank.add(new Question(
            "Which data type stores whole numbers?",
            new String[]{"int", "boolean", "char", "String"}, 0));

        bank.add(new Question(
            "Which value represents true or false?",
            new String[]{"int", "boolean", "double", "char"}, 1));

        bank.add(new Question(
            "Which operator checks equality in Java?",
            new String[]{"=", "==", "!=", ">"}, 1));

        bank.add(new Question(
            "Which keyword is used to inherit a class?",
            new String[]{"implements", "extends", "inherits", "super"}, 1));

        bank.add(new Question(
            "Which package contains Java Swing classes?",
            new String[]{"java.io", "java.net", "javax.swing", "java.sql"}, 2));

        bank.add(new Question(
            "Which component displays non-editable text?",
            new String[]{"JLabel", "JButton", "JFrame", "JRadioButton"}, 0));

        bank.add(new Question(
            "Which loop executes while a condition is true?",
            new String[]{"if", "while", "switch", "break"}, 1));

        bank.add(new Question(
            "Which keyword stops a loop immediately?",
            new String[]{"continue", "return", "break", "final"}, 2));

        bank.add(new Question(
            "Which access modifier gives the widest access?",
            new String[]{"private", "protected", "public", "default"}, 2));

        bank.add(new Question(
            "Which method compares two strings by content?",
            new String[]{"==", "equals()", "compare()", "same()"}, 1));

        bank.add(new Question(
            "Which keyword is used to create a subclass?",
            new String[]{"extends", "static", "final", "package"}, 0));

        bank.add(new Question(
            "What is the size of Java char in bits?",
            new String[]{"8", "16", "32", "64"}, 1));

        bank.add(new Question(
            "Which keyword is used to handle exceptions?",
            new String[]{"try", "check", "error", "catching"}, 0));

        bank.add(new Question(
            "Which collection stores unique elements?",
            new String[]{"ArrayList", "HashSet", "LinkedList", "Vector"}, 1));

        bank.add(new Question(
            "Which keyword prevents a variable from being reassigned?",
            new String[]{"static", "const", "final", "fixed"}, 2));

        bank.add(new Question(
            "Which method gets the length of a String?",
            new String[]{"size()", "count()", "length()", "getLength()"}, 2));

        bank.add(new Question(
            "Which feature allows methods with the same name but different parameters?",
            new String[]{"Overriding", "Overloading", "Inheritance", "Encapsulation"}, 1));

        bank.add(new Question(
            "Which keyword refers to the current object?",
            new String[]{"super", "this", "self", "current"}, 1));

        bank.add(new Question(
            "Which layout arranges components in rows and columns?",
            new String[]{"BorderLayout", "FlowLayout", "GridLayout", "CardLayout"}, 2));

        bank.add(new Question(
            "Which method is used to add an item to an ArrayList?",
            new String[]{"insert()", "add()", "put()", "append()"}, 1));
    }

    // HOME PAGE
    private JPanel homePage() {

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(LIGHT);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(580, 410));
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("✦");
        icon.setFont(new Font("Arial", Font.BOLD, 48));
        icon.setForeground(BLUE);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("JAVA QUIZ APPLICATION");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Test Your Java Knowledge");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 19));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel details = new JLabel(
            "<html><center>10 Random Questions<br><br>" +
            "Multiple Choice Questions<br><br>" +
            "Timer and Instant Navigation</center></html>");

        details.setFont(new Font("Arial", Font.PLAIN, 15));
        details.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = button("START QUIZ", BLUE);
        start.setMaximumSize(new Dimension(200, 50));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        start.addActionListener(e -> startQuiz());

        card.add(icon);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(details);
        card.add(Box.createVerticalStrut(25));
        card.add(start);

        page.add(card);

        return page;
    }

    // QUIZ PAGE
    private JPanel quizPage() {

        JPanel page = new JPanel(new BorderLayout(15, 15));
        page.setBackground(LIGHT);
        page.setBorder(new EmptyBorder(25, 28, 25, 28));

        JLabel heading = new JLabel("JAVA QUIZ APPLICATION");
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(NAVY);

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(BLUE);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(heading, BorderLayout.WEST);
        top.add(timerLabel, BorderLayout.EAST);

        totalLabel = new JLabel();
        answeredLabel = new JLabel();
        unansweredLabel = new JLabel();

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 105));

        stats.add(statCard(totalLabel, new Color(225, 232, 255), BLUE));
        stats.add(statCard(answeredLabel, new Color(220, 246, 235), GREEN));
        stats.add(statCard(unansweredLabel, new Color(255, 239, 218), ORANGE));

        JPanel header = new JPanel(new BorderLayout(0, 15));
        header.setOpaque(false);
        header.add(top, BorderLayout.NORTH);
        header.add(stats, BorderLayout.SOUTH);

        page.add(header, BorderLayout.NORTH);

        // QUESTION PANEL
        JPanel questionCard = new JPanel();
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setBackground(Color.WHITE);
        questionCard.setBorder(new EmptyBorder(35, 35, 35, 35));

        questionNo = new JLabel();
        questionNo.setFont(new Font("Arial", Font.BOLD, 16));
        questionNo.setForeground(BLUE);

        questionText = new JLabel();
        questionText.setFont(new Font("Arial", Font.BOLD, 21));
        questionText.setForeground(NAVY);

        questionCard.add(questionNo);
        questionCard.add(Box.createVerticalStrut(25));
        questionCard.add(questionText);
        questionCard.add(Box.createVerticalStrut(30));

        for (int i = 0; i < 4; i++) {

            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 18));
            options[i].setBackground(Color.WHITE);
            options[i].setFocusPainted(false);

            final int index = i;

            options[i].addActionListener(e -> {

                if (selected == null) return;

                selected[current] = index;

                updateStats();
                refreshNavigation();
            });

            group.add(options[i]);

            questionCard.add(options[i]);
            questionCard.add(Box.createVerticalStrut(15));
        }

        JPanel center = new JPanel(new BorderLayout(18, 0));
        center.setOpaque(false);
        center.add(questionCard, BorderLayout.CENTER);

        // QUESTION NAVIGATION
        JPanel navCard = new JPanel(new BorderLayout(8, 12));
        navCard.setPreferredSize(new Dimension(340, 0));
        navCard.setBackground(Color.WHITE);
        navCard.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel navTitle = new JLabel("QUESTION NAVIGATION");
        navTitle.setFont(new Font("Arial", Font.BOLD, 17));
        navTitle.setForeground(NAVY);

        navCard.add(navTitle, BorderLayout.NORTH);

        navigation = new JPanel(new GridLayout(0, 2, 10, 10));
        navigation.setBackground(Color.WHITE);
        navCard.add(navigation, BorderLayout.CENTER);

        JLabel legend = new JLabel(
            "<html><b style='color:#1f9b70'>Green:</b> Answered<br>" +
            "<b style='color:#e6912d'>Orange:</b> Visited but unanswered<br>" +
            "<b style='color:#455bdc'>Blue:</b> Current<br>" +
            "<b style='color:gray'>Grey:</b> Not visited</html>");

        legend.setFont(new Font("Arial", Font.PLAIN, 13));
        navCard.add(legend, BorderLayout.SOUTH);

        center.add(navCard, BorderLayout.EAST);

        page.add(center, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel bottom = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0));

        bottom.setOpaque(false);

        previousButton = button("Previous", new Color(110, 120, 140));
        skipButton = button("Skip", ORANGE);
        nextButton = button("Next", BLUE);
        JButton submit = button("Submit Quiz", GREEN);

        previousButton.addActionListener(e -> goToQuestion(current - 1));
        nextButton.addActionListener(e -> goToQuestion(current + 1));

        skipButton.addActionListener(e -> {

            if (current < TOTAL - 1) {
                goToQuestion(current + 1);
            } else {
                JOptionPane.showMessageDialog(this,
                    "This is the last question. You can submit the quiz.");
            }
        });

        submit.addActionListener(e -> finishQuiz());

        bottom.add(previousButton);
        bottom.add(skipButton);
        bottom.add(nextButton);
        bottom.add(submit);

        page.add(bottom, BorderLayout.SOUTH);

        return page;
    }

    // STATISTICS CARDS
    private JPanel statCard(JLabel label, Color background, Color accent) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createLineBorder(accent, 2));

        label.setForeground(accent);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label);

        return panel;
    }

    // BUTTON DESIGN
    private JButton button(String text, Color color) {

        JButton b = new JButton(text);

        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(13, 20, 13, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return b;
    }

    // START QUIZ
    private void startQuiz() {

        ArrayList<Question> pool = new ArrayList<>(bank);
        Collections.shuffle(pool);

        ArrayList<Question> chosen =
            new ArrayList<>(pool.subList(0, TOTAL));

        Set<String> newSet = new HashSet<>();

        for (Question q : chosen) {
            newSet.add(q.text);
        }

        // Ensure a different question set from the previous quiz.
        if (newSet.equals(previousQuiz)) {
            chosen.set(TOTAL - 1, pool.get(TOTAL));
        }

        previousQuiz.clear();
        quiz.clear();

        for (Question q : chosen) {
            previousQuiz.add(q.text);
            quiz.add(new QuizQuestion(q));
        }

        selected = new int[TOTAL];
        Arrays.fill(selected, -1);

        visited = new boolean[TOTAL];
        visited[0] = true;

        current = 0;
        startTime = System.currentTimeMillis();

        layout.show(main, "QUIZ");

        showQuestion();
        updateStats();

        timer.start();
    }

    // DISPLAY QUESTION
    private void showQuestion() {

        QuizQuestion q = quiz.get(current);

        questionNo.setText(
            "QUESTION " + (current + 1) + " OF " + TOTAL);

        questionText.setText(
            "<html><div style='width:550px'>" +
            (current + 1) + ". " + q.text +
            "</div></html>");

        group.clearSelection();

        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options.get(i));
            options[i].setSelected(selected[current] == i);
        }

        previousButton.setEnabled(current > 0);
        nextButton.setEnabled(current < TOTAL - 1);
        skipButton.setEnabled(current < TOTAL - 1);

        refreshNavigation();
    }

    // NAVIGATION
    private void goToQuestion(int index) {

        if (index >= 0 && index < TOTAL) {

            current = index;
            visited[current] = true;

            showQuestion();
            updateStats();
        }
    }

    // QUESTION NAVIGATION COLOURS
    private void refreshNavigation() {

        navigation.removeAll();

        for (int i = 0; i < TOTAL; i++) {

            final int index = i;

            JButton b = new JButton("QUESTION " + (i + 1));

            b.setFont(new Font("Arial", Font.BOLD, 13));
            b.setPreferredSize(new Dimension(130, 75));
            b.setFocusPainted(false);

            if (i == current) {
                b.setBackground(BLUE);
                b.setForeground(Color.WHITE);

            } else if (selected[i] >= 0) {
                b.setBackground(new Color(205, 244, 225));
                b.setForeground(GREEN);

            } else if (visited[i]) {
                b.setBackground(new Color(255, 230, 195));
                b.setForeground(new Color(155, 90, 15));

            } else {
                b.setBackground(new Color(237, 239, 248));
                b.setForeground(NAVY);
            }

            b.addActionListener(e -> goToQuestion(index));

            navigation.add(b);
        }

        navigation.revalidate();
        navigation.repaint();
    }

    // UPDATE COUNTERS
    private void updateStats() {

        if (selected == null) return;

        int answered = 0;

        for (int a : selected) {
            if (a >= 0) answered++;
        }

        int unanswered = TOTAL - answered;

        totalLabel.setText(
            "<html><center>TOTAL QUESTIONS<br>" +
            "<font size='6'><b>" + TOTAL +
            "</b></font></center></html>");

        answeredLabel.setText(
            "<html><center>ANSWERED<br>" +
            "<font size='6'><b>" + answered +
            "</b></font></center></html>");

        unansweredLabel.setText(
            "<html><center>UNANSWERED<br>" +
            "<font size='6'><b>" + unanswered +
            "</b></font></center></html>");

        long seconds =
            (System.currentTimeMillis() - startTime) / 1000;

        timerLabel.setText(String.format(
            "Time: %02d:%02d", seconds / 60, seconds % 60));
    }

    // SUBMIT QUIZ
    private void finishQuiz() {

        int answered = 0;

        for (int a : selected) {
            if (a >= 0) answered++;
        }

        int unanswered = TOTAL - answered;

        if (unanswered > 0) {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "You have " + unanswered +
                " unanswered questions. Submit anyway?",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) return;

        } else {

            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to submit your quiz?",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) return;
        }

        timer.stop();

        int correct = 0;

        for (int i = 0; i < TOTAL; i++) {
            if (selected[i] == quiz.get(i).answer) {
                correct++;
            }
        }

        int incorrect = answered - correct;

        long seconds =
            (System.currentTimeMillis() - startTime) / 1000;

        showResult(correct, incorrect, answered, unanswered, seconds);
    }

    // RESULT PAGE
    private void showResult(int correct, int incorrect,
                            int answered, int unanswered,
                            long seconds) {

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(LIGHT);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(600, 560));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 45, 30, 45));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("QUIZ COMPLETED!");
        title.setFont(new Font("Arial", Font.BOLD, 29));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel score = new JLabel(
            "Your Score: " + correct + " / " + TOTAL);

        score.setFont(new Font("Arial", Font.BOLD, 26));
        score.setForeground(BLUE);
        score.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(15));
        card.add(score);
        card.add(Box.createVerticalStrut(30));

        addResultLine(card, "Total Questions", TOTAL);
        addResultLine(card, "Answered", answered);
        addResultLine(card, "Unanswered", unanswered);
        addResultLine(card, "Correct Answers", correct);
        addResultLine(card, "Incorrect Answers", incorrect);

        JLabel time = new JLabel(String.format(
            "Time Taken: %02d min %02d sec",
            seconds / 60, seconds % 60));

        time.setFont(new Font("Arial", Font.PLAIN, 17));
        time.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(time);
        card.add(Box.createVerticalStrut(25));

        JButton again = button("PLAY AGAIN", BLUE);
        again.setAlignmentX(Component.CENTER_ALIGNMENT);

        again.addActionListener(e -> startQuiz());

        JButton home = button("HOME", PURPLE);
        home.setAlignmentX(Component.CENTER_ALIGNMENT);

        home.addActionListener(e -> {
            timer.stop();
            layout.show(main, "HOME");
        });

        card.add(again);
        card.add(Box.createVerticalStrut(12));
        card.add(home);

        page.add(card);

        main.add(page, "RESULT");
        layout.show(main, "RESULT");
    }

    // RESULT DETAILS
    private void addResultLine(JPanel card, String label, int value) {

        JLabel line = new JLabel(label + ":  " + value);

        line.setFont(new Font("Arial", Font.PLAIN, 17));
        line.setForeground(NAVY);
        line.setAlignmentX(Component.CENTER_ALIGNMENT);

        line.setBorder(new EmptyBorder(7, 0, 7, 0));

        card.add(line);
    }

    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new QuizApplication().setVisible(true);
        });
    }
}
