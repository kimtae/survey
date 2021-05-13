package codingonwave.survey.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryTemplateDto {

    private Long id;

    private String name;

    public CategoryTemplateDto(String name) {
        this.name = name;
    }

    @QueryProjection
    public CategoryTemplateDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
