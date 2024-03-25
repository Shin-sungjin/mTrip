package com.example.test;

import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.example.test.User.DTO.User_Role;
import com.example.test.User.Service.UserService;
import com.mysql.cj.protocol.AuthenticationProvider;

import lombok.RequiredArgsConstructor;


@EnableWebSecurity(debug = false)
@EnableMethodSecurity(mode =  AdviceMode.PROXY)
@Configuration
@RequiredArgsConstructor
public class Security_Config  {

	UserDetailsService userDetailsService;
	
//	@Bean
//	public void configure(WebSecurity web) throws Exception {
//	    (web)->web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//	}
	
	
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("**.js", "**.css", "**.img");
     }

	@Bean
    protected SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests)-> authorizeHttpRequests
                	 .requestMatchers("/login_form", "main/**", "/**").permitAll()
                         .anyRequest( ).authenticated());
	
//        http
//        .rememberMe((remember)->remember
//        		.rememberMeServices(rememberMeServices(userDetailsService)))
//        		;
       http.rememberMe(
    		   (rememberMe) -> rememberMe
    		   .rememberMeParameter("remember")   // login 시 기억할 것인지에 대한 chk box name 값
    		   .tokenValiditySeconds(3600)  //토큰 유효 시간 
    	       .alwaysRemember(false)
    	       .userDetailsService(userDetailsService)
    		   );

     
        
        
         http.formLogin((formLogin)->formLogin
        		.loginPage("/login_form")
                .permitAll())
        		.securityContext((securityContext)->securityContext
        				.securityContextRepository(new DelegatingSecurityContextRepository(
        						new RequestAttributeSecurityContextRepository(),
        						new HttpSessionSecurityContextRepository())))
        
        ;
        
        
        http
        .sessionManagement((sessionManagement)->sessionManagement.invalidSessionUrl("/login_A"))
        .logout((logout)->logout
        	    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        ;
        
        
        http.csrf((csrf)->csrf.ignoringRequestMatchers("/**").csrfTokenRepository((CookieCsrfTokenRepository.withHttpOnlyFalse())));
        return http.build();
        
		//위는 테스 트 끝.	
        //defalutURL 수정 or 삭제 필요 테스트 이후 진행 작업자: 신성진 
        

    }
	
	
	
    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
    	RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
    	TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("key", userDetailsService, encodingAlgorithm);
    	rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
    	return rememberMe;
    }
    
    /** 구현 중 */
//    @Bean
//    RememberMeAuthenticationFilter rememberMeFilter() {
//        RememberMeAuthenticationFilter rememberMeFilter = new RememberMeAuthenticationFilter(null, null);
//        rememberMeFilter.setRememberMeServices(rememberMeServices());
//        rememberMeFilter.setAuthenticationManager(theAuthenticationManager);
//        return rememberMeFilter;
//    }
//
//    @Bean
//    TokenBasedRememberMeServices rememberMeServices() {
//        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("key", userDetailsService);
//        rememberMeServices.setUserDetailsService(myUserDetailsService);
//        rememberMeServices.setKey("springRocks");
//        return rememberMeServices;
//    }
//
//    @Bean
//    RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
//        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider("key");
//        rememberMeAuthenticationProvider.setKey("springRocks");
//        return rememberMeAuthenticationProvider;
//    }
    
    
//	@Bean
//	SecurityFilterChain  filterChain(HttpSecurity htsp) throws Exception{
//		
//		http
//		.authorizeHttpRequests( //HTTP 인가 설정 
//				(authorizeHttpRequests) -> 
//				 authorizeHttpRequests
//				 .requestMatchers(
//				  new AntPathRequestMatcher("/**")).permitAll()
//				)
//		;

	
			
		 
//		http
//		.authorizeHttpRequests((authorizeHttpRequests) -> 
//		 authorizeHttpRequests
//		 .requestMatchers(
//		  new AntPathRequestMatcher("/user/**")).denyAll())
//			.formLogin((formLogin) -> formLogin.loginPage("login_form"))
//			
//		.logout((logout) -> logout.logoutSuccessUrl("/"))
//		;s
	
//		http.authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests
//				.requestMatchers(new AntPathRequestMatcher("/mypage")).denyAll());
//		
//		return http.build();
//		
//	}
	
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
