package sh.stern.cobralist.party.join.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sh.stern.cobralist.exception.domain.ErrorInfo;
import sh.stern.cobralist.party.join.api.GuestAlreadyExistException;
import sh.stern.cobralist.party.join.api.PartyPasswordWrongException;

@ControllerAdvice
public class JoinPartyControllerExceptionAdviser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GuestAlreadyExistException.class)
    public ResponseEntity<ErrorInfo> handleGuestAlreadyExistException(Exception ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PartyPasswordWrongException.class)
    public ResponseEntity<ErrorInfo> handlePartyPasswordWrongException(Exception ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.FORBIDDEN);
    }
}
