package com.ucaldas.otri.application.auth.services;

import com.ucaldas.otri.application.auth.models.AuthenticationRequest;
import com.ucaldas.otri.application.auth.models.AuthenticationResponse;
import com.ucaldas.otri.application.auth.models.RegisterRequest;
import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.exceptions.ErrorCodes;
import com.ucaldas.otri.domain.services.IJwtService;
import com.ucaldas.otri.domain.users.entities.RoleType;
import com.ucaldas.otri.domain.users.entities.User;
import com.ucaldas.otri.domain.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request){

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApplicationException(
                    "User with email " + request.getEmail() + " already exists",
                    ErrorCodes.VALIDATION_ERROR
            );
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleType.Admin)
                .build();

        repository.save(user);
        return "Usuario registrado exitosamente";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex){
            throw new ApplicationException(
                    "Usuario o contraseña inválidos",
                    ErrorCodes.INVALID_CREDENTIALS
            );
        }

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationException("No se ha encontrado el usuario", ErrorCodes.VALIDATION_ERROR));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
