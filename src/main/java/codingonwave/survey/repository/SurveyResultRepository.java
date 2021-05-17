package codingonwave.survey.repository;

import codingonwave.survey.domain.*;
import codingonwave.survey.dto.SurveyResultDetailDto;
import codingonwave.survey.dto.SurveyResultDto;
import codingonwave.survey.dto.SurveyResultScoreDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static codingonwave.survey.domain.QSurveyResult.surveyResult;

@Repository
public class SurveyResultRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SurveyResultRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public void save(String username, Survey survey) {
        SurveyResult surveyResult = SurveyResult.of(username);
        List<Question> questionList = survey.getQuestionList();

        for (Question question : questionList) {
            SurveyResultDetail surveyResultDetail = extractResultDetailFromQuestion(question);
            surveyResult.addSurveyResultDetail(surveyResultDetail);
        }

        List<SurveyResultScore> surveyResultScores = extractResultScoreFromSurvey(survey);
        surveyResultScores.forEach(surveyResult::addSurveyResultScore);

        em.persist(surveyResult);
    }

    public SurveyResultDto findByUsername(String username) throws EntityNotFoundException {
        SurveyResult findSurveyResult = queryFactory.
                selectFrom(surveyResult).
                where(surveyResult.username.eq(username)).
                fetchOne();

        if (findSurveyResult == null) {
            throw new EntityNotFoundException();
        }

        SurveyResultDto surveyResultDto = new SurveyResultDto(findSurveyResult.getUsername(),
                findSurveyResult.getCreatedAt());

        List<SurveyResultDetailDto> detailList = findSurveyResult.getSurveyResultDetails().stream()
                .map(this::convertToDetailDto).collect(Collectors.toList());

        List<SurveyResultScoreDto> scoreList = findSurveyResult.getSurveyResultScores().stream()
                .map(this::convertToScoreDto).collect(Collectors.toList());

        surveyResultDto.setSurveyResultDetails(detailList);
        surveyResultDto.setSurveyResultScores(scoreList);

        return surveyResultDto;
    }

    private SurveyResultDetailDto convertToDetailDto(SurveyResultDetail surveyResultDetail) {
        return new SurveyResultDetailDto(surveyResultDetail.getQuestion(), surveyResultDetail.getAnswer(),
                surveyResultDetail.getCategoryName());
    }

    private SurveyResultScoreDto convertToScoreDto(SurveyResultScore surveyResultScore) {
        return new SurveyResultScoreDto(surveyResultScore.getCategoryName(), surveyResultScore.getScore());
    }

    private SurveyResultDetail extractResultDetailFromQuestion(Question question) {
        return new SurveyResultDetail(question.getValue(), question.getSelectedAnswer().getText(),
                question.getCategory().getName());
    }

    private List<SurveyResultScore> extractResultScoreFromSurvey(Survey survey) {
        Map<String, Integer> scoreMap = survey.calculateScore();

        List<SurveyResultScore> result = new ArrayList<>();
        for (String categoryName : scoreMap.keySet()) {
            result.add(new SurveyResultScore(categoryName, scoreMap.get(categoryName)));
        }

        return result;
    }
}
