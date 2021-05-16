package codingonwave.survey.repository;

import codingonwave.survey.domain.*;
import codingonwave.survey.dto.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static codingonwave.survey.domain.QSurveyTemplate.*;

@Repository
public class SurveyTemplateRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SurveyTemplateRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public void save(SurveyTemplateDto dto) {
        List<QuestionTemplate> questionList = makeQuestionTemplate(dto.getQuestionTemplateList());
        SurveyTemplate surveyTemplate = new SurveyTemplate();
        questionList.forEach(surveyTemplate::addQuestionTemplate);

        em.persist(surveyTemplate);
    }

    @Transactional
    public void remove(SurveyTemplateDto dto) {
        SurveyTemplate surveyTemplate = em.find(SurveyTemplate.class, dto.getId());
        em.remove(surveyTemplate);
    }

    @Transactional
    public void removeAll() {
        List<SurveyTemplate> surveyTemplates = queryFactory.selectFrom(surveyTemplate).fetch();
        surveyTemplates.forEach(em::remove);
    }

    public List<SurveyTemplateDto> findAll() {
        List<SurveyTemplate> surveyTemplates = queryFactory.selectFrom(surveyTemplate).fetch();
        return surveyTemplates.stream()
                .map(template -> new SurveyTemplateDto(template.getId(), template.isActive(),
                        convertQuestionTemplate(template.getQuestionTemplateList())))
                .collect(Collectors.toList());
    }

    public SurveyTemplateDto findById(Long id) {
        SurveyTemplate surveyTemplate = em.find(SurveyTemplate.class, id);
        return new SurveyTemplateDto(surveyTemplate.getId(), surveyTemplate.isActive(),
                convertQuestionTemplate(surveyTemplate.getQuestionTemplateList()));
    }

    public QuestionTemplateDto findQuestionTemplateById(Long questionTemplateId) {
        QuestionTemplate questionTemplate = em.find(QuestionTemplate.class, questionTemplateId);
        return convertQuestionTemplate(questionTemplate);
    }

    public AnswerTemplateDto findAnswerTemplateById(Long answerTemplateId) {
        AnswerTemplate answerTemplate = em.find(AnswerTemplate.class, answerTemplateId);
        return convertAnswerTemplate(answerTemplate);
    }

    @Transactional
    public void setActive(Long id) {
        queryFactory
                .update(surveyTemplate)
                .set(surveyTemplate.active, false)
                .execute();

        SurveyTemplate surveyTemplate = em.find(SurveyTemplate.class, id);
        surveyTemplate.setActive(true);
    }

    public SurveyTemplateDto findActiveSurveyTemplate() {
        SurveyTemplate surveyTemplate = queryFactory
                .selectFrom(QSurveyTemplate.surveyTemplate)
                .where(QSurveyTemplate.surveyTemplate.active.isTrue())
                .fetchOne();

        if (surveyTemplate != null) {
            return new SurveyTemplateDto(surveyTemplate.getId(), surveyTemplate.isActive(),
                    convertQuestionTemplate(surveyTemplate.getQuestionTemplateList()));
        }

        return null;
    }

    @Transactional
    public void addQuestion(SurveyTemplateDto surveyTemplate, QuestionTemplateDto newQuestionTemplate) {
        SurveyTemplate findSurveyTemplate = em.find(SurveyTemplate.class, surveyTemplate.getId());
        QuestionTemplate questionTemplate = makeQuestionTemplate(newQuestionTemplate);

        findSurveyTemplate.addQuestionTemplate(questionTemplate);
    }

    @Transactional
    public void updateQuestion(QuestionTemplateDto questionTemplate, QuestionType questionType, String newValue) {
        QuestionTemplate savedQuestionTemplate = em.find(QuestionTemplate.class, questionTemplate.getId());
        savedQuestionTemplate.setQuestionType(questionType);
        savedQuestionTemplate.setValue(newValue);

    }

    @Transactional
    public void removeQuestion(QuestionTemplateDto questionTemplate) {
        QuestionTemplate findQuestionTemplate = em.find(QuestionTemplate.class, questionTemplate.getId());
        SurveyTemplate findSurveyTemplate = findQuestionTemplate.getSurveyTemplate();

        findSurveyTemplate.removeQuestionTemplate(findQuestionTemplate);
    }

    @Transactional
    public void addAnswer(QuestionTemplateDto questionTemplate, AnswerTemplateDto answerTemplate) {
        QuestionTemplate findQuestionTemplate = em.find(QuestionTemplate.class, questionTemplate.getId());
        AnswerTemplate newAnswerTemplate = makeAnswerTemplate(answerTemplate);

        findQuestionTemplate.addAnswerTemplate(newAnswerTemplate);
    }

    @Transactional
    public void updateAnswer(AnswerTemplateDto answerTemplate, String newText, Integer newScore) {
        AnswerTemplate findAnswerTemplate = em.find(AnswerTemplate.class, answerTemplate.getId());
        findAnswerTemplate.setText(newText);
        findAnswerTemplate.setScore(newScore);
    }

    @Transactional
    public void removeAnswer(AnswerTemplateDto answerTemplate) {
        AnswerTemplate findAnswerTemplate = em.find(AnswerTemplate.class, answerTemplate.getId());
        QuestionTemplate findQuestionTemplate = findAnswerTemplate.getQuestionTemplate();

        findQuestionTemplate.removeAnswerTemplate(findAnswerTemplate);
    }

    private QuestionTemplate makeQuestionTemplate(QuestionTemplateDto dto) {
        CategoryTemplate categoryTemplate = em.find(CategoryTemplate.class, dto.getCategory().getId());
        QuestionTemplate questionTemplate = new QuestionTemplate(
                dto.getValue(),
                dto.getQuestionType(),
                categoryTemplate);

        List<AnswerTemplate> answerTemplates = makeAnswerTemplate(dto.getAnswerTemplateList());
        answerTemplates.forEach(questionTemplate::addAnswerTemplate);

        return questionTemplate;
    }

    private List<QuestionTemplate> makeQuestionTemplate(List<QuestionTemplateDto> dtoList) {
        return dtoList.stream().map(this::makeQuestionTemplate).collect(Collectors.toList());
    }

    private AnswerTemplate makeAnswerTemplate(AnswerTemplateDto dto) {
        return new AnswerTemplate(
                dto.getText(),
                dto.getScore());
    }

    private List<AnswerTemplate> makeAnswerTemplate(List<AnswerTemplateDto> dtoList) {
        return dtoList.stream().map(this::makeAnswerTemplate).collect(Collectors.toList());
    }

    private QuestionTemplateDto convertQuestionTemplate(QuestionTemplate template) {
        return new QuestionTemplateDto(
            template.getId(),
            template.getValue(),
            template.getQuestionType(),
            convertAnswerTemplate(template.getAnswerTemplateList()),
            convertCategoryTemplate(template.getCategoryTemplate()));
    }

    private List<QuestionTemplateDto> convertQuestionTemplate(List<QuestionTemplate> templateList) {
        return templateList.stream().map(this::convertQuestionTemplate).collect(Collectors.toList());
    }

    private AnswerTemplateDto convertAnswerTemplate(AnswerTemplate template) {
        return new AnswerTemplateDto(template.getId(), template.getText(), template.getScore());
    }

    private List<AnswerTemplateDto> convertAnswerTemplate(List<AnswerTemplate> templateList) {
        return templateList.stream().map(this::convertAnswerTemplate).collect(Collectors.toList());
    }

    private CategoryTemplateDto convertCategoryTemplate(CategoryTemplate template) {
        return new CategoryTemplateDto(template.getName());
    }
}
