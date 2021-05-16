package codingonwave.survey.module;

import codingonwave.survey.domain.Survey;
import codingonwave.survey.dto.SurveyTemplateDto;
import codingonwave.survey.repository.SurveyTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SurveyModule {

    private Map<String, Survey> surveyMap = new ConcurrentHashMap<>();

    private final SurveyTemplateRepository repository;

    public void start(String username) {
        SurveyTemplateDto surveyTemplate = repository.findActiveSurveyTemplate();
        Survey survey = new Survey();
        survey.init(surveyTemplate);

        surveyMap.put(username, survey);
    }

    public Survey surveyOf(String username) {
        return surveyMap.get(username);
    }

    public Boolean hasSurveyOf(String username) {
        return surveyMap.containsKey(username);
    }

    public void remove(String username) {
        surveyMap.remove(username);
    }

    public void clear() {
        surveyMap.clear();
    }
}
