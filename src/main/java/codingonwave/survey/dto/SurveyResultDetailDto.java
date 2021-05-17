package codingonwave.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SurveyResultDetailDto {

    private String question;

    private String answer;

    private String categoryName;

    public SurveyResultDetailDto(String question, String answer, String categoryName) {
        this.question = question;
        this.answer = answer;
        this.categoryName = categoryName;
    }
}
