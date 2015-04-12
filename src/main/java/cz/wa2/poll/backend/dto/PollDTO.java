package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Poll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollDTO {

    private Long id;
    private String name;
    private String question;
    private List<AnswerHelper> answers = new ArrayList<AnswerHelper>();

    public PollDTO() {
    }

    public PollDTO(Poll poll) {
        this.id = poll.getId();
        this.name = poll.getName();
        this.question = poll.getQuestion();
        answers = new ArrayList<AnswerHelper>();
        for (Map.Entry<Integer, String> entry : poll.getAnswers().entrySet())
        {
            this.answers.add(new AnswerHelper(entry.getKey(),entry.getValue()));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerHelper> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerHelper> answers) {
        this.answers = answers;
    }

    public Poll toEntity(){
        Poll poll = new Poll();
        if(id != null){
            poll.setId(id);
        }
        poll.setName(this.name);
        poll.setQuestion(this.question);
        Map<Integer, String> answersEntity = new HashMap<Integer, String>();

        for (AnswerHelper answer : answers) {
            answersEntity.put(answer.getId(),answer.getAnswer());
        }

        poll.setAnswers(answersEntity);

        return poll;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Question: "+question+"\n");
        return sb.toString();
    }

}
