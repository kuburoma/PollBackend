package cz.wa2.poll.backend.entities;

import javax.persistence.*;
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "voters", fetch = FetchType.LAZY)
    private List<VoterGroup> voterGroups;


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

    public void setVoterGroups(List<VoterGroup> voterGroups) {
        this.voterGroups = voterGroups;
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
