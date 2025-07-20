package forun.hub.api.domain.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


       @Autowired
        private UsuarioRepository usuarioRepository;

        public Page<DadosListagemUsuario> buscarPorCursoEAno(String nomeCurso, Integer ano, Pageable paginacao) {
            Page<Usuario> usuarios;

            if (nomeCurso != null && ano != null) {
                usuarios = usuarioRepository.findByCursoNomeAndAnoAndAtivoTrue(nomeCurso, ano, paginacao);
            } else if (nomeCurso != null) {
                usuarios = usuarioRepository.findByCursoNomeAndAtivoTrue(nomeCurso, paginacao);
            } else if (ano != null) {
                usuarios = usuarioRepository.findByAnoAndAtivoTrue(ano, paginacao);
            } else {
                usuarios = usuarioRepository.findAllByAtivoTrue(paginacao);
            }

            return usuarios.map(DadosListagemUsuario::new);
        }
    }


