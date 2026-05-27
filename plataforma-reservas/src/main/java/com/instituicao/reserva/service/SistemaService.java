package com.instituicao.reserva.service;

import com.instituicao.reserva.model.*;
import com.instituicao.reserva.repository.DadosRepository;
import java.time.LocalDateTime;
import java.util.List;

public class SistemaService {
    private List<Usuario> usuarios;
    private List<Agendavel> itens;
    private List<Reserva> reservas;
    private int conflitosEvitadosCount = 0;

    @SuppressWarnings("unchecked")
    public SistemaService() {
        // Carrega os dados automaticamente do arquivo externo ao iniciar
        Object[] dados = DadosRepository.carregarDados();
        this.usuarios = (List<Usuario>) dados[0];
        this.itens = (List<Agendavel>) dados[1];
        this.reservas = (List<Reserva>) dados[2];
    }

    public void cadastrarUsuario(Usuario u) {
        usuarios.add(u);
        salvar();
    }

    public void cadastrarItem(Agendavel item) {
        itens.add(item);
        salvar();
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Agendavel> getItens() { return itens; }
    public List<Reserva> getReservas() { return reservas; }

    public void fazerReserva(Usuario usuario, Agendavel item, LocalDateTime inicio, LocalDateTime fim, String finalidade) throws Exception {
        // REQUISITO 1: Verifica limite de reservas ativas simultâneas conforme o tipo de usuário
        long ativas = reservas.stream()
            .filter(r -> r.getUsuario().getId().equals(usuario.getId()) && 
                    (r.getStatus().equals("ATIVA") || r.getStatus().equals("AGUARDANDO_VALIDACAO")))
            .count();
        
        if (ativas >= usuario.getLimiteReservasSimultaneas()) {
            throw new Exception("Limite máximo de reservas simultâneas atingido para o perfil de " + usuario.getTipo() + " (Máx: " + usuario.getLimiteReservasSimultaneas() + ").");
        }

        // REQUISITO 2: Algoritmo para Impedir conflitos de horário no mesmo ambiente/recurso
        for (Reserva r : reservas) {
            if (r.getItem().getCodigo().equalsIgnoreCase(item.getCodigo()) && 
               (r.getStatus().equals("ATIVA") || r.getStatus().equals("AGUARDANDO_VALIDACAO"))) {
                
                // Checa sobreposição de períodos
                if (inicio.isBefore(r.getDataHoraFim()) && fim.isAfter(r.getDataHoraInicio())) {
                    conflitosEvitadosCount++; // Incrementa o indicador do painel analítico
                    throw new Exception("CONFLITO DE HORÁRIO DETECTADO! O recurso '" + item.getCodigo() + "' já está ocupado neste período.");
                }
            }
        }

        // Cria a reserva (O status inicial "ATIVA" ou "PENDENTE" é definido polimorficamente pelo tipo de usuário)
        Reserva nova = new Reserva("RES-" + (reservas.size() + 1), usuario, item, inicio, fim, finalidade);
        reservas.add(nova);
        salvar();
    }

    // REQUISITO 3: Registrar devolução, atrasos e calcular penalidades dinamicamente
    public void finalizarReserva(String idReserva, long horasAtraso, String dano) throws Exception {
        Reserva res = reservas.stream()
            .filter(r -> r.getId().equalsIgnoreCase(idReserva))
            .findFirst()
            .orElseThrow(() -> new Exception("Reserva não encontrada."));

        if (!res.getStatus().equals("ATIVA")) {
            throw new Exception("Esta reserva não está ativa para ser finalizada.");
        }

        res.setStatus("CONCLUIDA");
        if (horasAtraso > 0 || !dano.equalsIgnoreCase("NENHUM")) {
            // Polimorfismo em ação: calcula a multa com base nas regras da interface Agendavel
            double multa = res.getItem().calcularPenalidadeAtraso(horasAtraso);
            res.setOcorrencia("Atraso: " + horasAtraso + "h | Danos: " + dano + " | Penalidade: R$ " + multa);
        } else {
            res.setOcorrencia("Uso concluído sem pendências.");
        }
        salvar();
    }

    // Validação da coordenação para usuários como Alunos
    public void aprovarReserva(String idReserva) throws Exception {
        Reserva res = reservas.stream()
            .filter(r -> r.getId().equalsIgnoreCase(idReserva))
            .findFirst()
            .orElseThrow(() -> new Exception("Reserva não encontrada."));

        if (res.getStatus().equals("AGUARDANDO_VALIDACAO")) {
            res.setStatus("ATIVA");
            salvar();
        } else {
            throw new Exception("Esta reserva não necessita de aprovação da coordenação.");
        }
    }

    // REQUISITO 4: Painel Analítico com os indicadores mínimos solicitados
    public void exibirIndicadores() {
        System.out.println("\n=======================================================");
        System.out.println("          PAINEL ANALÍTICO E INDICADORES MÍNIMOS");
        System.out.println("=======================================================");
        System.out.println("• Total de Reservas Solicitadas: " + reservas.size());
        System.out.println("• Conflitos Evitados pelo Sistema: " + conflitosEvitadosCount);
        
        long ativas = reservas.stream().filter(r -> r.getStatus().equals("ATIVA")).count();
        long pendentes = reservas.stream().filter(r -> r.getStatus().equals("AGUARDANDO_VALIDACAO")).count();
        long concluidasComOcorrencia = reservas.stream().filter(r -> r.getStatus().equals("CONCLUIDA") && !r.getOcorrencia().contains("sem pendências")).count();
        
        System.out.println("• Reservas Ativas no Momento: " + ativas);
        System.out.println("• Reservas Aguardando Coordenação: " + pendentes);
        System.out.println("• Devoluções com Registros de Atraso/Avaria: " + concluidasComOcorrencia);
        
        // Taxa de Ocupação Simples
        double taxaOcupacao = itens.isEmpty() ? 0 : ((double) (ativas + concluidasComOcorrencia) / itens.size()) * 10.0;
        System.out.printf("• Taxa Estimada de Ocupação dos Recursos: %.2f%%\n", Math.min(taxaOcupacao, 100.0));
        System.out.println("=======================================================");
    }

    private void salvar() {
        DadosRepository.salvarDados(usuarios, itens, reservas);
    }
}
