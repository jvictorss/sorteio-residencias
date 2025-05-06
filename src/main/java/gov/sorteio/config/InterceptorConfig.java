package gov.sorteio.config;

import gov.sorteio.dto.RodapeDTO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class InterceptorConfig implements HandlerInterceptor, WebMvcConfigurer {
    private String name;
    private final RodapeDTO rodapeDTO;

    @Autowired
    public InterceptorConfig(@Value("${spring.application.name}") String name, RodapeDTO rodapeDTO) {
        this.name = name;
        this.rodapeDTO = rodapeDTO;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int status = response.getStatus();
        String path2 = request.getServletPath();
        if(response.getStatus() == HttpStatus.NOT_FOUND.value() ||
                response.getStatus() == HttpStatus.METHOD_NOT_ALLOWED.value()){
            response.setStatus(200);
            request.getRequestDispatcher("/back/painel").forward(request,response);
        }
        return true;
    }
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        int status = response.getStatus();
        if(Objects.nonNull(modelAndView)) {
            session.setAttribute("nameAPI", name);
            session.setAttribute("rodape", rodapeDTO);
            Object path = modelAndView.getModel().get("path");
            if(modelAndView.getViewName().contains("error")){
                session.setAttribute("error",modelAndView.getModel());
                if(path.toString().contains("/site")){
                    Object statusCode = modelAndView.getModel().get("status");
                    if(statusCode.equals(405)){
                        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                        response.setHeader("Location", "?exception");
                    }else{
                        response.sendRedirect("?exception");
                    }
                }else{
                    response.sendRedirect(name + "/v1/painel?exception");
                }
            }
            modelAndView.addObject("iconTheme", readServletCookie(request, "iconTheme").orElse("bx-sun"));
            modelAndView.addObject("themeRSD", readServletCookie(request, "themeRSD").orElse("core2.css"));
            modelAndView.addObject("themeBordRSD", readServletCookie(request, "themeBordRSD").orElse("theme-default.css"));
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorConfig(name, rodapeDTO));
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        if(request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .filter(Strings::isNotBlank)
                    .findAny();
        }
        return Optional.empty();
    }
}
