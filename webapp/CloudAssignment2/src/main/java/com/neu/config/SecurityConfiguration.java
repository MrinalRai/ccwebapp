package com.neu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
//@ComponentScan("org.cc6225.security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicAuthenticationProvider authProvider;
    
//    @Autowired
//    private RestAuthenticationEntryPoint basicAuthEntryPoint;

	
    public SecurityConfiguration() {
    	super();
	};

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
    
    @Override
	public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/v1/user" );
        //web.ignoring().antMatchers(HttpMethod.POST, "/reset" );
    	
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
    	// Disable csrf 
    	http.csrf().disable();
        // authorize all requests after /**
    	//http.authorizeRequests().antMatchers("/**").authenticated().and().httpBasic().authenticationEntryPoint(basicAuthEntryPoint);
        // Create session as Stateless inorder to prevent session id in cookies
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
