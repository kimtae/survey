package codingonwave.survey.repository;

import codingonwave.survey.dto.CategoryTemplateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryTemplateRepositoryTest {

    @Autowired CategoryTemplateRepository sut;

    @Test
    void save() {
        //given
        CategoryTemplateDto category = new CategoryTemplateDto("category");

        //when
        sut.save(category);

        //then
        List<CategoryTemplateDto> categories = sut.findAll();
        assertThat(categories.size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        //given
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");

        //when
        sut.save(category1);
        sut.save(category2);
        List<CategoryTemplateDto> categories = sut.findAll();

        //then
        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");
        sut.save(category1);
        sut.save(category2);
        List<CategoryTemplateDto> categories = sut.findAll();

        CategoryTemplateDto savedCategory1 = categories.get(0);
        CategoryTemplateDto savedCategory2 = categories.get(1);

        //when
        CategoryTemplateDto findCategory1 = sut.findById(savedCategory1.getId());
        CategoryTemplateDto findCategory2 = sut.findById(savedCategory2.getId());

        //then
        assertThat(findCategory1.getName()).isEqualTo(savedCategory1.getName());
        assertThat(findCategory2.getName()).isEqualTo(savedCategory2.getName());

    }

    @Test
    void findByName() {
        //given
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");
        sut.save(category1);
        sut.save(category2);

        //when
        CategoryTemplateDto findCategory1 = sut.findByName("category1");
        CategoryTemplateDto findCategory2 = sut.findByName("category2");

        //then
        assertThat(findCategory1.getName()).isEqualTo(category1.getName());
        assertThat(findCategory2.getName()).isEqualTo(category2.getName());
    }

    @Test
    void update() {
        //given
        CategoryTemplateDto categoryTemplate = new CategoryTemplateDto("category");
        sut.save(categoryTemplate);
        CategoryTemplateDto savedCategoryTemplate = sut.findByName("category");

        //when
        sut.update(savedCategoryTemplate.getId(), "new-category");
        CategoryTemplateDto findCategoryTemplate = sut.findById(savedCategoryTemplate.getId());

        //then
        assertThat(findCategoryTemplate.getName()).isEqualTo("new-category");
    }
}