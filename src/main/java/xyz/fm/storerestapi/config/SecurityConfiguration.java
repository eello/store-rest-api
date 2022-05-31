package xyz.fm.storerestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.fm.storerestapi.jwt.JwtAccessDeniedHandler;
import xyz.fm.storerestapi.jwt.JwtAuthenticationEntryPoint;
import xyz.fm.storerestapi.jwt.JwtProvider;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ROLE_VENDOR_EXECUTIVE = "VENDOR_EXECUTIVE";

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfiguration(
            JwtProvider jwtProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtProvider = jwtProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, permitAllPostPatterns()).permitAll()
                .antMatchers(HttpMethod.GET, permitHasRoleExecutiveGetPatterns()).hasRole(ROLE_VENDOR_EXECUTIVE)
                .antMatchers(HttpMethod.PATCH, permitHasRoleExecutivePatchPatterns()).hasRole(ROLE_VENDOR_EXECUTIVE)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfiguration(jwtProvider));
    }

    private String[] permitAllPostPatterns() {
        return new String[]{
                "/vendor", "/vendor/manager"
        };
    }

    private String[] permitHasRoleExecutiveGetPatterns() {
        return new String[]{
                "/vendor/manager"
        };
    }

    private String[] permitHasRoleExecutivePatchPatterns() {
        return new String[]{
                "/vendor/manager/approve/{targetId}"
        };
    }
}
