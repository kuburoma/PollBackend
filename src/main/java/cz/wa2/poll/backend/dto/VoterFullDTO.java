package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;

public class VoterFullDTO {

    public VoterFullDTO(){}

    public VoterFullDTO(Voter voter){
        this.id = voter.getId();
        this.firstName = voter.getFirstName();
        this.lastName = voter.getLastName();
        this.email = voter.getEmail();
        this.password = voter.getPassword();
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

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

    public Voter toEntity(){
        Voter voter = new Voter();
        if(id != null){
            voter.setId(id);
        }
        voter.setFirstName(firstName);
        voter.setLastName(lastName);
        voter.setEmail(email);
        voter.setPassword(password);
        return voter;
    }
}
