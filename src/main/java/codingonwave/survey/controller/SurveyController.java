package codingonwave.survey.controller;

import codingonwave.survey.domain.Question;
import codingonwave.survey.domain.Survey;
import codingonwave.survey.dto.ScoreResponse;
import codingonwave.survey.module.SurveyModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyModule surveyModule;

    @PostMapping
    public ResponseEntity<String> start(@RequestParam("username") String username, HttpServletResponse response) {
        Boolean isDuplicateUsername = surveyModule.hasSurveyOf(username);

        if (isDuplicateUsername) {
            return new ResponseEntity<>("duplicate username", HttpStatus.BAD_REQUEST);
        }

        surveyModule.start(username);

        response.addCookie(new Cookie("username", username));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/question/{questionIndex}")
    public ResponseEntity<Question> getQuestionByIndex(
            @PathVariable("questionIndex") Integer questionIndex, @CookieValue("username") String username) {

        Survey survey = surveyModule.surveyOf(username);
        Question question = survey.getQuestionByIndex(questionIndex);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping("/answer")
    public void answerToQuestion(@CookieValue("username") String username,
                                 @RequestParam("questionIndex") Integer questionIndex,
                                 @RequestParam("answerIndex") Integer answerIndex) {

        Survey survey = surveyModule.surveyOf(username);
        survey.answer(questionIndex, answerIndex);
    }

    @GetMapping("/score")
    public ResponseEntity<ScoreResponse> calculateScore(@CookieValue("username") String username) {

        Survey survey = surveyModule.surveyOf(username);
        Boolean finish = survey.isFinish();

        if (!finish) {
            ScoreResponse failResponse = new ScoreResponse("this survey is not finished yet");
            return new ResponseEntity<>(failResponse, HttpStatus.BAD_REQUEST);
        }

        Map<String, Integer> scoreMap = survey.calculateScore();
        ScoreResponse successResponse = new ScoreResponse("success", scoreMap);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping("/clear")
    public void clear() {
        surveyModule.clear();
    }
}
