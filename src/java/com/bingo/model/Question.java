package com.bingo.model;

public class Question {
    private int id;
    private int cellNumber;
    private String questionText;

    public Question() {
    }

    public Question(int cellNumber, String questionText) {
        this.cellNumber = cellNumber;
        this.questionText = questionText;
    }

    public Question(int id, int cellNumber, String questionText) {
        this.id = id;
        this.cellNumber = cellNumber;
        this.questionText = questionText;
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
}
