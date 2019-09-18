package sh.stern.cobralist.party.persistence.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;

@ControllerAdvice
public class PartyExceptionAdviser {

    @ExceptionHandler(PartyNotFoundException.class)
    public ResponseEntity<ErrorInfo> handlePartyNotFoundException(PartyNotFoundException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleGuestAlreadyExistException(GuestNotFoundException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MusicRequestNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleMusicRequestNotFoundException(MusicRequestNotFoundException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.NOT_FOUND);
    }
}
