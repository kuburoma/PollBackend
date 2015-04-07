package cz.wa2.poll.backend.entities;

import cz.wa2.poll.backend.dto.VoterDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Voter")
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voter_id")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "supervisor")
    private List<VoterGroup> supervisedGroups;

    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "voters")
    private List<VoterGroup> voterGroups;

    @OneToMany(mappedBy = "voter")
    private List<Ballot> ballots;

    public Voter() {
    }

    public Voter(VoterDTO voterDTO) {
        this.id = voterDTO.getId();
        this.firstName = voterDTO.getFirstName();
        this.lastName = voterDTO.getFirstName();
        this.email = voterDTO.getPassword();
        this.password = voterDTO.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<VoterGroup> getVoterGroups() {
        return voterGroups;
    }

    @Transient
    public void addVoterGroup(VoterGroup voterGroup){
        if(this.voterGroups == null){
            this.voterGroups = new ArrayList<VoterGroup>();
        }
        this.voterGroups.add(voterGroup);
    }

    public void setVoterGroups(List<VoterGroup> voterGroups) {
        this.voterGroups = voterGroups;
    }

    public List<VoterGroup> getSupervisedGroups() {
        return supervisedGroups;
    }

    public void setSupervisedGroups(List<VoterGroup> supervisedGroups) {
        this.supervisedGroups = supervisedGroups;
    }

    @Transient
    public void addSupervisedGroup(VoterGroup supervisedGroup){
        if(this.supervisedGroups == null){
            this.supervisedGroups = new ArrayList<VoterGroup>();
        }
        this.supervisedGroups.add(supervisedGroup);
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FirstName: "+firstName+"\n");
        sb.append("LastName: "+lastName+"\n");
        sb.append("EmailtName: "+email+"\n");
        return sb.toString();
    }
}
