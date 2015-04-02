package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;

public class VoterDTO {

    public VoterDTO(){}

    public VoterDTO(Voter voter){
        this.id = voter.getId();
        this.firstName = voter.getFirstName();
        this.lastName = voter.getLastName();
        this.email = voter.getEmail();
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

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

}
