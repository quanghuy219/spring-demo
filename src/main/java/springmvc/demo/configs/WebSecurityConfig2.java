package springmvc.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springmvc.demo.hooks.JWTAuthenticationFilter;
import springmvc.demo.hooks.JWTCustomLoginFilter;
import springmvc.demo.services.authentication.StaffUserDetailService;


@EnableWebSecurity
@Configuration
@Order(2)
public class WebSecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private StaffUserDetailService staffUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/staff/login").csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/staff/login").permitAll()
                .and()
                .addFilterBefore(new JWTCustomLoginFilter("/staff/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(staffUserDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
