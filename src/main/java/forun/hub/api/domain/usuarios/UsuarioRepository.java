package forun.hub.api.domain.usuarios;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    Usuario findByEmail(String email);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    Page<Usuario> findByCursoNomeAndAnoAndAtivoTrue(String nomeCurso, Integer ano, Pageable paginacao);

    Page<Usuario> findByCursoNomeAndAtivoTrue(String nomeCurso, Pageable paginacao);

    Page<Usuario> findByAnoAndAtivoTrue(Integer ano, Pageable paginacao);
}
