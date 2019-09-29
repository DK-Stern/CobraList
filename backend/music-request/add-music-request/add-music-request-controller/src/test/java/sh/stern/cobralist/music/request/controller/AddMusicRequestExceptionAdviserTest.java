package sh.stern.cobralist.music.request.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTONotFulfilledException;
import sh.stern.cobralist.add.music.request.api.MusicRequestAlreadyExistException;
import sh.stern.cobralist.exception.domain.ErrorInfo;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddMusicRequestExceptionAdviserTest {

    private AddMusicRequestExceptionAdviser testSubject;

    @Before
    public void setUp() {
        testSubject = new AddMusicRequestExceptionAdviser();
    }

    @Test
    public void handleMusicRequestAlreadyExistExceptionReturnsHttpStatusConflict() {
        // given
        final MusicRequestAlreadyExistException musicRequestAlreadyExistException = new MusicRequestAlreadyExistException("trackId");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleMusicRequestAlreadyExistException(musicRequestAlreadyExistException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CONFLICT);
    }

    @Test
    public void handleMusicRequestAlreadyExistExceptionReturnsErrorInfoWithUrl() {
        // given
        final String expectedUrl = "url";
        final MusicRequestAlreadyExistException musicRequestAlreadyExistException = new MusicRequestAlreadyExistException("trackId");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer(expectedUrl));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleMusicRequestAlreadyExistException(musicRequestAlreadyExistException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void handleMusicRequestAlreadyExistExceptionReturnsErrorInfoWithMessage() {
        // given
        final String expectedTrackId = "trackId";
        final MusicRequestAlreadyExistException musicRequestAlreadyExistException = new MusicRequestAlreadyExistException(expectedTrackId);
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleMusicRequestAlreadyExistException(musicRequestAlreadyExistException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getMessage).isEqualTo("MusicRequest mit streaming id '" + expectedTrackId + "' existiert bereits.");
    }

    @Test
    public void handleAddMusicRequestDTONotFulfilledExceptionReturnsHttpStatusBadRequest() {
        // given
        final AddMusicRequestDTONotFulfilledException addMusicRequestDTONotFulfilledException = new AddMusicRequestDTONotFulfilledException("id");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleAddMusicRequestDTONotFulfilledException(addMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handleAddMusicRequestDTONotFulfilledExceptionReturnsErrorInfoWithUrl() {
        // given
        final String expectedUrl = "url";
        final AddMusicRequestDTONotFulfilledException addMusicRequestDTONotFulfilledException = new AddMusicRequestDTONotFulfilledException("id");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer(expectedUrl));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleAddMusicRequestDTONotFulfilledException(addMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void handleAddMusicRequestDTONotFulfilledExceptionReturnsErrorInfoWithMessage() {
        // given
        final String missingAttributeName = "id";
        final AddMusicRequestDTONotFulfilledException addMusicRequestDTONotFulfilledException = new AddMusicRequestDTONotFulfilledException("id");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> resultedResponseEntity = testSubject.handleAddMusicRequestDTONotFulfilledException(addMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(resultedResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getMessage).isEqualTo("Das Request-Objekt ist nicht vollstaendig. Das Attribute '" + missingAttributeName + "' ist leer.");
    }
}