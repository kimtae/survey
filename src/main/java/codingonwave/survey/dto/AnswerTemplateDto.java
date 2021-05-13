package codingonwave.survey.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class AnswerTemplateDto {

    private Long id;

    private String text;

    private Integer score;

    public AnswerTemplateDto(Long id, String text, Integer score) {
        this.id = id;
        this.text = text;
        this.score = score;
    }

    public AnswerTemplateDto(String text, Integer score) {
        this.text = text;
        this.score = score;
    }
}
