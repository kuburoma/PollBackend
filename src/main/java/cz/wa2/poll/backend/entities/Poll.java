package cz.wa2.poll.backend.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Poll")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String question;

    @ManyToOne(cascade = CascadeType.ALL)
    private VoterGroup voterGroup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poll", fetch = FetchType.EAGER)
    private List<Ballot> ballots;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Answer> answers;

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

    public VoterGroup getVoterGroup() {
        return voterGroup;
    }

    public void setVoterGroup(VoterGroup voterGroup) {
        this.voterGroup = voterGroup;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void addBallot(Ballot ballot){
        if(this.ballots == null){
            this.ballots = new ArrayList<Ballot>();
        }
        this.ballots.add(ballot);
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer){
        if(this.answers == null){
            this.answers = new ArrayList<Answer>();
        }
        this.answers.add(answer);

    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Question: "+question+"\n");
        return sb.toString();
    }
}
