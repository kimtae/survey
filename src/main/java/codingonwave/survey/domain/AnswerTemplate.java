package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class AnswerTemplate {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_template_id")
    private QuestionTemplate questionTemplate;

    public AnswerTemplate(String text, Integer score) {
        this.text = text;
        this.score = score;
    }

    public void setQuestionTemplate(QuestionTemplate questionTemplate) {
        this.questionTemplate = questionTemplate;
    }

    public QuestionTemplate getQuestionTemplate() {
        return questionTemplate;
    }

    public String getText() {
        return text;
    }

    public Integer getScore() {
        return score;
    }

    public Long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
