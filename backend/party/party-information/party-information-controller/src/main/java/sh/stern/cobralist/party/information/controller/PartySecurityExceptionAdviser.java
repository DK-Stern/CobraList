package sh.stern.cobralist.party.information.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;
import sh.stern.cobralist.party.security.api.NoPartyParticipantException;

@ControllerAdvice
public class PartySecurityExceptionAdviser {
    @ExceptionHandler()
    public ResponseEntity<ErrorInfo> handleNoPartyParticipantException(NoPartyParticipantException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.NOT_FOUND);
    }
}
