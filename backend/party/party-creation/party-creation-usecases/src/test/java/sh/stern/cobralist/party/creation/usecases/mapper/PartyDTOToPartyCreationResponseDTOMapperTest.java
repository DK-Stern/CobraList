package sh.stern.cobralist.party.creation.usecases.mapper;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.creation.api.PartyCreationResponseDTO;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class PartyDTOToPartyCreationResponseDTOMapperTest {

    private PartyDTOToPartyCreationResponseDTOMapper testSubject;

    @Before
    public void setUp() {
        testSubject = new PartyDTOToPartyCreationResponseDTOMapper();
    }

    @Test
    public void mapPartyCode() {
        // given
        final PartyDTO partyDTO = new PartyDTO();
        final String expectedPartyCode = "1234adsf";
        partyDTO.setPartyCode(expectedPartyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.getPartyCode()).isEqualTo(expectedPartyCode);
    }

    @Test
    public void mapPartyName() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.getPartyName()).isEqualTo(expectedPartyName);
    }

    @Test
    public void mapPartyPassword() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.getPassword()).isEqualTo(expectedPartyPassword);
    }

    @Test
    public void mapDownVoting() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.isDownVoting()).isEqualTo(expectedDownVoting);
    }

    @Test
    public void mapDescription() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void mapPlaylistDTO() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when
        final PartyCreationResponseDTO resultedDTO = testSubject.map(partyDTO, playlistDTO);

        // then
        assertThat(resultedDTO.getPlaylist()).isEqualTo(playlistDTO);
    }

    @Test
    public void throwsExceptionIfPartyDtoIsNull() {
        // given
        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(null, playlistDTO))
                .withMessage("'partyDTO' is null");
    }

    @Test
    public void throwsExceptionIfPartyCodeIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();
        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, playlistDTO))
                .withMessage("'partyCode' is null");
    }

    @Test
    public void throwsExceptionIfPartyNameIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, playlistDTO))
                .withMessage("'partyName' is null");
    }

    @Test
    public void throwsExceptionIfPartyPasswordIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, playlistDTO))
                .withMessage("'password' is null");
    }

    @Test
    public void throwsExceptionIfDownVotingIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, playlistDTO))
                .withMessage("'downVoting' is null");
    }

    @Test
    public void throwsExceptionIfDescriptionIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final PlaylistDTO playlistDTO = new PlaylistDTO();

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, playlistDTO))
                .withMessage("'description' is null");
    }

    @Test
    public void throwsExceptionIfPlaylistDTOIsNull() {
        // given
        final PartyDTO partyDTO = new PartyDTO();

        final String partyCode = "partyCode";
        partyDTO.setPartyCode(partyCode);

        final String expectedPartyName = "partyName";
        partyDTO.setPartyName(expectedPartyName);

        final String expectedPartyPassword = "123456789";
        partyDTO.setPassword(expectedPartyPassword);

        final boolean expectedDownVoting = true;
        partyDTO.setDownVoting(expectedDownVoting);

        final String expectedDescription = "description";
        partyDTO.setDescription(expectedDescription);

        // when u. then
        assertThatNullPointerException()
                .isThrownBy(() -> testSubject.map(partyDTO, null))
                .withMessage("'playlistDTO' is null");
    }
}