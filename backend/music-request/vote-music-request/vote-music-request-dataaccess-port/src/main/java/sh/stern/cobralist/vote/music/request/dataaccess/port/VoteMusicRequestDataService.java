package sh.stern.cobralist.vote.music.request.dataaccess.port;

public interface VoteMusicRequestDataService {
    Long getPlaylistId(String partyCode);

    int countMusicRequest(Long musicRequestId);

    int countVotesFromParticipantForMusicRequest(Long participantId, boolean isGuest, Long musicRequestId);

    String getPartyCreatorPrincipalUsername(String partyCode);

    String getPlaylistStreamingId(Long playlistId);

    /**
     * @param musicRequestId
     * @param downVote
     * @return new rating
     */
    Integer updateMusicRequestsVote(Long musicRequestId, boolean downVote, Long participantId, boolean isGuest);

    int getMusicRequestPosition(Long musicRequestId);

    int getMusicRequestRating(Long musicRequestId);

    boolean isDownVotingAllowed(String partyCode);
}
