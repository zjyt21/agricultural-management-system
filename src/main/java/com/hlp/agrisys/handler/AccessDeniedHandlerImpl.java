package com.hlp.agrisys.handler;

import com.alibaba.fastjson.JSON;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.FORBIDDEN.value(), "Unauthorized operation");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);

    }
}