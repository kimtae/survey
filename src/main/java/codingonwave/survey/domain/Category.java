package codingonwave.survey.domain;

import codingonwave.survey.dto.CategoryTemplateDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Category {

    private String name;

    public static Category from(CategoryTemplateDto categoryTemplate) {
        return new Category(categoryTemplate.getName());
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
