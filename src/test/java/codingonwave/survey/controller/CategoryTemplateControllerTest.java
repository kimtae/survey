package codingonwave.survey.controller;

import codingonwave.survey.dto.CategoryTemplateDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryTemplateControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        save_test_data();
    }

    @AfterEach
    void after() {
        clear_test_data();
    }

    void save_test_data() {
        CategoryTemplateDto testData1 = new CategoryTemplateDto("test-category1");
        CategoryTemplateDto testData2 = new CategoryTemplateDto("test-category2");

        given().contentType("application/json").body(testData1).when().post("/category-template");
        given().contentType("application/json").body(testData2).when().post("/category-template");

    }

    void clear_test_data() {
        when().get("/category-template/remove");
    }

    @Test
    void sut_correctly_save() {
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");

        given().
                log().
                all().
                contentType("application/json").
                body(category1).
        when().
                post("/category-template").
        then().
                statusCode(200);
    }

    @Test
    void sut_correctly_find_all_category_templates() {
        given().
                log().
                all().
        when().
                get("/category-template").
        then().
                statusCode(200).
                assertThat().
                body("size()", is(2));
    }

    @Test
    void sut_correctly_update_category_name() {
        //given
        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);
        CategoryTemplateDto categoryTemplate = categoryTemplates[0];

        //when
        given().
                log().all().
                pathParam("categoryTemplateId", categoryTemplate.getId()).
                param("name", "new-category").
        when().
                patch("/category-template/{categoryTemplateId}").
        then().
                statusCode(200);


        CategoryTemplateDto findCategoryTemplate = given().
                pathParam("categoryTemplateId", categoryTemplate.getId()).
                when().get("/category-template/{categoryTemplateId}").as(CategoryTemplateDto.class);

        //then
        assertThat(findCategoryTemplate.getId()).isEqualTo(categoryTemplate.getId());
        assertThat(categoryTemplate.getName()).isEqualTo("test-category1");
        assertThat(findCategoryTemplate.getName()).isEqualTo("new-category");
    }

    @Test
    void sut_correctly_remove_category_template() {
        //given
        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);
        CategoryTemplateDto categoryTemplate = categoryTemplates[0];

        //when
        given().log().all()
                .pathParam("categoryTemplateId", categoryTemplate.getId()).
        when().
                delete("/category-template/{categoryTemplateId}").
        then().
                statusCode(200);

        CategoryTemplateDto[] findCategoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);

        //then
        assertThat(categoryTemplates.length).isEqualTo(2);
        assertThat(findCategoryTemplates.length).isEqualTo(1);
    }
}