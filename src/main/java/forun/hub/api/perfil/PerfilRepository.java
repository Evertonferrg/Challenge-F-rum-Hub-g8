package forun.hub.api.perfil;

import forun.hub.api.perfis.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}
