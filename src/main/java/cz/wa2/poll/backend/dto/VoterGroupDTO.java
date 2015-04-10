package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;

public class VoterGroupDTO {

    private Long id;
    private String name;
    private String description;

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

    public VoterGroup toEntity(){
        VoterGroup voterGroup = new VoterGroup();
        voterGroup.setName(this.getName());
        voterGroup.setDescription(this.getDescription());
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
