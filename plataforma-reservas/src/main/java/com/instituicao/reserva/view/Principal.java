package com.instituicao.reserva.view;

import com.instituicao.reserva.model.*;
import com.instituicao.reserva.service.SistemaService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        SistemaService service = new SistemaService();
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // CARGA INICIAL DE TESTES: Se a base de dados estiver vazia, cria dados automaticamente
        if (service.getUsuarios().isEmpty()) {
            service.cadastrarUsuario(new Professor("P1", "Prof. Alexandre", "alexandre@faculdade.com"));
            service.cadastrarUsuario(new Aluno("A1", "Ronald Damasceno (Aluno)", "ronald@estudante.com"));
            service.cadastrarItem(new Ambiente("LAB1", "Laboratório de Informática 3", "Bloco B, Sala 202", 40));
            service.cadastrarItem(new Recurso("NOTE1", "Kit Notebook Dell + Projetor", "Armário Central", true));
        }

        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("   PLATAFORMA DE RESERVA DE AMBIENTES E RECURSOS");
            System.out.println("=======================================================");
            System.out.println("1. Listar Usuários e Itens Disponíveis");
            System.out.println("2. Solicitar Nova Reserva (Evita Conflitos)");
            System.out.println("3. Validar/Aprovar Reservas Pendentes (Coordenação)");
            System.out.println("4. Registrar Devolução / Finalizar Reserva (Penalidades)");
            System.out.println("5. Ver Relatórios e Painel de Indicadores");
            System.out.println("6. Sair do Sistema");
            System.out.print("Escolha uma opção: ");
            
            int opcao = sc.nextInt();
            sc.nextLine(); // Limpa o buffer do teclado

            try {
                switch (opcao) {
                    case 1:
                        System.out.println("\n--- USUÁRIOS CADASTRADOS ---");
                        for (Usuario u : service.getUsuarios()) {
                            System.out.println("[" + u.getId() + "] " + u.getNome() + " | Perfil: " + u.getTipo() + " | Limite Ativas: " + u.getLimiteReservasSimultaneas());
                        }
                        System.out.println("\n--- AMBIENTES E RECURSOS ---");
                        for (Agendavel i : service.getItens()) {
                            System.out.println("[" + i.getCodigo() + "] " + i.getDescricao() + " | Local: " + i.getLocalizacao());
                        }
                        break;

                    case 2:
                        System.out.print("Digite o ID do Usuário (Ex: P1 ou A1): ");
                        String idU = sc.nextLine();
                        Usuario user = service.getUsuarios().stream()
                                .filter(u -> u.getId().equalsIgnoreCase(idU)).findFirst()
                                .orElseThrow(() -> new Exception("Usuário não cadastrado."));

                        System.out.print("Digite o Código do Recurso/Ambiente (Ex: LAB1 ou NOTE1): ");
                        String idI = sc.nextLine();
                        Agendavel item = service.getItens().stream()
                                .filter(i -> i.getCodigo().equalsIgnoreCase(idI)).findFirst()
                                .orElseThrow(() -> new Exception("Item não cadastrado."));

                        System.out.print("Data/Hora Início (Formato: dd/MM/yyyy HH:mm): ");
                        LocalDateTime inicio = LocalDateTime.parse(sc.nextLine(), dtf);
                        
                        System.out.print("Data/Hora Fim (Formato: dd/MM/yyyy HH:mm): ");
                        LocalDateTime fim = LocalDateTime.parse(sc.nextLine(), dtf);

                        System.out.print("Finalidade da Reserva: ");
                        String finalidade = sc.nextLine();

                        service.fazerReserva(user, item, inicio, fim, finalidade);
                        System.out.println("\n» Operação processada! Verifique o status final no menu 5.");
                        break;

                    case 3:
                        System.out.println("\n--- RESERVAS AGUARDANDO VALIDAÇÃO DA COORDENAÇÃO ---");
                        long pendentes = service.getReservas().stream().filter(r -> r.getStatus().equals("AGUARDANDO_VALIDACAO")).count();
                        if (pendentes == 0) {
                            System.out.println("Nenhuma reserva pendente.");
                        } else {
                            service.getReservas().stream()
                                    .filter(r -> r.getStatus().equals("AGUARDANDO_VALIDACAO"))
                                    .forEach(r -> System.out.println("[" + r.getId() + "] Usuário: " + r.getUsuario().getNome() + " | Solicitou: " + r.getItem().getCodigo() + " para " + r.getFinalidade()));
                            
                            System.out.print("\nDigite o ID da Reserva que deseja APROVAR (Ex: RES-1): ");
                            String idAprovar = sc.nextLine();
                            service.aprovarReserva(idAprovar);
                            System.out.println("» Reserva aprovada e ativada com sucesso!");
                        }
                        break;

                    case 4:
                        System.out.println("\n--- RESERVAS ATIVAS EM ANDAMENTO ---");
                        long ativas = service.getReservas().stream().filter(r -> r.getStatus().equals("ATIVA")).count();
                        if (ativas == 0) {
                            System.out.println("Nenhuma reserva ativa no momento.");
                        } else {
                            service.getReservas().stream()
                                    .filter(r -> r.getStatus().equals("ATIVA"))
                                    .forEach(r -> System.out.println("[" + r.getId() + "] " + r.getUsuario().getNome() + " está utilizando o recurso " + r.getItem().getCodigo()));
                            
                            System.out.print("\nDigite o ID da Reserva para registrar a Devolução: ");
                            String idFin = sc.nextLine();
                            System.out.print("Houve quantas horas de atraso na entrega? (Digite 0 para nenhuma): ");
                            long horas = sc.nextLong();
                            sc.nextLine(); // Limpa buffer
                            System.out.print("Descreva avarias/danos se houver (ou digite NENHUM): ");
                            String dano = sc.nextLine();

                            service.finalizarReserva(idFin, horas, dano);
                            System.out.println("» Devolução realizada e arquivada.");
                        }
                        break;

                    case 5:
                        System.out.println("\n=======================================================");
                        System.out.println("               RELATÓRIO GERAL DE RESERVAS             ");
                        System.out.println("=======================================================");
                        if (service.getReservas().isEmpty()) {
                            System.out.println("Nenhuma reserva efetuada até o momento.");
                        } else {
                            for (Reserva r : service.getReservas()) {
                                System.out.println("[" + r.getId() + "] Período: " + r.getDataHoraInicio().format(dtf) + " até " + r.getDataHoraFim().format(dtf));
                                System.out.println("     Item: " + r.getItem().getCodigo() + " | Responsável: " + r.getUsuario().getNome());
                                System.out.println("     Status: " + r.getStatus() + " | Registro/Ocorrência: " + r.getOcorrencia());
                                System.out.println("-------------------------------------------------------");
                            }
                        }
                        service.exibirIndicadores();
                        break;

                    case 6:
                        System.out.println("Encerrando a plataforma com segurança. Dados salvos!");
                        System.exit(0);

                    default:
                        System.out.println("Opção inválida! Escolha um número de 1 a 6.");
                }
            } catch (Exception e) {
                System.out.println("\n⚠ AVISO DO SISTEMA: " + e.getMessage());
            }
        }
    }
}