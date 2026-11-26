JAVA QUIZ APPLICATION
=====================

FILES
-----
QuizApplication.java - Main Java Swing source code.
README.txt            - Project setup and run instructions.

REQUIREMENTS
------------
- Java Development Kit (JDK)
- VS Code or another Java IDE

HOW TO RUN
----------
1. Extract this ZIP file.
2. Open the extracted QuizApplication folder in VS Code.
3. Open the terminal in that folder.
4. Compile the program:
       javac QuizApplication.java
5. Run the program:
       java QuizApplication

FEATURES
--------
- Home page with Start Quiz button
- 10 randomly selected questions per quiz
- Shuffled answer choices
- Quiz timer
- Direct question navigation
- Answered and unanswered counters
- Navigation status colours:
    Green  = Answered
    Orange = Visited but unanswered
    Blue   = Current question
    Grey   = Not visited
- Submission confirmation dialog
- Result page with score and time taken
- Play Again and Home buttons

QUESTION BANK
-------------
The program includes 26 Java multiple-choice questions and selects 10
for each quiz. It avoids repeating the exact same 10-question set
immediately within the current application session.
