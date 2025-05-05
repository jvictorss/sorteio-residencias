package gov.sorteio.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class InterceptorConfig implements HandlerInterceptor, WebMvcConfigurer {
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
//        request.getSession().setAttribute("avisos",5);
        if(Objects.nonNull(modelAndView)) {
            if(Objects.nonNull(modelAndView.getModel().get("theme_cleartheme_clear"))){
                String tipo = modelAndView.getModel().get("theme_cleartheme_clear").toString();
                modelAndView.addObject("theme", "core2.css");
                modelAndView.addObject("themeBord", "theme-default.css");
            }else{
                modelAndView.addObject("theme", readServletCookie(request, "theme").orElse("core2.css"));
                modelAndView.addObject("themeBord", readServletCookie(request, "themeBord").orElse("theme-default.css"));
            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorConfig());
    }

    public Optional<String> readServletCookie(HttpServletRequest request, String name) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(Strings::isNotBlank)
                .findAny();
    }
}
