package codingonwave.survey.module;

import codingonwave.survey.domain.QuestionType;
import codingonwave.survey.dto.AnswerTemplateDto;
import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.dto.QuestionTemplateDto;
import codingonwave.survey.dto.SurveyTemplateDto;
import codingonwave.survey.repository.CategoryTemplateRepository;
import codingonwave.survey.repository.SurveyTemplateRepository;
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
class SurveyModuleTest {

    @Autowired
    SurveyTemplateRepository surveyTemplateRepository;

    @Autowired
    CategoryTemplateRepository categoryTemplateRepository;

    @BeforeEach
    void beforeEach() {
        CategoryTemplateDto category1 = new CategoryTemplateDto("category1");
        CategoryTemplateDto category2 = new CategoryTemplateDto("category2");
        categoryTemplateRepository.save(category1);
        categoryTemplateRepository.save(category2);
    }

    SurveyTemplateDto makeNewSurveyTemplateDto() {
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

        return new SurveyTemplateDto(Arrays.asList(question1, question2));
    }

    @Test
    void sut_correctly_start_survey() {
        //given
        SurveyModule sut = new SurveyModule();
        String sessionKey = "user";

        //when
        sut.start(sessionKey);

        //then
        assertThat(sut.surveyOf(sessionKey)).isNotNull();

    }
}