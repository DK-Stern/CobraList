package sh.stern.cobralist.vote.music.request.api;

public class DownVotingNotAllowedException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Downvoten in der Party mit dem Partycode '%s' ist nicht erlaubt.";

    public DownVotingNotAllowedException(String partyCode) {
        super(String.format(ERROR_MESSAGE, partyCode));
    }
}
