package cz.wa2.poll.backend.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "VoterGroup")
public class VoterGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "votergroup_id")
    private Long id;
    private String name;
    private String description;
    private String founder;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name="voter_votergroup",
            joinColumns=@JoinColumn(name="voutergroup_id"),
            inverseJoinColumns=@JoinColumn(name="voter_id")
    )
    private List<Voter> voters;

    @OneToMany(mappedBy = "voterGroup")
    private List<Poll> polls;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Description: "+description+"\n");
        sb.append("Founder: "+founder+"\n");
        return sb.toString();
    }
}
