package cz.wa2.poll.backend.dto;

public class VoterGroupDTO {

    private Long id;
    private String name;
    private String description;
    private String founder;

    public VoterGroupDTO() {
    }

    public VoterGroupDTO(Long id, String name, String description, String founder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founder = founder;
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

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
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
