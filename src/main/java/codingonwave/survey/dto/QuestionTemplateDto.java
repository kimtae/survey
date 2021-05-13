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

    private String text;

    private String imageUrl;

    private QuestionType questionType;

    private List<AnswerTemplateDto> answerTemplateList = new ArrayList<>();

    private CategoryTemplateDto category;


    public QuestionTemplateDto(Long id, String text, String imageUrl, QuestionType questionType,
                               List<AnswerTemplateDto> answerTemplateList, CategoryTemplateDto category) {
        this.id = id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.questionType = questionType;
        this.answerTemplateList = answerTemplateList;
        this.category = category;
    }

    public QuestionTemplateDto(String text, String imageUrl, QuestionType questionType,
                               List<AnswerTemplateDto> answerTemplateList, CategoryTemplateDto category) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.questionType = questionType;
        this.answerTemplateList = answerTemplateList;
        this.category = category;
    }


}
