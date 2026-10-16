package sptech.school.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.backend.dto.LoginRequestDto;
import sptech.school.backend.dto.UsuarioRequestDto;
import sptech.school.backend.dto.UsuarioResponseDto;
import sptech.school.backend.exception.UsuarioNaoEncontradoException;
import sptech.school.backend.service.UsuarioService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(
            @PathVariable Integer id
    ) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(id));
        } catch (UsuarioNaoEncontradoException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(
            @Valid @RequestBody UsuarioRequestDto dto
    ) {
        try {
            return ResponseEntity.status(201).body(usuarioService.cadastrar(dto));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDto dto
    ) {
        try {
            return ResponseEntity.ok(usuarioService.atualizar(id, dto));
        } catch (UsuarioNaoEncontradoException exception) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (UsuarioNaoEncontradoException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDto> logar(
            @Valid @RequestBody LoginRequestDto dto
    ) {
        try {
            return ResponseEntity.ok(usuarioService.logar(dto));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(401).build();
        }
    }
}