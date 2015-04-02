package cz.wa2.poll.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "Answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Boolean defaultAnswer;

    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultAnswer() {
        return defaultAnswer;
    }

    public void setDefaultAnswer(Boolean defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
