package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VoterGroupDTO {

    private Long id;
    private String name;
    private String description;
    private VoterDTO supervisor;

    public VoterGroupDTO() {
    }

    public VoterGroupDTO(VoterGroup voterGroup) {
        this.id = voterGroup.getId();
        this.name = voterGroup.getName();
        this.description = voterGroup.getDescription();
    }

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

    public VoterDTO getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(VoterDTO supervisor) {
        this.supervisor = supervisor;
    }

    public VoterGroup toEntity(){
        VoterGroup voterGroup = new VoterGroup();
        voterGroup.setName(this.getName());
        voterGroup.setDescription(this.getDescription());
        if(supervisor != null){
            voterGroup.setSupervisor(new Voter(supervisor));
        }
        return voterGroup;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name: "+name+"\n");
        sb.append("Description: "+description+"\n");
        return sb.toString();
    }
}
