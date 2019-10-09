package sh.stern.cobralist.vote.music.request.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import sh.stern.cobralist.exception.domain.ErrorInfo;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTONotFulfilledException;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VoteMusicRequestExceptionAdviserTest {

    private VoteMusicRequestExceptionAdviser testSubject;

    @Before
    public void setUp() {
        testSubject = new VoteMusicRequestExceptionAdviser();
    }

    @Test
    public void handleVoteMusicRequestDTONotFulfilledExceptionReturnsHttpStatusBadRequest() {
        // given
        final VoteMusicRequestDTONotFulfilledException voteMusicRequestDTONotFulfilledException = new VoteMusicRequestDTONotFulfilledException("isDownVote");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> errorInfoResponseEntity = testSubject.handleVoteMusicRequestDTONotFulfilledException(voteMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(errorInfoResponseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void handleVoteMusicRequestDTONotFulfilledExceptionReturnsErrorInfoWithUrl() {
        // given
        final String expectedUrl = "url";
        final VoteMusicRequestDTONotFulfilledException voteMusicRequestDTONotFulfilledException = new VoteMusicRequestDTONotFulfilledException("isDownVote");
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer(expectedUrl));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> errorInfoResponseEntity = testSubject.handleVoteMusicRequestDTONotFulfilledException(voteMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(errorInfoResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void handleVoteMusicRequestDTONotFulfilledExceptionReturnsErrorInfoWithMessage() {
        // given
        final String attribute = "isDownVote";
        final VoteMusicRequestDTONotFulfilledException voteMusicRequestDTONotFulfilledException = new VoteMusicRequestDTONotFulfilledException(attribute);
        final HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        when(httpServletRequestMock.getRequestURL()).thenReturn(new StringBuffer("url"));
        final ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequestMock);

        // when
        final ResponseEntity<ErrorInfo> errorInfoResponseEntity = testSubject.handleVoteMusicRequestDTONotFulfilledException(voteMusicRequestDTONotFulfilledException, servletWebRequest);

        // then
        assertThat(errorInfoResponseEntity.getBody()).isNotNull().extracting(ErrorInfo::getMessage).isEqualTo("Request unvollstaendig. Attribut '" + attribute + "' ist leer.");
    }
}