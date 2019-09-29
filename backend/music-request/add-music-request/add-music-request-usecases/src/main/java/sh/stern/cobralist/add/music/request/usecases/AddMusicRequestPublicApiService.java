package sh.stern.cobralist.add.music.request.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTO;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTONotFulfilledException;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestService;
import sh.stern.cobralist.add.music.request.api.MusicRequestAlreadyExistException;
import sh.stern.cobralist.add.music.request.dataaccess.port.AddMusicRequestDataService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collections;
import java.util.Objects;

@Service
public class AddMusicRequestPublicApiService implements AddMusicRequestService {

    private final PartySecurityService partySecurityService;
    private final MusicRequestPositionService musicRequestPositionService;
    private final PlaylistService playlistService;
    private final AddMusicRequestDataService addMusicRequestDataService;

    @Autowired
    public AddMusicRequestPublicApiService(PartySecurityService partySecurityService,
                                           MusicRequestPositionService musicRequestPositionService,
                                           PlaylistService playlistService,
                                           AddMusicRequestDataService addMusicRequestDataService) {
        this.partySecurityService = partySecurityService;
        this.musicRequestPositionService = musicRequestPositionService;
        this.playlistService = playlistService;
        this.addMusicRequestDataService = addMusicRequestDataService;
    }

    @Override
    public void addMusicRequest(UserPrincipal userPrincipal, AddMusicRequestDTO addMusicRequestDTO) {
        checkAddMusicRequestDTO(addMusicRequestDTO);
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, addMusicRequestDTO.getPartyCode());

        final Long playlistId = musicRequestPositionService.getPlaylistId(addMusicRequestDTO.getPartyCode());
        boolean musicRequestExist = addMusicRequestDataService.doesMusicRequestExist(playlistId, addMusicRequestDTO.getTrackDTO().getId());

        if (musicRequestExist) {
            throw new MusicRequestAlreadyExistException(addMusicRequestDTO.getTrackDTO().getId());
        }

        final int position = musicRequestPositionService.calculateMusicRequestPosition(playlistId, 0);
        final String playlistStreamingId = addMusicRequestDataService.getPlaylistStreamingId(playlistId);
        playlistService.addTracksWithPositionToPlaylist(userPrincipal.getUsername(), playlistStreamingId, Collections.singletonList(addMusicRequestDTO.getTrackDTO()), position);
        musicRequestPositionService.persistNewMusicRequest(playlistId, addMusicRequestDTO.getTrackDTO(), position);
    }

    private void checkAddMusicRequestDTO(AddMusicRequestDTO addMusicRequestDTO) {
        if (!StringUtils.hasText(addMusicRequestDTO.getPartyCode())) {
            throw new AddMusicRequestDTONotFulfilledException("partyCode");
        } else if (Objects.isNull(addMusicRequestDTO.getTrackDTO())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getId())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.id");
        } else if (Objects.isNull(addMusicRequestDTO.getTrackDTO().getArtists())
                || addMusicRequestDTO.getTrackDTO().getArtists().isEmpty()
                || !StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getArtists().get(0))) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.artists");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getUri())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.uri");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getName())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.name");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getAlbumName())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.albumName");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrackDTO().getImageUrl())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.imageUrl");
        } else if (Objects.isNull(addMusicRequestDTO.getTrackDTO().getImageWidth())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.imageWidth");
        } else if (Objects.isNull(addMusicRequestDTO.getTrackDTO().getImageHeight())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.imageHeight");
        } else if (Objects.isNull(addMusicRequestDTO.getTrackDTO().getDuration())) {
            throw new AddMusicRequestDTONotFulfilledException("trackDTO.duration");
        }
    }
}
