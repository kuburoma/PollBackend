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

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    private VoterGroup voterGroup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poll")
    private List<Ballot> ballots;

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

    @Transient
    public void addBallot(Ballot ballot){
        if(this.ballots == null){
            this.ballots = new ArrayList<Ballot>();
        }
        this.ballots.add(ballot);
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Question: "+question+"\n");
        return sb.toString();
    }
}
