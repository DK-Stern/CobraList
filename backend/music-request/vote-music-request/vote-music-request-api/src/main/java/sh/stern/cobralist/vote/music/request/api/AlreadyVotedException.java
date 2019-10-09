package sh.stern.cobralist.vote.music.request.api;

public class AlreadyVotedException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Es wurde bereits fuer den Musikwunsch mit der id '%d' abgestimmt.";

    public AlreadyVotedException(Long musicRequestId) {
        super(String.format(ERROR_MESSAGE, musicRequestId));
    }
}
