package codingonwave.survey.domain;

import codingonwave.survey.dto.SurveyTemplateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Survey {

    private List<Question> questionList = new ArrayList<>();

    public void init(SurveyTemplateDto surveyTemplate) {
        questionList = surveyTemplate.getQuestionTemplateList()
                .stream().map(Question::from)
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public Question getQuestionByIndex(Integer index) {
        return questionList.get(index);
    }

    public void answer(Integer questionIndex, Integer answerIndex) {
        Question question = questionList.get(questionIndex);
        question.answer(answerIndex);
    }

    public Answer getSelectedAnswerOf(Integer questionIndex) {
        Question question = questionList.get(questionIndex);
        return question.getSelectedAnswer();
    }

    public Answer getAnswer(Integer questionIndex, Integer answerIndex) {
        Question question = questionList.get(questionIndex);
        return question.getAnswer(answerIndex);
    }

    public Boolean isFinish() {
        for (Question question : questionList) {
            Boolean answered = isAnswered(question);
            if (!answered) {
                return false;
            }
        }

        return true;
    }

    private Boolean isAnswered(Question question) {
        List<Answer> answerList = question.getAnswerList();
        boolean result = false;

        for (Answer answer : answerList) {
            result = result || answer.isSelected();
        }

        return result;
    }
}
