package com.instituicao.reserva.model;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nome;
    private String email;

    public Usuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public abstract String getTipo();
    public abstract int getLimiteReservasSimultaneas();
    public abstract boolean requerAprovacaoCoordenacao();
}