package codingonwave.survey.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ScoreResponse {
    private String message;
    private Map<String, Integer> scoreMap = new HashMap<>();

    public ScoreResponse(String message) {
        this.message = message;
    }

    public ScoreResponse(String message, Map<String, Integer> scoreMap) {
        this.message = message;
        this.scoreMap = scoreMap;
    }
}
