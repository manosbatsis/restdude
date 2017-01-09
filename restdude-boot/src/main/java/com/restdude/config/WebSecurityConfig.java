/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-boot, https://manosbatsis.github.io/restdude/restdude-boot
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.config;

import com.restdude.auth.userdetails.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
//@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().
                httpBasic().and().
                csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
/*
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return this.methodSecurityExpressionHandler();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        com.restdude.auth.spel.binding.MethodSecurityExpressionHandler expressionHandler = new com.restdude.auth.spel.binding.MethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new ModelPermissionEvaluator());
        expressionHandler.setParameterNameDiscoverer(new AnnotationParameterNameDiscoverer(
                P.class.getCanonicalName(),
                Param.class.getCanonicalName(),
                PathVariable.class.getCanonicalName(),
                RequestBody.class.getCanonicalName(),
                RequestParam.class.getCanonicalName()));
        return expressionHandler;
    }
    */

}