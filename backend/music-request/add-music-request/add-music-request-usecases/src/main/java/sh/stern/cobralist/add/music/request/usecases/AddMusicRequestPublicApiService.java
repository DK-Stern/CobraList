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
import java.util.Optional;

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
        boolean musicRequestExist = addMusicRequestDataService.doesMusicRequestExist(playlistId, addMusicRequestDTO.getTrack().getStreamingId());

        if (musicRequestExist) {
            throw new MusicRequestAlreadyExistException(addMusicRequestDTO.getTrack().getStreamingId());
        }

        Integer position = 0;
        final boolean playlistEmpty = musicRequestPositionService.isPlaylistEmpty(playlistId);
        if (!playlistEmpty) {
            final Optional<Integer> negativeMuiscRequestOptional = musicRequestPositionService.getPositionOfMusicRequestWithNegativeRatingAndLowestPosition(playlistId);
            position = negativeMuiscRequestOptional.orElseGet(() -> musicRequestPositionService.getPositionOfLastMusicRequest(playlistId) + 1);
        }

        final String playlistStreamingId = addMusicRequestDataService.getPlaylistStreamingId(playlistId);
        playlistService.addTracksWithPositionToPlaylist(userPrincipal.getUsername(), playlistStreamingId, Collections.singletonList(addMusicRequestDTO.getTrack()), position);
        musicRequestPositionService.persistNewMusicRequest(playlistId, addMusicRequestDTO.getTrack(), position);
    }

    private void checkAddMusicRequestDTO(AddMusicRequestDTO addMusicRequestDTO) {
        if (!StringUtils.hasText(addMusicRequestDTO.getPartyCode())) {
            throw new AddMusicRequestDTONotFulfilledException("partyCode");
        } else if (Objects.isNull(addMusicRequestDTO.getTrack())) {
            throw new AddMusicRequestDTONotFulfilledException("track");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrack().getStreamingId())) {
            throw new AddMusicRequestDTONotFulfilledException("track.id");
        } else if (Objects.isNull(addMusicRequestDTO.getTrack().getArtists())
                || addMusicRequestDTO.getTrack().getArtists().isEmpty()
                || !StringUtils.hasText(addMusicRequestDTO.getTrack().getArtists().get(0))) {
            throw new AddMusicRequestDTONotFulfilledException("track.artists");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrack().getUri())) {
            throw new AddMusicRequestDTONotFulfilledException("track.uri");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrack().getName())) {
            throw new AddMusicRequestDTONotFulfilledException("track.name");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrack().getAlbumName())) {
            throw new AddMusicRequestDTONotFulfilledException("track.albumName");
        } else if (!StringUtils.hasText(addMusicRequestDTO.getTrack().getImageUrl())) {
            throw new AddMusicRequestDTONotFulfilledException("track.imageUrl");
        } else if (Objects.isNull(addMusicRequestDTO.getTrack().getImageWidth())) {
            throw new AddMusicRequestDTONotFulfilledException("track.imageWidth");
        } else if (Objects.isNull(addMusicRequestDTO.getTrack().getImageHeight())) {
            throw new AddMusicRequestDTONotFulfilledException("track.imageHeight");
        } else if (Objects.isNull(addMusicRequestDTO.getTrack().getDuration())) {
            throw new AddMusicRequestDTONotFulfilledException("track.duration");
        }
    }
}
