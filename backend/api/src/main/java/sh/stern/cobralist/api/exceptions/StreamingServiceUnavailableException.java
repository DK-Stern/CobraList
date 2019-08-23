package sh.stern.cobralist.api.exceptions;

import java.text.MessageFormat;

public class StreamingServiceUnavailableException extends RuntimeException {
    private static final String ERROR_MESSAGE = "''{0}'' ist derzeit nicht erreichbar.";

    public StreamingServiceUnavailableException(String streamingService) {
        super(MessageFormat.format(ERROR_MESSAGE, streamingService));
    }
}
