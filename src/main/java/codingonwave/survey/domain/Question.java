package codingonwave.survey.domain;

import codingonwave.survey.dto.QuestionTemplateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Question {

    private String value;

    private List<Answer> answerList = new ArrayList<>();

    private Category category;

    public static Question from(QuestionTemplateDto questionTemplate) {
        List<Answer> answerList = questionTemplate.getAnswerTemplateList().stream()
                .map(Answer::from).collect(Collectors.toList());

        Category category = Category.from(questionTemplate.getCategory());

        return new Question(questionTemplate.getValue(), answerList, category);
    }

    public Question(String value, List<Answer> answerList, Category category) {
        this.value = value;
        this.answerList = answerList;
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }


    public void answer(Integer answerIndex) {
        answerList.forEach(Answer::unselect);

        Answer answer = answerList.get(answerIndex);
        answer.select();
    }

    public Answer getSelectedAnswer() {
        return answerList.stream()
                .filter(Answer::isSelected).findFirst().orElse(null);
    }

    public Answer getAnswer(Integer answerIndex) {
        return answerList.get(answerIndex);
    }

    public Category getCategory() {
        return category;
    }
}
