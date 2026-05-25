package com.instituicao.reserva.model;

public class Aluno extends Usuario {
    private static final long serialVersionUID = 1L;

    public Aluno(String id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public String getTipo() { return "ALUNO_AUTORIZADO"; }

    @Override
    public int getLimiteReservasSimultaneas() { return 3; } // Menor limite

    @Override
    public boolean requerAprovacaoCoordenacao() { return true; } // Requer validação da coordenação
}