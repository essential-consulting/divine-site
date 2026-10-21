package sptech.school.backend.mapper;

import sptech.school.backend.dto.UsuarioRequestDto;
import sptech.school.backend.dto.UsuarioResponseDto;
import sptech.school.backend.entity.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setSobrenome(dto.getSobrenome());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setAtivo(dto.getAtivo());
        usuario.setTipoUsuario(dto.getTipoUsuario());

        return usuario;
    }

    public static UsuarioResponseDto toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setTelefone(usuario.getTelefone());
        dto.setEmail(usuario.getEmail());
        dto.setAtivo(usuario.getAtivo());
        dto.setDtCadastro(usuario.getDtCadastro());
        dto.setTipoUsuario(usuario.getTipoUsuario());

        return dto;
    }
}