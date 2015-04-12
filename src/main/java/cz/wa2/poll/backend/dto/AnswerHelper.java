package cz.wa2.poll.backend.dto;

import java.io.Serializable;

/**
 * Created by Nell on 12.4.2015.
 */
public class AnswerHelper implements Serializable {

    private Integer id;
    private String answer;

    public AnswerHelper() {
    }

    public AnswerHelper(Integer id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
