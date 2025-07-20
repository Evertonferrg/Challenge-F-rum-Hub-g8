package forun.hub.api.infra.security;

import forun.hub.api.domain.usuarios.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class  SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/login") || (path.equals("/usuarios") && method.equals("POST"))) {
            filterChain.doFilter(request, response);
            return;
        }


        if (tokenJWT != null) {
            try {
                String subject = tokenService.getSubject(tokenJWT);
                System.out.println("Token válido para subject: " + subject);
                var usuario = usuarioRepository.findByEmail(subject);

                if (usuario != null) {
                    System.out.println("Usuário encontrado: " + usuario.getNome());
                    System.out.println("Authorities: " + usuario.getAuthorities());

                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Usuário não encontrado para o subject do token.");
                }

            } catch (Exception e) {
                System.out.println("Token inválido ou expirado: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT inválido ou expirado!");
                return;
            }
        } else {
            System.out.println("Token JWT ausente na requisição.");
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "").trim();
    }
}
