package codingonwave.survey.module;

import codingonwave.survey.domain.Survey;

public class SurveyModule {

    public void start(String sessionKey) {

    }

    public Survey surveyOf(String sessionKey) {
        return new Survey();
    }
}
