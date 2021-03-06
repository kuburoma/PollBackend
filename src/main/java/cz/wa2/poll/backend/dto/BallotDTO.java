package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Ballot;

public class BallotDTO {

    private Long id;
    private Integer answer;

    public BallotDTO() {
    }

    public BallotDTO(Ballot ballot) {
        this.id = ballot.getId();
        this.answer = ballot.getAnswer();
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ballot toEntity(){
        Ballot ballot = new Ballot();
        ballot.setId(id);
        ballot.setAnswer(answer);
        return ballot;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Answer: "+answer+"\n");
        return sb.toString();
    }
}
