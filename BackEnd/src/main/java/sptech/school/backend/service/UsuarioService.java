package sptech.school.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sptech.school.backend.dto.LoginRequestDto;
import sptech.school.backend.dto.UsuarioRequestDto;
import sptech.school.backend.dto.UsuarioResponseDto;
import sptech.school.backend.entity.Usuario;
import sptech.school.backend.exception.UsuarioNaoEncontradoException;
import sptech.school.backend.mapper.UsuarioMapper;
import sptech.school.backend.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDto> listar() {
        return usuarioRepository.listar()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    public UsuarioResponseDto buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado"));

        return UsuarioMapper.toResponse(usuario);
    }

    public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }

        if (usuarioRepository.buscarPorEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Usuario usuario = UsuarioMapper.toEntity(dto);

        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setAtivo(true);
        usuario.setDtCadastro(LocalDate.now());

        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()) {
            usuario.setTipoUsuario("USUARIO");
        }

        Integer id = usuarioRepository.cadastrar(usuario);
        usuario.setId(id);

        return UsuarioMapper.toResponse(usuario);
    }

    public UsuarioResponseDto atualizar(Integer id, UsuarioRequestDto dto) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (usuarioRepository.buscarPorEmail(dto.getEmail())
                .filter(outro -> !outro.getId().equals(id))
                .isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Usuario usuarioAtualizado = UsuarioMapper.toEntity(dto);

        usuarioAtualizado.setId(id);
        usuarioAtualizado.setDtCadastro(usuario.getDtCadastro());

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            usuarioAtualizado.setSenha(usuario.getSenha());
        } else {
            usuarioAtualizado.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (dto.getAtivo() == null) {
            usuarioAtualizado.setAtivo(usuario.getAtivo());
        }

        if (dto.getTipoUsuario() == null || dto.getTipoUsuario().isBlank()) {
            usuarioAtualizado.setTipoUsuario(usuario.getTipoUsuario());
        }

        usuarioRepository.atualizar(id, usuarioAtualizado);

        return UsuarioMapper.toResponse(usuarioAtualizado);
    }

    public void excluir(Integer id) {
        if (!usuarioRepository.excluir(id)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");
        }
    }

    public UsuarioResponseDto logar(LoginRequestDto dto) {
        Usuario usuario = usuarioRepository.buscarPorEmail(dto.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        return UsuarioMapper.toResponse(usuario);
    }
}