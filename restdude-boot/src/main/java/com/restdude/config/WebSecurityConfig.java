/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.config;

import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.AnonymousAuthenticationFilter;
import com.restdude.auth.userdetails.util.RestAuthenticationEntryPoint;
import com.restdude.domain.users.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private String anonymousKey = UUID.randomUUID().toString();

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setRestAuthenticationEntryPoint(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new AnonymousAuthenticationFilter(anonymousKey);
    }

    @Bean
    public AnonymousAuthenticationProvider anonymousAuthenticationProvider() {
        return new AnonymousAuthenticationProvider(anonymousKey);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/resources/**", "/", "/client/**", "/template/**/*.hbs", "/fonts/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*

        <!-- allow anonymous register/login etc. -->
        <sec:intercept-url pattern="/apiauth/**" access="permitAll()"/>
        <sec:intercept-url pattern="/api/auth/**" access="permitAll()"/>

        <!-- for spring social login/signup -->
        <sec:intercept-url pattern="/login" access="permitAll"/>
        <sec:intercept-url pattern="/signin/**" access="permitAll"/>
        <sec:intercept-url pattern="/signup/**" access="permitAll"/>

        <!-- protect REST API  modifying methods-->
        <sec:intercept-url pattern="/api/rest/**" method="POST" access="isAuthenticated()"/>
        <sec:intercept-url pattern="/api/rest/**" method="PUT" access="isAuthenticated()"/>
        <sec:intercept-url pattern="/api/rest/**" method="PATCH" access="isAuthenticated()"/>
        <sec:intercept-url pattern="/api/rest/**" method="DELETE" access="isAuthenticated()"/>

        * */
        http.exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().defaultsDisabled().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors().disable()
                .rememberMe().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/apiauth/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signin/**").permitAll()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/api/management/**").hasAnyAuthority(Role.ROLE_ADMIN)
                .antMatchers("/v2/api-docs").hasAnyAuthority(Role.ROLE_USER)
                .antMatchers(HttpMethod.POST, "/api/rest/**").hasAnyAuthority(Role.ROLE_USER)
                .antMatchers(HttpMethod.PATCH, "/api/rest/**").hasAnyAuthority(Role.ROLE_USER)
                .antMatchers(HttpMethod.PUT, "/api/rest/**").hasAnyAuthority(Role.ROLE_USER)
                .antMatchers(HttpMethod.DELETE, "/api/rest/**").hasAnyAuthority(Role.ROLE_USER)

                .anyRequest().authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(this.restAuthenticationEntryPoint)
                .and()
                .anonymous().disable()
                .addFilterAt(
                        anonymousAuthenticationFilter(),
                        org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class)
                .logout().invalidateHttpSession(true);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}