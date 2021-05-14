package codingonwave.survey.controller;

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
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SurveyTemplateControllerTest {

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
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        given().contentType("application/json").body(category1).post("/category-template");
        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);

        QuestionTemplateDto question1 = new QuestionTemplateDto("question1", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        QuestionTemplateDto question2 = new QuestionTemplateDto("question2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        SurveyTemplateDto surveyTemplate = new SurveyTemplateDto(Arrays.asList(question1, question2));

        given().contentType("application/json").body(surveyTemplate).post("survey-template");

    }

    void clear_test_data() {
        when().get("/survey-template/remove");
        when().get("/category-template/remove");
    }

    @Test
    void sut_correctly_find_all_survey_template() {
        given().log().all().
        when().
                get("/survey-template").
        then().
                statusCode(200).
                assertThat().
                body("size()", is(1));

    }

    @Test
    void sut_correctly_save_survey_template() {
        //given
        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);

        QuestionTemplateDto question1 = new QuestionTemplateDto("new-question-1", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        QuestionTemplateDto question2 = new QuestionTemplateDto("new-question-2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        SurveyTemplateDto surveyTemplate = new SurveyTemplateDto(Arrays.asList(question1, question2));

        //when
        given().
                log().all().
                contentType("application/json").
                body(surveyTemplate).
        when().
                post("/survey-template").
        then().
                statusCode(200);

        //then
        given().
                log().all().
        when().
                get("/survey-template").
        then().
                statusCode(200).
                assertThat().
                body("size()", is(2));

    }

    @Test
    void sut_correctly_add_new_question() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);

        CategoryTemplateDto[] categoryTemplates = when().get("/category-template").as(CategoryTemplateDto[].class);

        QuestionTemplateDto newQuestionTemplate = new QuestionTemplateDto("new-question", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                categoryTemplates[0]);

        //when
        given().log().all().
                contentType("application/json").
                pathParam("surveyTemplateId", surveyTemplates[0].getId()).
                body(newQuestionTemplate).
        when().
                post("/survey-template/{surveyTemplateId}/question-template").
        then().
                statusCode(200);

        //then
        SurveyTemplateDto findSurveyTemplate = given().log().all().
                pathParam("surveyTemplateId", surveyTemplates[0].getId()).
                when().
                get("/survey-template/{surveyTemplateId}").
                as(SurveyTemplateDto.class);

        assertThat(surveyTemplates[0].getQuestionTemplateList().size()).isEqualTo(2);
        assertThat(findSurveyTemplate.getQuestionTemplateList().size()).isEqualTo(3);

        List<String> textQuestionValues = findSurveyTemplate.getQuestionTemplateList()
                .stream().map(QuestionTemplateDto::getValue)
                .collect(Collectors.toList());

        assertThat(textQuestionValues).contains("new-question");

    }

    @Test
    void sut_correctly_update_question() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto surveyTemplate = surveyTemplates[0];
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);

        //when
        given().log().all().
                pathParam("questionTemplateId", questionTemplate.getId()).
                param("questionType", "TEXT").
                param("questionValue", "update-question").
        when().
                patch("/survey-template/question-template/{questionTemplateId}").
        then().
                statusCode(200);

        //then
        SurveyTemplateDto findSurveyTemplate =
                given().pathParam("surveyTemplateId", surveyTemplate.getId()).
                when().get("/survey-template/{surveyTemplateId}").as(SurveyTemplateDto.class);

        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList()
                .stream().filter(template -> template.getId().equals(questionTemplate.getId())).findFirst().orElse(null);

        assertThat(findQuestionTemplate).isNotNull();
        assertThat(findQuestionTemplate.getValue()).isEqualTo("update-question");
    }

    @Test
    void sut_correctly_remove_question() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto surveyTemplate = surveyTemplates[0];
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);

        //when
        given().
                log().all().
                pathParam("questionTemplateId", questionTemplate.getId()).
        when().
                delete("/survey-template/question-template/{questionTemplateId}").
        then().
                statusCode(200);

        SurveyTemplateDto findSurveyTemplate = given().pathParam("surveyTemplateId", surveyTemplate.getId()).
                when().get("/survey-template/{surveyTemplateId}").as(SurveyTemplateDto.class);

        //then
        assertThat(surveyTemplate.getQuestionTemplateList().size()).isEqualTo(2);
        assertThat(findSurveyTemplate.getQuestionTemplateList().size()).isEqualTo(1);
    }

    @Test
    void sut_correctly_add_answer() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto surveyTemplate = surveyTemplates[0];
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);

        AnswerTemplateDto another = new AnswerTemplateDto("another", 200);

        //when
        given().
                log().all().
                pathParam("questionTemplateId", questionTemplate.getId()).
                contentType("application/json").
                body(another).
        when().
                post("/survey-template/question-template/{questionTemplateId}/answer-template").
        then().
                statusCode(200);


        SurveyTemplateDto findSurveyTemplate = given().
                pathParam("surveyTemplateId", surveyTemplate.getId()).
                when().get("/survey-template/{surveyTemplateId}").
                as(SurveyTemplateDto.class);

        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList()
                .stream().filter(template -> template.getId().equals(questionTemplate.getId()))
                .findFirst().orElse(null);

        //then
        assertThat(findQuestionTemplate).isNotNull();
        assertThat(questionTemplate.getAnswerTemplateList().size()).isEqualTo(2);
        assertThat(findQuestionTemplate.getAnswerTemplateList().size()).isEqualTo(3);
    }

    @Test
    void sut_correctly_update_answer() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto surveyTemplate = surveyTemplates[0];
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);
        AnswerTemplateDto answerTemplate = questionTemplate.getAnswerTemplateList().get(0);

        //when
        given().log().all().
                pathParam("answerTemplateId", answerTemplate.getId()).
                param("text", "new-option").
                param("score", 1000).
        when().
                patch("/survey-template/question-template/answer-template/{answerTemplateId}").
        then().
                statusCode(200);

        SurveyTemplateDto findSurveyTemplate = given().
                pathParam("surveyTemplateId", surveyTemplate.getId()).
                when().get("/survey-template/{surveyTemplateId}").
                as(SurveyTemplateDto.class);

        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList().stream().
                filter(template -> template.getId().equals(questionTemplate.getId())).findFirst().orElse(null);

        assertThat(findQuestionTemplate).isNotNull();

        AnswerTemplateDto findAnswerTemplate = findQuestionTemplate.getAnswerTemplateList().stream()
                .filter(template -> template.getId().equals(answerTemplate.getId())).findFirst().orElse(null);

        //then
        assertThat(findAnswerTemplate).isNotNull();
        assertThat(findAnswerTemplate.getText()).isEqualTo("new-option");
        assertThat(findAnswerTemplate.getScore()).isEqualTo(1000);

    }

    @Test
    void sut_correctly_remove_answer() {
        //given
        SurveyTemplateDto[] surveyTemplates = when().get("/survey-template").as(SurveyTemplateDto[].class);
        SurveyTemplateDto surveyTemplate = surveyTemplates[0];
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);
        AnswerTemplateDto answerTemplate = questionTemplate.getAnswerTemplateList().get(0);

        //when
        given().log().all().
                pathParam("answerTemplateId", answerTemplate.getId()).
        when().
                delete("/survey-template/question-template/answer-template/{answerTemplateId}").
        then().
                statusCode(200);

        SurveyTemplateDto findSurveyTemplate = given().
                pathParam("surveyTemplateId", surveyTemplate.getId()).
                when().get("/survey-template/{surveyTemplateId}").
                as(SurveyTemplateDto.class);

        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList().stream().
                filter(template -> template.getId().equals(questionTemplate.getId())).findFirst().orElse(null);

        assertThat(findQuestionTemplate).isNotNull();

        //then
        assertThat(questionTemplate.getAnswerTemplateList().size()).isEqualTo(2);
        assertThat(findQuestionTemplate.getAnswerTemplateList().size()).isEqualTo(1);

    }
}