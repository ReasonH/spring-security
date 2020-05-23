package com.reason.springsec.security.filters;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

// api에 대한 보호 역할
public class FilterSkipMatcher implements RequestMatcher {
    private OrRequestMatcher orRequestMatcher;
    private RequestMatcher processingMatcher;

    // 건너뛸 주소들과, 필터가 작동할 주소를 등록
    public FilterSkipMatcher(List<String> pathToSkip, String processingPath){
        this.orRequestMatcher = new OrRequestMatcher(pathToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList()));
        this.processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !orRequestMatcher.matches(request) && processingMatcher.matches(request);
    }
}
