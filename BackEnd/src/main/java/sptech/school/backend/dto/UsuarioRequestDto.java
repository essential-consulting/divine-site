package sptech.school.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDto {

        @NotBlank
        @Size(min = 3, max = 100)
        private String nome;

        @NotBlank
        private String sobrenome;

        @NotBlank
        @Size(min = 11, max = 11)
        private String telefone;

        @NotBlank
        @Email
        private String email;

        private String senha;
        private Boolean ativo;
        private String tipoUsuario;

        public UsuarioRequestDto() {
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getSobrenome() {
                return sobrenome;
        }

        public void setSobrenome(String sobrenome) {
                this.sobrenome = sobrenome;
        }

        public String getTelefone() {
                return telefone;
        }

        public void setTelefone(String telefone) {
                this.telefone = telefone;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getSenha() {
                return senha;
        }

        public void setSenha(String senha) {
                this.senha = senha;
        }

        public Boolean getAtivo() {
                return ativo;
        }

        public void setAtivo(Boolean ativo) {
                this.ativo = ativo;
        }

        public String getTipoUsuario() {
                return tipoUsuario;
        }

        public void setTipoUsuario(String tipoUsuario) {
                this.tipoUsuario = tipoUsuario;
        }
}