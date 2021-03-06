package com.kadioglumf.primefaces.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/register.xhtml")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/contact-message.xhtml")
                .access("hasAnyRole('ROLE_ADMIN')")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.xhtml")
                .permitAll()
                .defaultSuccessUrl("/index.xhtml", true)
                .failureUrl("/login.xhtml?loginFailed=true")
                .and()
                .logout()
                .logoutSuccessUrl("/index.xhtml")
                .and()
                .csrf()
                .disable();

/*
        http.authorizeRequests().antMatchers("/javax.faces.resource/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/register.xhtml")
                .permitAll()
                .anyRequest().authenticated();
        // login
        http.formLogin().loginPage("/login.xhtml").permitAll()
                .failureUrl("/login.xhtml?error=true");
        // logout
        http.logout().logoutSuccessUrl("/login.xhtml");
        // not needed as JSF 2.2 is implicitly protected against CSRF
        http.csrf().disable();

 */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(env.getProperty("usersByUsernameQuery"))
                .authoritiesByUsernameQuery(env.getProperty("authoritiesByUsernameQuery"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
