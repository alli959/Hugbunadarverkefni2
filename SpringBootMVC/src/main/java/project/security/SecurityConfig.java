package project.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("anonymous").password("{noop}anonymous").roles("ANON");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // OLI, EKKI SNERTA!!!
        httpSecurity
                .httpBasic()
                .and()
                .authorizeRequests()
                // Make all methods with path userEnabled have to be authenticated with a user + password
                // Allow all OPTIONS requests
                .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
                // Make sure newUser method is available also when not signed in
                .antMatchers(HttpMethod.GET, "/user/*").authenticated()
                .antMatchers(HttpMethod.GET, "/user/**").authenticated()
                .antMatchers(HttpMethod.GET, "/register*").hasAnyRole("ANON", "ADMIN")
                .anyRequest().hasAnyRole("ADMIN", "ANON");
                // Rest of the methods are only available to Username: admin, Password: admin

    }

}
