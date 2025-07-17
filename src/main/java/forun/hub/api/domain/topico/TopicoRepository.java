package forun.hub.api.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("""
            SELECT t FROM Topico t
            WHERE (:nomeCurso IS NULL OR t.curso.nome = :nomeCurso)
              AND (:ano IS NULL OR YEAR(t.dataCriacao) = :ano)
            """)
    Page<Topico> buscarPorCursoEAno(
            @Param("nomeCurso") String nomeCurso,
            @Param("ano") Integer ano,
            Pageable pageable
    );
    boolean existsByTitulo(String titulo);

    Page<Topico> findAllByAtivoTrue(Pageable paginacao);
}
