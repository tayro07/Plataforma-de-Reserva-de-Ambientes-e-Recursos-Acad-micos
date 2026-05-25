package com.instituicao.reserva.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Usuario usuario;
    private Agendavel item;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String finalidade;
    private String status; // ATIVA, CONCLUIDA, AGUARDANDO_VALIDACAO
    private String ocorrencia;

    public Reserva(String id, Usuario usuario, Agendavel item, LocalDateTime inicio, LocalDateTime fim, String finalidade) {
        this.id = id;
        this.usuario = usuario;
        this.item = item;
        this.dataHoraInicio = inicio;
        this.dataHoraFim = fim;
        this.finalidade = finalidade;
        // Se o usuário exigir aprovação, começa pendente, senão começa Ativa
        this.status = usuario.requerAprovacaoCoordenacao() ? "AGUARDANDO_VALIDACAO" : "ATIVA";
        this.ocorrencia = "Nenhuma";
    }

    public String getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Agendavel getItem() { return item; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOcorrencia() { return ocorrencia; }
    public void setOcorrencia(String ocorrencia) { this.ocorrencia = ocorrencia; }
    public String getFinalidade() { return finalidade; }
}