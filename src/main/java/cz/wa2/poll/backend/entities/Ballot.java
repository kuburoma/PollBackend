package cz.wa2.poll.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "Ballot")
public class Ballot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Poll poll;

    @ManyToOne
    @JoinColumn(name="voter_id")
    private Voter voter;

    @ManyToOne
    private Answer answer;

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Answer: "+answer+"\n");
        return sb.toString();
    }
}
