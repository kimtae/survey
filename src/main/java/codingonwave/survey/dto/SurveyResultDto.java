package codingonwave.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyResultDto {

    private String username;

    private LocalTime createdAt;

    private List<SurveyResultDetailDto> surveyResultDetails = new ArrayList<>();

    private List<SurveyResultScoreDto> surveyResultScores = new ArrayList<>();

    public SurveyResultDto(String username, LocalTime createdAt) {
        this.username = username;
        this.createdAt = createdAt;
    }
}
