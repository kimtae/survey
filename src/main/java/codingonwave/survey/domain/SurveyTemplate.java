package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class SurveyTemplate {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "surveyTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTemplate> questionTemplateList = new ArrayList<>();

    private Boolean active;

    public void addQuestionTemplate(QuestionTemplate questionTemplate) {
        questionTemplate.setSurveyTemplate(this);
        questionTemplateList.add(questionTemplate);
    }

    public void removeQuestionTemplate(QuestionTemplate questionTemplate) {
        questionTemplate.setSurveyTemplate(null);
        questionTemplateList.remove(questionTemplate);
    }

    public List<QuestionTemplate> getQuestionTemplateList() {
        return questionTemplateList;
    }

    public Long getId() {
        return id;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isActive() {
        return active;
    }
}
