package codingonwave.survey.repository;

import codingonwave.survey.domain.*;
import codingonwave.survey.dto.*;
import codingonwave.survey.module.SurveyModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SurveyResultRepositoryTest {

    @Autowired
    CategoryTemplateRepository categoryTemplateRepository;

    @Autowired
    SurveyTemplateRepository surveyTemplateRepository;

    @Autowired
    SurveyResultRepository sut;

    @Autowired
    SurveyModule surveyModule;

    @BeforeEach
    void beforeEach() {
        CategoryTemplateDto categoryTemplate1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto categoryTemplate2 = new CategoryTemplateDto("category2");
        categoryTemplateRepository.save(categoryTemplate1);
        categoryTemplateRepository.save(categoryTemplate2);

        CategoryTemplateDto category1 = categoryTemplateRepository.findByName("category1");
        CategoryTemplateDto category2 = categoryTemplateRepository.findByName("category2");

        QuestionTemplateDto questionTemplate1 = new QuestionTemplateDto("question1", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 10), new AnswerTemplateDto("no", 0)),
                category1);

        QuestionTemplateDto questionTemplate2 = new QuestionTemplateDto("question2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 20), new AnswerTemplateDto("no", 0)),
                category1);

        QuestionTemplateDto questionTemplate3 = new QuestionTemplateDto("question3", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                category2);

        QuestionTemplateDto questionTemplate4 = new QuestionTemplateDto("question2", QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 200), new AnswerTemplateDto("no", 0)),
                category2);

        SurveyTemplateDto surveyTemplate = new SurveyTemplateDto(Arrays.asList(questionTemplate1, questionTemplate2,
                questionTemplate3, questionTemplate4));

        surveyTemplateRepository.save(surveyTemplate);

        List<SurveyTemplateDto> surveyTemplates = surveyTemplateRepository.findAll();
        surveyTemplateRepository.setActive(surveyTemplates.get(0).getId());

    }

    @AfterEach
    void afterEach() {
        surveyTemplateRepository.removeAll();
        categoryTemplateRepository.removeAll();
    }

    @Test
    void sut_correctly_save_survey_result() {
        //given
        String username = "user";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);
        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);

        //when
        sut.save(username, survey);

    }

    @Test
    void sut_correctly_find_survey_result_by_name() {
        //given
        String username = "userA";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);
        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);

        sut.save(username, survey);

        //when
        SurveyResultDto actual = sut.findByUsername(username);

        //then
        assertThat(actual.getUsername()).isEqualTo(username);
    }

    @Test
    void sut_correctly_save_survey_details() {
        //given
        String username = "user";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);

        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);

        //when
        sut.save(username, survey);
        SurveyResultDto actual = sut.findByUsername(username);

        //then
        assertThat(actual.getUsername()).isEqualTo(username);
        assertThat(actual.getSurveyResultDetails().size()).isEqualTo(survey.getQuestionList().size());
    }

    @Test
    void sut_correctly_save_survey_details_info() {
        //given
        String username = "user";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);

        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);

        //when
        sut.save(username, survey);
        SurveyResultDto actual = sut.findByUsername(username);

        //then
        List<Question> questionList = survey.getQuestionList();
        List<SurveyResultDetailDto> surveyResultDetails = actual.getSurveyResultDetails();

        for (int i=0; i<questionList.size(); i++) {
            Question question = questionList.get(i);
            SurveyResultDetailDto detail = surveyResultDetails.get(i);

            assertThat(detail.getQuestion()).isEqualTo(question.getValue());
            assertThat(detail.getCategoryName()).isEqualTo(question.getCategory().getName());
            assertThat(detail.getAnswer()).isEqualTo(question.getSelectedAnswer().getText());
        }
    }

    @Test
    void sut_correctly_save_survey_scores() {
        //given
        String username = "user";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);

        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);
        Map<String, Integer> scoreMap = survey.calculateScore();

        //when
        sut.save(username, survey);
        SurveyResultDto actual = sut.findByUsername(username);

        //then
        assertThat(actual.getSurveyResultScores().size()).isEqualTo(scoreMap.keySet().size());
    }

    @Test
    void sut_correctly_save_survey_scores_info() {
        //given
        String username = "user";
        surveyModule.start(username);
        Survey survey = surveyModule.surveyOf(username);

        survey.answer(0, 0);
        survey.answer(1, 0);
        survey.answer(2, 0);
        survey.answer(3, 0);
        Map<String, Integer> scoreMap = survey.calculateScore();

        //when
        sut.save(username, survey);
        SurveyResultDto actual = sut.findByUsername(username);
        List<SurveyResultScoreDto> surveyResultScores = actual.getSurveyResultScores();

        //then
        for (SurveyResultScoreDto surveyResultScore : surveyResultScores) {
            assertThat(scoreMap.containsKey(surveyResultScore.getCategoryName())).isTrue();
            assertThat(surveyResultScore.getScore()).isEqualTo(scoreMap.get(surveyResultScore.getCategoryName()));
        }
    }
}