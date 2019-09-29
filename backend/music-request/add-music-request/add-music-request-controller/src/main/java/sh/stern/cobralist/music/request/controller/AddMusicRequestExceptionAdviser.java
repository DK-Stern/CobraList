package sh.stern.cobralist.music.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTONotFulfilledException;
import sh.stern.cobralist.add.music.request.api.MusicRequestAlreadyExistException;
import sh.stern.cobralist.exception.domain.ErrorInfo;

@ControllerAdvice
public class AddMusicRequestExceptionAdviser {
    @ExceptionHandler(MusicRequestAlreadyExistException.class)
    public ResponseEntity<ErrorInfo> handleMusicRequestAlreadyExistException(MusicRequestAlreadyExistException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AddMusicRequestDTONotFulfilledException.class)
    public ResponseEntity<ErrorInfo> handleAddMusicRequestDTONotFulfilledException(AddMusicRequestDTONotFulfilledException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.BAD_REQUEST);
    }
}
