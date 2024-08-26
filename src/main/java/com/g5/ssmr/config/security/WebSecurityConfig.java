package com.g5.ssmr.config.security;

import com.g5.ssmr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/external/**").permitAll()
                .antMatchers(HttpMethod.GET, "/external/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/external/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/index.html",
                "/favicon.ico",
                "/main.be1abdbec295dcb14df2.js",
                "/polyfills.29c7b32d441016b25970.js",
                "/styles.f17e3f7d486136f03217.css",
                "/runtime.0e49e2b53282f40c8925.js",
                "/assets/**",
                "/"
        );
        userService.createAdminUser();
    }
}
