package com.program.endtermexam;

import java.util.HashMap;

public class QuizHelper {
    private String answer, question, score;

    public QuizHelper(String answer, String question, String score) {
        this.answer = answer;
        this.question = question;
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}