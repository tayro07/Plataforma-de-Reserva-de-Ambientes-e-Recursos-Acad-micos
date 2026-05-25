package com.instituicao.reserva.model;

public class Ambiente implements Agendavel {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nome;
    private String localizacao;
    private int capacidade;
    private boolean disponivel;

    public Ambiente(String codigo, String nome, String localizacao, int capacidade) {
        this.codigo = codigo;
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
        this.disponivel = true;
    }

    @Override
    public String getCodigo() { return codigo; }
    @Override
    public String getTipo() { return "AMBIENTE"; }
    @Override
    public String getLocalizacao() { return localizacao; }
    @Override
    public int getCapacidade() { return capacidade; }
    @Override
    public boolean isDisponivel() { return disponivel; }
    @Override
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    
    @Override
    public String getDescricao() { 
        return "Sala/Lab: " + nome + " (Capacidade: " + capacidade + " pessoas)"; 
    }

    @Override
    public double calcularPenalidadeAtraso(long horasAtraso) {
        return horasAtraso * 50.0; // R$50,00 por hora de atraso para ambientes
    }
}