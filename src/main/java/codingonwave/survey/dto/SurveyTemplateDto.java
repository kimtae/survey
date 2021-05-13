package codingonwave.survey.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SurveyTemplateDto {

    private Long id;

    private List<QuestionTemplateDto> questionTemplateList = new ArrayList<>();

    public SurveyTemplateDto(Long id, List<QuestionTemplateDto> questionTemplateList) {
        this.id = id;
        this.questionTemplateList = questionTemplateList;
    }

    public SurveyTemplateDto(List<QuestionTemplateDto> questionTemplateList) {
        this.questionTemplateList = questionTemplateList;
    }
}
