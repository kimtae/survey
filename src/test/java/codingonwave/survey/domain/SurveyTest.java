package codingonwave.survey.domain;

import codingonwave.survey.dto.AnswerTemplateDto;
import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.dto.QuestionTemplateDto;
import codingonwave.survey.dto.SurveyTemplateDto;
import codingonwave.survey.repository.CategoryTemplateRepository;
import codingonwave.survey.repository.SurveyTemplateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SurveyTest {

    @Autowired
    SurveyTemplateRepository surveyTemplateRepository;

    @Autowired
    CategoryTemplateRepository categoryTemplateRepository;

    @BeforeEach
    void before() {
        saveTestCategoryTemplate();
        saveTestSurveyTemplate();
    }

    @AfterEach
    void after() {
        surveyTemplateRepository.removeAll();
        categoryTemplateRepository.removeAll();
    }

    void saveTestCategoryTemplate() {
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");
        categoryTemplateRepository.save(category1);
        categoryTemplateRepository.save(category2);
    }

    void saveTestSurveyTemplate() {
        CategoryTemplateDto category1 = categoryTemplateRepository.findByName("category1");
        List<AnswerTemplateDto> answerList1 = Arrays.asList(
                new AnswerTemplateDto("yes", 10),
                new AnswerTemplateDto("no", 0));

        QuestionTemplateDto question1 = new QuestionTemplateDto(
                "question1",
                QuestionType.TEXT,
                answerList1,
                category1);


        CategoryTemplateDto category2 = categoryTemplateRepository.findByName("category2");
        List<AnswerTemplateDto> answerList2 = Arrays.asList(
                new AnswerTemplateDto("check", 20),
                new AnswerTemplateDto("uncheck", 0));

        QuestionTemplateDto question2 = new QuestionTemplateDto(
                null,
                "www.naver.com",
                QuestionType.IMAGE,
                answerList2,
                category2);

        SurveyTemplateDto surveyTemplate = new SurveyTemplateDto(Arrays.asList(question1, question2));
        surveyTemplateRepository.save(surveyTemplate);
    }

    @Test
    void sut_correctly_make_survey_from_template() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);

        //when
        sut.init(surveyTemplate);

        //then
        assertThat(sut.getQuestionList().size()).isEqualTo(surveyTemplate.getQuestionTemplateList().size());

    }

    @Test
    void sut_correctly_make_question() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        List<QuestionTemplateDto> questionTemplates = surveyTemplate.getQuestionTemplateList();
        List<String> questionTemplateValues = questionTemplates
                .stream().map(QuestionTemplateDto::getValue).collect(Collectors.toList());


        //when
        sut.init(surveyTemplate);
        List<String> questionValues = sut.getQuestionList()
                .stream().map(Question::getValue).collect(Collectors.toList());

        //then
        assertThat(questionValues).containsAll(questionTemplateValues);
    }

    @Test
    void sut_correctly_make_question_category() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);

        //when
        sut.init(surveyTemplate);
        Question firstQuestion = sut.getQuestionByIndex(0);
        Question secondQuestion = sut.getQuestionByIndex(1);

        //then
        assertThat(firstQuestion.getCategory().getName()).isEqualTo("category1");
        assertThat(secondQuestion.getCategory().getName()).isEqualTo("category2");
    }

    @Test
    void sut_correctly_make_answer() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        List<QuestionTemplateDto> questionTemplates = surveyTemplate.getQuestionTemplateList();

        //when
        sut.init(surveyTemplate);
        List<Question> questions = sut.getQuestionList();

        //then
        for (Question question : questions) {
            QuestionTemplateDto questionTemplate = questionTemplates.stream()
                    .filter(template -> template.getValue().equals(question.getValue()))
                    .findFirst()
                    .orElse(null);

            assertThat(questionTemplate).isNotNull();

            List<AnswerTemplateDto> answerTemplates = questionTemplate.getAnswerTemplateList();
            List<Answer> answers = question.getAnswerList();

            assertThat(answers.size()).isEqualTo(answerTemplates.size());

            for (Answer answer : answers) {
                AnswerTemplateDto answerTemplate = answerTemplates.stream()
                        .filter(template -> template.getText().equals(answer.getText())).findFirst().orElse(null);

                assertThat(answerTemplate).isNotNull();
                assertThat(answer.getScore()).isEqualTo(answerTemplate.getScore());
            }
        }
    }

    @Test
    void sut_correctly_get_question_by_index() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        QuestionTemplateDto questionTemplate = surveyTemplate.getQuestionTemplateList().get(0);

        sut.init(surveyTemplate);

        //when
        Question question = sut.getQuestionByIndex(0);

        //then
        assertThat(question.getValue()).isEqualTo(questionTemplate.getValue());
        assertThat(question.getAnswerList().size()).isEqualTo(questionTemplate.getAnswerTemplateList().size());
    }

    @Test
    void sut_correctly_answer_to_question() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        sut.init(surveyTemplate);

        Integer questionIndex = 0;
        Integer answerIndex = 0;

        //when
        sut.answer(questionIndex, answerIndex);
        Answer answer = sut.getSelectedAnswerOf(questionIndex);

        //then
        assertThat(answer.isSelected()).isTrue();
    }

    @Test
    void sut_correctly_answer_only_one_option() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        sut.init(surveyTemplate);

        Integer questionIndex = 0;
        Integer selectedAnswerIndex = 0;
        Integer unselectedAnswerIndex = 1;

        //when
        sut.answer(questionIndex, selectedAnswerIndex);
        sut.answer(questionIndex, unselectedAnswerIndex);

        sut.answer(questionIndex, selectedAnswerIndex);

        Answer selectedAnswer = sut.getSelectedAnswerOf(questionIndex);
        Answer unselectedAnswer = sut.getAnswer(questionIndex, unselectedAnswerIndex);

        //then
        assertThat(selectedAnswer.isSelected()).isTrue();
        assertThat(unselectedAnswer.isSelected()).isFalse();
    }

    @Test
    void sut_correctly_notify_finish_survey() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        sut.init(surveyTemplate);

        sut.getQuestionList().forEach(question -> question.answer(0));

        //when
        Boolean isFinish = sut.isFinish();

        //then
        assertThat(isFinish).isTrue();
    }

    @Test
    void sut_correctly_notify_not_finish_survey() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        sut.init(surveyTemplate);

        sut.getQuestionList().get(0).answer(0);

        //when
        Boolean isFinish = sut.isFinish();

        //then
        assertThat(isFinish).isFalse();
    }

    @Test
    void sut_correctly_calculate_score() {
        //given
        Survey sut = new Survey();
        SurveyTemplateDto surveyTemplate = surveyTemplateRepository.findAll().get(0);
        sut.init(surveyTemplate);

        sut.answer(0, 0);
        sut.answer(1, 0);

        //when
        Map<String, Integer> scoreMap = sut.calculateScore();

        //then
        assertThat(scoreMap.get("category1")).isEqualTo(10);
        assertThat(scoreMap.get("category2")).isEqualTo(20);
    }
}