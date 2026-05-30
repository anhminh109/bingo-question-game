package com.bingo.model;

public class Question {
    private int id;
    private int cellNumber;
    private String questionText;
    private String answer;

    public Question() {
    }

    public Question(int cellNumber, String questionText) {
        this(cellNumber, questionText, "");
    }

    public Question(int cellNumber, String questionText, String answer) {
        this.cellNumber = cellNumber;
        this.questionText = questionText;
        this.answer = answer;
    }

    public Question(int id, int cellNumber, String questionText) {
        this(id, cellNumber, questionText, "");
    }

    public Question(int id, int cellNumber, String questionText, String answer) {
        this.id = id;
        this.cellNumber = cellNumber;
        this.questionText = questionText;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
