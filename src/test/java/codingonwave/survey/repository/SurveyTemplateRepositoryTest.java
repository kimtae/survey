package codingonwave.survey.repository;

import codingonwave.survey.domain.QuestionType;
import codingonwave.survey.dto.AnswerTemplateDto;
import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.dto.QuestionTemplateDto;
import codingonwave.survey.dto.SurveyTemplateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SurveyTemplateRepositoryTest {

    @Autowired SurveyTemplateRepository sut;
    @Autowired CategoryTemplateRepository categoryRepository;

    @BeforeEach
    void beforeEach() {
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    SurveyTemplateDto makeNewSurveyTemplateDto() {
        CategoryTemplateDto category1 = categoryRepository.findByName("category1");
        List<AnswerTemplateDto> answerList1 = Arrays.asList(
                new AnswerTemplateDto("yes", 10),
                new AnswerTemplateDto("no", 0));

        QuestionTemplateDto question1 = new QuestionTemplateDto(
                "question1",
                QuestionType.TEXT,
                answerList1,
                category1);


        CategoryTemplateDto category2 = categoryRepository.findByName("category2");
        List<AnswerTemplateDto> answerList2 = Arrays.asList(
                new AnswerTemplateDto("check", 20),
                new AnswerTemplateDto("uncheck", 0));

        QuestionTemplateDto question2 = new QuestionTemplateDto(
                null,
                "www.naver.com",
                QuestionType.IMAGE,
                answerList2,
                category2);

        return new SurveyTemplateDto(Arrays.asList(question1, question2));
    }

    QuestionTemplateDto makeQuestionTemplateDto() {
        CategoryTemplateDto category = categoryRepository.findByName("category1");

        return new QuestionTemplateDto(
                "new question",
                QuestionType.TEXT,
                Arrays.asList(new AnswerTemplateDto("yes", 100), new AnswerTemplateDto("no", 0)),
                category);
    }

    @Test
    void sut_correctly_save_survey_template() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();

        //when
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> actual = sut.findAll();

        //then
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void sut_correctly_find_all_survey_template() {
        //given
        SurveyTemplateDto surveyTemplate1 = makeNewSurveyTemplateDto();
        SurveyTemplateDto surveyTemplate2 = makeNewSurveyTemplateDto();

        sut.save(surveyTemplate1);
        sut.save(surveyTemplate2);

        //when
        List<SurveyTemplateDto> actual = sut.findAll();

        //then
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void sut_correctly_find_survey_template_by_id() {
        //given
        SurveyTemplateDto surveyTemplate1 = makeNewSurveyTemplateDto();
        SurveyTemplateDto surveyTemplate2 = makeNewSurveyTemplateDto();

        sut.save(surveyTemplate1);
        sut.save(surveyTemplate2);

        List<SurveyTemplateDto> surveyTemplates = sut.findAll();
        SurveyTemplateDto savedSurveyTemplate1 = surveyTemplates.get(0);
        SurveyTemplateDto savedSurveyTemplate2 = surveyTemplates.get(1);

        //when
        SurveyTemplateDto findSurveyTemplate1 = sut.findById(savedSurveyTemplate1.getId());
        SurveyTemplateDto findSurveyTemplate2 = sut.findById(savedSurveyTemplate2.getId());

        //then
        assertThat(findSurveyTemplate1.getId()).isEqualTo(savedSurveyTemplate1.getId());
        assertThat(findSurveyTemplate2.getId()).isEqualTo(savedSurveyTemplate2.getId());
    }

    @Test
    void sut_correctly_set_active_survey_template() {
        //given
        SurveyTemplateDto surveyTemplate1 = makeNewSurveyTemplateDto();
        SurveyTemplateDto surveyTemplate2 = makeNewSurveyTemplateDto();

        sut.save(surveyTemplate1);
        sut.save(surveyTemplate2);

        List<SurveyTemplateDto> surveyTemplates = sut.findAll();
        SurveyTemplateDto savedSurveyTemplate = surveyTemplates.get(0);

        //when
        sut.setActive(savedSurveyTemplate.getId());
        SurveyTemplateDto activeSurveyTemplate = sut.findActiveSurveyTemplate();

        //then
        assertThat(activeSurveyTemplate.getId()).isEqualTo(savedSurveyTemplate.getId());
    }

    @Test
    void sut_correctly_remove_survey_template() {
        //given
        SurveyTemplateDto surveyTemplate1 = makeNewSurveyTemplateDto();
        SurveyTemplateDto surveyTemplate2 = makeNewSurveyTemplateDto();

        sut.save(surveyTemplate1);
        sut.save(surveyTemplate2);

        List<SurveyTemplateDto> surveyTemplates = sut.findAll();
        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);

        //when
        sut.remove(targetSurveyTemplate);
        List<SurveyTemplateDto> findSurveyTemplates = sut.findAll();

        //then
        assertThat(findSurveyTemplates.size()).isEqualTo(1);
    }

    @Test
    void sut_correctly_add_question() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();
        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);
        QuestionTemplateDto newQuestionTemplate = makeQuestionTemplateDto();

        //when
        sut.addQuestion(targetSurveyTemplate, newQuestionTemplate);
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());

        //then
        assertThat(findSurveyTemplate.getQuestionTemplateList().size()).isEqualTo(3);
    }

    @Test
    void sut_correctly_modify_question() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();

        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);

        //when
        targetSurveyTemplate.getQuestionTemplateList()
                .forEach(template -> sut.updateQuestion(template, QuestionType.TEXT, "update"));

        //then
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());
        for (QuestionTemplateDto dto : findSurveyTemplate.getQuestionTemplateList()) {
            assertThat(dto.getValue()).isEqualTo("update");
        }
    }

    @Test
    void sut_correctly_delete_question() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();

        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);
        QuestionTemplateDto targetQuestionTemplate = targetSurveyTemplate.getQuestionTemplateList().get(0);

        //when
        sut.removeQuestion(targetQuestionTemplate);
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());

        //then
        assertThat(findSurveyTemplate.getQuestionTemplateList().size()).isEqualTo(1);
    }

    @Test
    void sut_correctly_add_answer() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();

        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);
        QuestionTemplateDto targetQuestionTemplate = targetSurveyTemplate.getQuestionTemplateList().get(0);

        AnswerTemplateDto newAnswerTemplate = new AnswerTemplateDto("new answer", 100);

        //when
        sut.addAnswer(targetQuestionTemplate, newAnswerTemplate);
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());
        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList().get(0);

        //then
        assertThat(findQuestionTemplate.getAnswerTemplateList().size()).isEqualTo(3);
    }

    @Test
    void sut_correctly_update_answer() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();

        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);
        QuestionTemplateDto targetQuestionTemplate = targetSurveyTemplate.getQuestionTemplateList().get(0);

        AnswerTemplateDto targetAnswerTemplate = targetQuestionTemplate.getAnswerTemplateList().get(0);

        //when
        sut.updateAnswer(targetAnswerTemplate, "update", 100);
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());
        AnswerTemplateDto findAnswerTemplate = findSurveyTemplate.getQuestionTemplateList().get(0)
                .getAnswerTemplateList().get(0);

        //then
        assertThat(findAnswerTemplate.getText()).isEqualTo("update");
        assertThat(findAnswerTemplate.getScore()).isEqualTo(100);
    }

    @Test
    void sut_correctly_remove_answer() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();
        sut.save(surveyTemplate);
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();

        SurveyTemplateDto targetSurveyTemplate = surveyTemplates.get(0);
        QuestionTemplateDto targetQuestionTemplate = targetSurveyTemplate.getQuestionTemplateList().get(0);

        AnswerTemplateDto targetAnswerTemplate = targetQuestionTemplate.getAnswerTemplateList().get(0);

        //when
        sut.removeAnswer(targetAnswerTemplate);
        SurveyTemplateDto findSurveyTemplate = sut.findById(targetSurveyTemplate.getId());
        QuestionTemplateDto findQuestionTemplate = findSurveyTemplate.getQuestionTemplateList().get(0);

        //then
        assertThat(findQuestionTemplate.getAnswerTemplateList().size()).isEqualTo(1);
    }

    @Test
    void sut_correctly_get_question_category() {
        //given
        SurveyTemplateDto surveyTemplate = makeNewSurveyTemplateDto();

        //when
        sut.save(surveyTemplate);

        //then
        List<SurveyTemplateDto> surveyTemplates = sut.findAll();
        for (SurveyTemplateDto savedSurveyTemplate : surveyTemplates) {
            List<QuestionTemplateDto> questionTemplateList = savedSurveyTemplate.getQuestionTemplateList();
            for (QuestionTemplateDto questionTemplate : questionTemplateList) {
                assertThat(questionTemplate.getCategory().getName()).isNotEmpty();
            }
        }
    }
}