package cz.wa2.poll.backend.dto;

public class PollDTO {

    private Long id;
    private String name;
    private String question;

    public PollDTO() {
    }

    public PollDTO(Long id, String name, String question) {
        this.id = id;
        this.name = name;
        this.question = question;
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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Question: "+question+"\n");
        return sb.toString();
    }
}
