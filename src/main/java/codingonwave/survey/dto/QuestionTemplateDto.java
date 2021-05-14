package codingonwave.survey.dto;

import codingonwave.survey.domain.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class QuestionTemplateDto {

    private Long id;

    private String value;

    private QuestionType questionType;

    private List<AnswerTemplateDto> answerTemplateList = new ArrayList<>();

    private CategoryTemplateDto category;


    public QuestionTemplateDto(Long id, String value, QuestionType questionType,
                               List<AnswerTemplateDto> answerTemplateList, CategoryTemplateDto category) {
        this.id = id;
        this.value = value;
        this.questionType = questionType;
        this.answerTemplateList = answerTemplateList;
        this.category = category;
    }

    public QuestionTemplateDto(String value, QuestionType questionType,
                               List<AnswerTemplateDto> answerTemplateList, CategoryTemplateDto category) {
        this.value = value;
        this.questionType = questionType;
        this.answerTemplateList = answerTemplateList;
        this.category = category;
    }


}
