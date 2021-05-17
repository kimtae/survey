package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class SurveyResultScore {

    @Id @GeneratedValue
    private Long id;

    private String categoryName;

    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_result_id")
    private SurveyResult surveyResult;

    public SurveyResultScore(String categoryName, Integer score) {
        this.categoryName = categoryName;
        this.score = score;
    }

    public void setSurveyResult(SurveyResult surveyResult) {
        this.surveyResult = surveyResult;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getScore() {
        return score;
    }
}
