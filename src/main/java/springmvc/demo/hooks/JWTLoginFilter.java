package springmvc.demo.hooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springmvc.demo.models.User;
import springmvc.demo.services.authentication.TokenAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        User user = new User();
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(email == "" || password == "") {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } else {
            user.setEmail(email);
            user.setPassword(password);
        }

        System.out.println("login user: " + user.getEmail());

        return getAuthenticationManager().authenticate( new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        try {
            TokenAuthenticationService.addAuthentication(response, authentication);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
