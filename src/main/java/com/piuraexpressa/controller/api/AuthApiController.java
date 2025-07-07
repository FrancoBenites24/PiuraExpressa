package com.piuraexpressa.controller.api;
/* 
import com.piuraexpressa.dto.LoginRequest;
import com.piuraexpressa.dto.LoginResponse;
import com.piuraexpressa.seguridad.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;*/

/*@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor */
public class AuthApiController {
/* 
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generarToken(userDetails.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }*/
}
