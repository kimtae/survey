package codingonwave.survey.controller;

import codingonwave.survey.domain.Question;
import codingonwave.survey.domain.QuestionType;
import codingonwave.survey.dto.AnswerTemplateDto;
import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.dto.QuestionTemplateDto;
import codingonwave.survey.dto.SurveyTemplateDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.util.Arrays;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
        save_test_data();
    }

    @AfterEach
    void afterEach() {
        clear_test_data();
    }

    void save_test_data() {
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        given().contentType("application/json").body(category1).post("/category-template");
        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);

        QuestionTemplateDto question1 = new QuestionTemplateDto("question1", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        QuestionTemplateDto question2 = new QuestionTemplateDto("question2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("no", 0), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        SurveyTemplateDto surveyTemplate1 = new SurveyTemplateDto(Arrays.asList(question1, question2));

        QuestionTemplateDto question3 = new QuestionTemplateDto("question1", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        QuestionTemplateDto question4 = new QuestionTemplateDto("question2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("no", 0), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        SurveyTemplateDto surveyTemplate2 = new SurveyTemplateDto(Arrays.asList(question3, question4));

        given().contentType("application/json").body(surveyTemplate1).post("/survey-template");
        given().contentType("application/json").body(surveyTemplate2).post("/survey-template");

        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto savedSurveyTemplate = surveyTemplates[0];

        given()
                .pathParam("surveyTemplateId", savedSurveyTemplate.getId()).
        when()
                .patch("/survey-template/{surveyTemplateId}/active");

    }

    void clear_test_data() {
        when().get("/survey/clear");
        when().get("/survey-template/remove");
        when().get("/category-template/remove");
    }

    @Test
    void sut_correctly_start_survey() {
        given().
                log().all().
                param("username", "user").
        when().
                post("/survey").
        then().
                statusCode(200);
    }

    @Test
    void sut_correctly_check_duplicate_username() {
        //given
        given().
                param("username", "user").
        when().
                post("/survey").
        then().
                statusCode(200);

        //when
        String message = given().
                                param("username", "user").
                        when().
                                post("/survey").
                        //then
                        then().
                                statusCode(400).
                                extract().
                                asString();

        assertThat(message).isEqualTo("duplicate username");

    }

    @Test
    void sut_correctly_set_username_at_cookie() {
        given().
                param("username", "user").
        when().
                post("/survey").
        then().
                statusCode(200).
                cookie("username", "user");

    }

    @Test
    void sut_correctly_get_question_by_index() {
        //given
        given().param("username", "user").when().post("/survey");

        //when
        given().
                log().all().
                pathParam("questionIndex", 0).
                cookie("username", "user").
        when().
                get("/survey/question/{questionIndex}").
        //then
        then().
                statusCode(200).
                assertThat().
                body("value", is("question1")).
                body("answerList.size()", is(2));
    }

    @Test
    void sut_correctly_answer_to_a_question() {
        //given
        given().param("username", "user").when().post("/survey");

        //when
        given().
                log().all().
                cookie("username", "user").
                param("questionIndex", 0).
                param("answerIndex", 0).
        when().
                post("/survey/answer").
        //then
        then().
                statusCode(200);
    }

    @Test
    void sut_correctly_set_answer_to_question() {
        //given
        given().param("username", "user").when().post("/survey");
        given().cookie("username", "user").
                param("questionIndex", 0).
                param("answerIndex", 0).
        when().post("/survey/answer");

        //when
        Question question = given().
                                    log().all().
                                    pathParam("questionIndex", 0).
                                    cookie("username", "user").
                            when().
                                    get("/survey/question/{questionIndex}").
                                    as(Question.class);
        //then
        assertThat(question.getAnswer(0).isSelected()).isTrue();
        assertThat(question.getSelectedAnswer().getText()).isEqualTo(question.getAnswer(0).getText());
        assertThat(question.getAnswer(1).isSelected()).isFalse();
    }
}