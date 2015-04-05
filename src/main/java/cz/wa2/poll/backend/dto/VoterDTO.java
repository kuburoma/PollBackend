package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private String password;
    private List<VoterGroupDTO> supervisedGroups;
    private List<VoterGroupDTO> voterGroups;

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

    public List<VoterGroupDTO> getSupervisedGroups() {
        if(this.supervisedGroups == null){
            this.supervisedGroups = new ArrayList<VoterGroupDTO>();
        }
        return supervisedGroups;
    }

    public void addSupervisedGroup(VoterGroupDTO supervisedGroup) {
        if(this.supervisedGroups == null){
            this.supervisedGroups = new ArrayList<VoterGroupDTO>();
        }
        this.supervisedGroups.add(supervisedGroup);
    }

    public void setSupervisedGroups(List<VoterGroupDTO> supervisedGroups) {
        this.supervisedGroups = supervisedGroups;
    }

    public List<VoterGroupDTO> getVoterGroups() {
        if(this.voterGroups == null){
            this.voterGroups = new ArrayList<VoterGroupDTO>();
        }
        return voterGroups;
    }

    public void addVoterGroup(VoterGroupDTO voterGroup){
        if(this.voterGroups == null){
            this.voterGroups = new ArrayList<VoterGroupDTO>();
        }
        this.voterGroups.add(voterGroup);

    }

    public void setVoterGroups(List<VoterGroupDTO> voterGroups) {
        this.voterGroups = voterGroups;
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
