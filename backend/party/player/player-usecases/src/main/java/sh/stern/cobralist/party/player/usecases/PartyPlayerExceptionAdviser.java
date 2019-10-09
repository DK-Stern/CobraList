package sh.stern.cobralist.party.player.usecases;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;
import sh.stern.cobralist.party.security.api.NoPartyCreatorException;

@ControllerAdvice
public class PartyPlayerExceptionAdviser {
    @ExceptionHandler()
    public ResponseEntity<ErrorInfo> handleNoPartyCreatorException(NoPartyCreatorException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.NOT_FOUND);
    }
}
