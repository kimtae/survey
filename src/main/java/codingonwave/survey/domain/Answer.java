package codingonwave.survey.domain;

import codingonwave.survey.dto.AnswerTemplateDto;

public class Answer {

    private String text;

    private Integer score;

    private Boolean isSelected = false;

    public static Answer from(AnswerTemplateDto answerTemplate) {
        return new Answer(answerTemplate.getText(), answerTemplate.getScore());
    }

    public Answer(String text, Integer score) {
        this.text = text;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public Integer getScore() {
        return score;
    }

    public void select() {
        isSelected = true;
    }

    public void unselect() {
        isSelected = false;
    }

    public Boolean isSelected() {
        return isSelected;
    }
}
