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

import com.restdude.auth.jwt.JwtAuthenticationProcessingFilter;
import com.restdude.auth.userdetails.service.UserDetailsService;
import com.restdude.auth.userdetails.util.AnonymousAuthenticationFilter;
import com.restdude.auth.userdetails.util.RestAuthenticationEntryPoint;
import com.restdude.domain.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // JWT
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";


    @Autowired private AuthenticationManager authenticationManager;

    // /JWT
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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/resources/**", "/", "/client/**", "/template/**/*.hbs", "/fonts/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().defaultsDisabled().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                //
                // .addFilterAfter(new CsrfValidatorFilter(), CsrfFilter.class)
                // .csrf().csrfTokenRepository(csrfTokenRepository).ignoringAntMatchers(new String[]{}).and().authorizeRequests()
                .cors()
                .and()
                .rememberMe().disable()
                .addFilterBefore(jwtAuthenticationProcessingFilter(), AnonymousAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/apiauth/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signin/**").permitAll()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/api/management/**").hasAnyAuthority(Roles.ROLE_ADMIN)
                .antMatchers("/v2/api-docs").hasAnyAuthority(Roles.ROLE_USER)
                .antMatchers(HttpMethod.POST, "/api/rest/**").hasAnyAuthority(Roles.ROLE_USER)
                .antMatchers(HttpMethod.PATCH, "/api/rest/**").hasAnyAuthority(Roles.ROLE_USER)
                .antMatchers(HttpMethod.PUT, "/api/rest/**").hasAnyAuthority(Roles.ROLE_USER)
                .antMatchers(HttpMethod.DELETE, "/api/rest/**").hasAnyAuthority(Roles.ROLE_USER)

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
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }



    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() throws Exception {
        JwtAuthenticationProcessingFilter filter
                = new JwtAuthenticationProcessingFilter();
        return filter;
    }
}