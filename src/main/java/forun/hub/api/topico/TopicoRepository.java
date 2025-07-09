package forun.hub.api.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("""
            SELECT t FROM topico t
            WHERE (:nomeCurso IS NULL OR t.curso.nome = :nomeCurso)
              AND (:ano IS NULL OR YEAR(t.dataCriacao) = :ano)
            """)
    Page<Topico> buscarPorCursoEAno(
            @Param("nomeCurso") String nomeCurso,
            @Param("ano") Integer ano,
            Pageable pageable
    );
}
