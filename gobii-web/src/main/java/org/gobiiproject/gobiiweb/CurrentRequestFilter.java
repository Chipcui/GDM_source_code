package org.gobiiproject.gobiiweb;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Phil on 6/27/2016.
 */
public class CurrentRequestFilter extends OncePerRequestFilter {


    private ThreadLocal<HttpServletRequest> currentRequest;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        currentRequest.set(httpServletRequest);
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    public ThreadLocal<HttpServletRequest> getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(ThreadLocal<HttpServletRequest> currentRequest) {
        this.currentRequest = currentRequest;
    }

}
