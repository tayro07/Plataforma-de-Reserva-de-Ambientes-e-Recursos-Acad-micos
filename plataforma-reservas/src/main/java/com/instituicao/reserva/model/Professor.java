package com.instituicao.reserva.model;

public class Professor extends Usuario {
    private static final long serialVersionUID = 1L;

    public Professor(String id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public String getTipo() { return "PROFESSOR"; }

    @Override
    public int getLimiteReservasSimultaneas() { return 10; } // Conforme requisito

    @Override
    public boolean requerAprovacaoCoordenacao() { return false; } // Aprovação automática
}