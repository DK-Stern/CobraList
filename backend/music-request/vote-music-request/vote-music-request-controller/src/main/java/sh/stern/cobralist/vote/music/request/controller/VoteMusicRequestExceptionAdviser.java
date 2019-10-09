package sh.stern.cobralist.vote.music.request.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;
import sh.stern.cobralist.vote.music.request.api.AlreadyVotedException;
import sh.stern.cobralist.vote.music.request.api.DownVotingNotAllowedException;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTONotFulfilledException;

@ControllerAdvice
public class VoteMusicRequestExceptionAdviser {
    @ExceptionHandler(VoteMusicRequestDTONotFulfilledException.class)
    public ResponseEntity<ErrorInfo> handleVoteMusicRequestDTONotFulfilledException(VoteMusicRequestDTONotFulfilledException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyVotedException.class)
    public ResponseEntity<ErrorInfo> handleAlreadyVotedException(AlreadyVotedException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DownVotingNotAllowedException.class)
    public ResponseEntity<ErrorInfo> handleDownVotingNotAllowedException(DownVotingNotAllowedException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.METHOD_NOT_ALLOWED);
    }
}
