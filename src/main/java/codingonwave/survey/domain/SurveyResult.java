package codingonwave.survey.domain;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SurveyResult {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private LocalTime createdAt = LocalTime.now();

    @OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL)
    private List<SurveyResultDetail> surveyResultDetails = new ArrayList<>();

    @OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL)
    private List<SurveyResultScore> surveyResultScores = new ArrayList<>();


    public static SurveyResult of(String username) {
        SurveyResult surveyResult = new SurveyResult();
        surveyResult.setUsername(username);
        return surveyResult;
    }

    public void addSurveyResultDetail(SurveyResultDetail surveyResultDetail) {
        surveyResultDetail.setSurveyResult(this);
        surveyResultDetails.add(surveyResultDetail);
    }

    public void addSurveyResultScore(SurveyResultScore surveyResultScore) {
        surveyResultScore.setSurveyResult(this);
        surveyResultScores.add(surveyResultScore);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SurveyResultDetail> getSurveyResultDetails() {
        return surveyResultDetails;
    }

    public List<SurveyResultScore> getSurveyResultScores() {
        return surveyResultScores;
    }

    public String getUsername() {
        return username;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }
}
