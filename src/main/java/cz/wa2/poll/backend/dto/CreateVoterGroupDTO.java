package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.VoterGroup;

import java.util.List;

/**
 * Created by Nell on 2.4.2015.
 */
public class CreateVoterGroupDTO {

    private String name;
    private String description;
    private VoterDTO supervisor;

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
}
