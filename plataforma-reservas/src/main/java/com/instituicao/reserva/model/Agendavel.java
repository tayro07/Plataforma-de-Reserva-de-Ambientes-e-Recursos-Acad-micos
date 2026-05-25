package com.instituicao.reserva.model;

import java.io.Serializable;

public interface Agendavel extends Serializable {
    String getCodigo();
    String getTipo();
    String getLocalizacao();
    int getCapacidade();
    boolean isDisponivel();
    void setDisponivel(boolean disponivel);
    String getDescricao();
    double calcularPenalidadeAtraso(long horasAtraso);
}