package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class SurveyResultDetail {

    @Id @GeneratedValue
    private Long id;

    private String question;

    private String answer;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_result_id")
    private SurveyResult surveyResult;

    public SurveyResultDetail(String question, String answer, String categoryName) {
        this.question = question;
        this.answer = answer;
        this.categoryName = categoryName;
    }

    public void setSurveyResult(SurveyResult surveyResult) {
        this.surveyResult = surveyResult;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
