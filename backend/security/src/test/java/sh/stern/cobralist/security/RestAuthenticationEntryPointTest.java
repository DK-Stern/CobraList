package sh.stern.cobralist.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RestAuthenticationEntryPointTest {

    private RestAuthenticationEntryPoint testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new RestAuthenticationEntryPoint();
    }

    @Test
    public void commence() throws ServletException, IOException {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);
        final AuthenticationException authExceptionMock = mock(AuthenticationException.class);

        // when
        testSubject.commence(requestMock, responseMock,
                authExceptionMock);

        // then
        verify(responseMock).sendError(HttpServletResponse.SC_UNAUTHORIZED, authExceptionMock.getLocalizedMessage());
    }
}