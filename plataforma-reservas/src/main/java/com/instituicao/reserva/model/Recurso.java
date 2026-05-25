package com.instituicao.reserva.model;

public class Recurso implements Agendavel {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nome;
    private String localizacao;
    private boolean disponivel;
    private boolean fragil;

    public Recurso(String codigo, String nome, String localizacao, boolean fragil) {
        this.codigo = codigo;
        this.nome = nome;
        this.localizacao = localizacao;
        this.fragil = fragil;
        this.disponivel = true;
    }

    @Override
    public String getCodigo() { return codigo; }
    @Override
    public String getTipo() { return "RECURSO"; }
    @Override
    public String getLocalizacao() { return localizacao; }
    @Override
    public int getCapacidade() { return 1; }
    @Override
    public boolean isDisponivel() { return disponivel; }
    @Override
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    
    @Override
    public String getDescricao() { 
        return "Equipamento: " + nome + (fragil ? " [FRÁGIL - Requer Técnico]" : ""); 
    }

    @Override
    public double calcularPenalidadeAtraso(long horasAtraso) {
        double base = horasAtraso * 20.0;
        return fragil ? base * 1.5 : base; // Recursos frágeis têm multa maior
    }
}