package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;

public class PollDTO {

    private Long id;
    private String name;
    private String question;

    public PollDTO() {
    }

    public PollDTO(Poll poll) {
        this.id = poll.getId();
        this.name = poll.getName();
        this.question = poll.getQuestion();
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

    public Poll toEntity(){
        Poll poll = new Poll();
        if(id != null){
            poll.setId(id);
        }
        poll.setName(this.name);
        poll.setQuestion(this.question);
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
