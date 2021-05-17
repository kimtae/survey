package codingonwave.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SurveyResultScoreDto {

    private String categoryName;

    private Integer score;

    public SurveyResultScoreDto(String categoryName, Integer score) {
        this.categoryName = categoryName;
        this.score = score;
    }
}
