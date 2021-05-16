package codingonwave.survey.domain;

import codingonwave.survey.dto.AnswerTemplateDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
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
        setSelected(true);
    }

    public void unselect() {
        setSelected(false);
    }

    public Boolean isSelected() {
        return getSelected();
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getSelected() {
        return isSelected;
    }
}
