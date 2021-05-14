package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class QuestionTemplate {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_template_id")
    private SurveyTemplate surveyTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_template_id")
    private CategoryTemplate categoryTemplate;

    @OneToMany(mappedBy = "questionTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerTemplate> answerTemplateList = new ArrayList<>();

    public void addAnswerTemplate(AnswerTemplate answerTemplate) {
        answerTemplate.setQuestionTemplate(this);
        answerTemplateList.add(answerTemplate);
    }

    public void removeAnswerTemplate(AnswerTemplate answerTemplate) {
        answerTemplate.setQuestionTemplate(null);
        answerTemplateList.remove(answerTemplate);
    }

    public QuestionTemplate(String value, QuestionType questionType, CategoryTemplate categoryTemplate) {
        this.value = value;
        this.questionType = questionType;
        this.categoryTemplate = categoryTemplate;
    }

    public void setSurveyTemplate(SurveyTemplate surveyTemplate) {
        this.surveyTemplate = surveyTemplate;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public CategoryTemplate getCategoryTemplate() {
        return categoryTemplate;
    }

    public List<AnswerTemplate> getAnswerTemplateList() {
        return answerTemplateList;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public SurveyTemplate getSurveyTemplate() {
        return surveyTemplate;
    }
}
