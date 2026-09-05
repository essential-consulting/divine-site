package sptech.school.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Buscar usuario por id. Ex: /usuarios/1
    // if found 200 else 404
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId (@PathVariable Integer id){

        try {
            String sql = """
                    SELECT * FROM usuario
                    WHERE id = ?
                    """;

            Usuario usuario = jdbcTemplate.queryForObject(
                    sql, new BeanPropertyRowMapper<>(Usuario.class),
                    id
            );
            return ResponseEntity.status(200).body(usuario);
        } catch (EmptyResultDataAccessException exception){
            return ResponseEntity.status(404).build();
        }
    }

    // Cadastro
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario novoUsuario) {
        if (novoUsuario.getNome() == null || novoUsuario.getNome().isBlank() ||
                novoUsuario.getSobrenome() == null || novoUsuario.getSobrenome().isBlank() ||
                novoUsuario.getTelefone() == null || novoUsuario.getTelefone().isBlank() ||
                novoUsuario.getEmail() == null || novoUsuario.getEmail().isBlank() ||
                novoUsuario.getSenha() == null || novoUsuario.getSenha().isBlank()) {

            return ResponseEntity.status(400).build();
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());

        String sqlInsert = """
                INSERT INTO usuario (nome, sobrenome, telefone, email, senha) 
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sqlInsert, novoUsuario.getNome(), novoUsuario.getSobrenome(), novoUsuario.getTelefone(), novoUsuario.getEmail(), senhaCriptografada);

        novoUsuario.setSenha(null);

        return ResponseEntity.status(201).body(novoUsuario);
    }

    // Fazer validação de login de usuário
    // if (email) exists and (usuario.senha == usuarioBanco.senha) then 200
    @PostMapping("/login")
    public ResponseEntity<Usuario> logar(@RequestBody Usuario usuario) {
        try {
            String sqlLogin = """
            SELECT * FROM usuario 
            WHERE email = ? 
            """;

            Usuario usuarioBanco = jdbcTemplate.queryForObject(
                    sqlLogin,
                    new BeanPropertyRowMapper<>(Usuario.class),
                    usuario.getEmail()
            );

            boolean senhaValida = passwordEncoder.matches(usuario.getSenha(), usuarioBanco.getSenha());

            if(!senhaValida) {
                return ResponseEntity.status(401).build();
            }

            usuarioBanco.setSenha(null);

            return ResponseEntity.status(200).body(usuarioBanco);
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.status(401).build();
        }

    }
}
