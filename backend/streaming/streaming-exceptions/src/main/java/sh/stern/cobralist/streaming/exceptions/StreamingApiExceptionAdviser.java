package sh.stern.cobralist.streaming.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;

@ControllerAdvice
public class StreamingApiExceptionAdviser {

    @ExceptionHandler(StreamingServiceUnavailableException.class)
    public ResponseEntity<ErrorInfo> handleStreamingServiceUnavailableException(StreamingServiceUnavailableException ex, WebRequest request) {
        final String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
        return new ResponseEntity<>(new ErrorInfo(url, ex), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
