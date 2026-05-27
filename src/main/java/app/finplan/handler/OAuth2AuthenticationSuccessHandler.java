package app.finplan.handler;

import app.finplan.model.User;
import app.finplan.model.UserRole;
import app.finplan.repositories.UserRepository;
import app.finplan.security.CustomUserDetailsService;
import app.finplan.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String sub = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        //String name = oauth2User.getAttribute("name");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        //String picture = oauth2User.getAttribute("picture");

        User user = userRepository.findByGoogleSub(sub)
                .orElseGet(() -> userRepository.findByEmail(email)
                        .map(existing -> linkGoogle(existing, sub, email, firstName, lastName))
                        .orElseGet(() -> createGoogleUser(sub, email, firstName, lastName)));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(baseUrl + "/oauth/success")
                .queryParam("token", accessToken)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private User linkGoogle(User user, String sub, String email, String firstName, String lastName) {
        user.setGoogleSub(sub);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    private User createGoogleUser(String sub, String email, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGoogleSub(sub);
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }
}
