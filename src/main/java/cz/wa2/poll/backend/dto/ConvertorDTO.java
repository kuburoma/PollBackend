package cz.wa2.poll.backend.dto;

import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nell on 2.4.2015.
 */
public class ConvertorDTO {

    public List<VoterGroupDTO> convertVoterGroupToDTO(List<VoterGroup> voterGroups){
        List<VoterGroupDTO> voterGroupDTOs = new ArrayList<VoterGroupDTO>();
        Iterator<VoterGroup> it = voterGroups.iterator();
        while (it.hasNext()){
            voterGroupDTOs.add(new VoterGroupDTO(it.next()));
        }
        return voterGroupDTOs;
    }

    public List<VoterDTO> convertVoterToDTO(List<Voter> voters){
        List<VoterDTO> voterDTOs = new ArrayList<VoterDTO>();
        Iterator<Voter> it = voters.iterator();
        while (it.hasNext()) {
            voterDTOs.add(new VoterDTO(it.next()));
        }
        return voterDTOs;
    }

}
