package sh.stern.cobralist.party.creation.usecases.mapper;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.party.creation.api.PartyCreationResponseDTO;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;

import java.util.Objects;

@Component
public class PartyDTOToPartyCreationResponseDTOMapper {

    public PartyCreationResponseDTO map(PartyDTO partyDTO, PlaylistDTO playlistDTO) {
        checkConditions(partyDTO, playlistDTO);

        final PartyCreationResponseDTO partyCreationResponseDTO = new PartyCreationResponseDTO();
        partyCreationResponseDTO.setPartyCode(partyDTO.getPartyCode());
        partyCreationResponseDTO.setPartyName(partyDTO.getPartyName());
        partyCreationResponseDTO.setPassword(partyDTO.getPassword());
        partyCreationResponseDTO.setDownVoting(partyDTO.isDownVoting());
        partyCreationResponseDTO.setDescription(partyDTO.getDescription());
        partyCreationResponseDTO.setPlaylist(playlistDTO);
        return partyCreationResponseDTO;
    }

    private void checkConditions(PartyDTO partyDTO, PlaylistDTO playlistDTO) {
        if (Objects.isNull(partyDTO)) {
            throw new NullPointerException("'partyDTO' is null");
        } else if (Objects.isNull(partyDTO.getPartyCode())) {
            throw new NullPointerException("'partyCode' is null");
        } else if (Objects.isNull(partyDTO.getPartyName())) {
            throw new NullPointerException("'partyName' is null");
        } else if (Objects.isNull(partyDTO.getPassword())) {
            throw new NullPointerException("'password' is null");
        } else if (Objects.isNull(partyDTO.isDownVoting())) {
            throw new NullPointerException("'downVoting' is null");
        } else if (Objects.isNull(partyDTO.getDescription())) {
            throw new NullPointerException("'description' is null");
        } else if (Objects.isNull(playlistDTO)) {
            throw new NullPointerException("'playlistDTO' is null");
        }
    }
}
