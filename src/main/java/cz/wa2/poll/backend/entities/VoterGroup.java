package cz.wa2.poll.backend.entities;

import javax.persistence.*;
import java.util.ArrayList;
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

    @ManyToOne
    private Voter supervisor;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.REFRESH,
            CascadeType.MERGE})
    @JoinTable(
            name = "voter_votergroup",
            joinColumns = @JoinColumn(name = "voutergroup_id"),
            inverseJoinColumns = @JoinColumn(name = "voter_id")
    )
    private List<Voter> voters;

    @OneToMany(mappedBy = "voterGroup",cascade = {CascadeType.ALL})
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

    public Voter getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Voter supervisor) {
        this.supervisor = supervisor;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    @Transient
    public void addVoter(Voter voter) {
        if (this.voters == null) {
            this.voters = new ArrayList<Voter>();
        }
        this.voters.add(voter);
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
        sb.append("Name: " + name + "\n");
        sb.append("Description: " + description + "\n");
        return sb.toString();
    }
}
