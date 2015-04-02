package cz.wa2.poll.backend.dto;

public class BallotDTO {

    private Long id;
    private Boolean answer;

    public BallotDTO() {
    }

    public BallotDTO(Long id, Boolean answer) {
        this.id = id;
        this.answer = answer;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Answer: "+answer+"\n");
        return sb.toString();
    }
}
