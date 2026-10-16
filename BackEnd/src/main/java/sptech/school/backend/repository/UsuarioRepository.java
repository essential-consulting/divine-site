package sptech.school.backend.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sptech.school.backend.entity.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuario";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Usuario.class)
        );
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        List<Usuario> usuarios = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Usuario.class),
                id
        );

        return usuarios.stream().findFirst();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        List<Usuario> usuarios = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Usuario.class),
                email
        );

        return usuarios.stream().findFirst();
    }

    public Integer cadastrar(Usuario usuario) {
        String sql = """
                INSERT INTO usuario
                (nome, sobrenome, telefone, email, senha, ativo, dt_cadastro, tipo_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAtivo(),
                usuario.getDtCadastro(),
                usuario.getTipoUsuario()
        );

        return jdbcTemplate.queryForObject(
                "SELECT id FROM usuario WHERE email = ?",
                Integer.class,
                usuario.getEmail()
        );
    }

    public boolean atualizar(Integer id, Usuario usuario) {
        String sql = """
                UPDATE usuario
                SET nome = ?, sobrenome = ?, telefone = ?,
                    email = ?, senha = ?, ativo = ?, tipo_usuario = ?
                WHERE id = ?
                """;

        int linhas = jdbcTemplate.update(
                sql,
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAtivo(),
                usuario.getTipoUsuario(),
                id
        );

        return linhas > 0;
    }

    public boolean excluir(Integer id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        return jdbcTemplate.update(sql, id) > 0;
    }
}