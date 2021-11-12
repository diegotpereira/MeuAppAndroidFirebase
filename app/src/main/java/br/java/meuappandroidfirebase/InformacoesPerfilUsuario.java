package br.java.meuappandroidfirebase;

public class InformacoesPerfilUsuario {

    private String nome;
    private String idade;
    private String fone;
    private String endereco;
    private String cep;
    private String email;

    public InformacoesPerfilUsuario() {
    }

    public InformacoesPerfilUsuario(String nome, String idade, String fone, String endereco, String cep, String email) {
        this.nome = nome;
        this.idade = idade;
        this.fone = fone;
        this.endereco = endereco;
        this.cep = cep;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setCidade(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
