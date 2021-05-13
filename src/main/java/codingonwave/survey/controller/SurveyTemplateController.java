package codingonwave.survey.controller;

import codingonwave.survey.domain.QuestionType;
import codingonwave.survey.dto.AnswerTemplateDto;
import codingonwave.survey.dto.QuestionTemplateDto;
import codingonwave.survey.dto.SurveyTemplateDto;
import codingonwave.survey.repository.SurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/survey-template")
@RequiredArgsConstructor
public class SurveyTemplateController {

    private final SurveyTemplateRepository repository;

    @GetMapping
    public List<SurveyTemplateDto> surveyTemplates() {
        return repository.findAll();
    }

    @GetMapping("/{surveyTemplateId}")
    public SurveyTemplateDto surveyTemplate(@PathVariable("surveyTemplateId") Long surveyTemplateId) {
        return repository.findById(surveyTemplateId);
    }

    @PostMapping
    public void createNewSurveyTemplate(@RequestBody SurveyTemplateDto surveyTemplate) {
        repository.save(surveyTemplate);
    }

    @DeleteMapping("/{surveyTemplateId}")
    public void removeSurveyTemplate(@PathVariable("surveyTemplateId") Long surveyTemplateId) {
        SurveyTemplateDto surveyTemplate = repository.findById(surveyTemplateId);
        repository.remove(surveyTemplate);
    }

    @PostMapping("/{surveyTemplateId}/question-template")
    public void addQuestionTemplate(@PathVariable("surveyTemplateId") Long surveyTemplateId,
                                    @RequestBody QuestionTemplateDto newQuestionTemplate) {

        SurveyTemplateDto surveyTemplate = repository.findById(surveyTemplateId);
        repository.addQuestion(surveyTemplate, newQuestionTemplate);
    }

    @PatchMapping("/question-template/{questionTemplateId}")
    public void updateQuestionTemplate(@PathVariable("questionTemplateId") Long questionTemplateId,
                                       @RequestParam("questionType") QuestionType questionType,
                                       @RequestParam("questionValue") String questionValue) {

        QuestionTemplateDto questionTemplate = repository.findQuestionTemplateById(questionTemplateId);
        repository.updateQuestion(questionTemplate, questionType, questionValue);
    }

    @DeleteMapping("/question-template/{questionTemplateId}")
    public void removeQuestionTemplate(@PathVariable("questionTemplateId") Long questionTemplateId) {

        QuestionTemplateDto questionTemplate = repository.findQuestionTemplateById(questionTemplateId);
        repository.removeQuestion(questionTemplate);
    }

    @PostMapping("/question-template/{questionTemplateId}/answer-template")
    public void addAnswerTemplate(@PathVariable("questionTemplateId") Long questionTemplateId,
                                  @RequestBody AnswerTemplateDto newAnswerTemplate) {

        QuestionTemplateDto questionTemplate = repository.findQuestionTemplateById(questionTemplateId);
        repository.addAnswer(questionTemplate, newAnswerTemplate);
    }

    @PatchMapping("/question-template/answer-template/{answerTemplateId}")
    public void updateAnswerTemplate(@PathVariable("answerTemplateId") Long answerTemplateId,
                                     @RequestParam("text") String newText,
                                     @RequestParam("score") Integer newScore) {

        AnswerTemplateDto answerTemplate = repository.findAnswerTemplateById(answerTemplateId);
        repository.updateAnswer(answerTemplate, newText, newScore);
    }

    @DeleteMapping("/question-template/answer-template/{answerTemplateId}")
    public void removeAnswerTemplate(@PathVariable("answerTemplateId") Long answerTemplateId) {
        AnswerTemplateDto answerTemplate = repository.findAnswerTemplateById(answerTemplateId);
        repository.removeAnswer(answerTemplate);
    }

    @GetMapping("/remove")
    public void removeAll() {
        repository.removeAll();
    }
}
