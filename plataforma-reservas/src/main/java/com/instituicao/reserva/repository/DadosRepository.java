package com.instituicao.reserva.repository;

import com.instituicao.reserva.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DadosRepository {
    private static final String FILE_NAME = "dados_sistema.dat";

    @SuppressWarnings("unchecked")
    public static void salvarDados(List<Usuario> usuarios, List<Agendavel> itens, List<Reserva> reservas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(usuarios);
            oos.writeObject(itens);
            oos.writeObject(reservas);
        } catch (IOException e) {
            System.out.println("Erro ao salvar base de dados externa: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] carregarDados() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new Object[]{new ArrayList<Usuario>(), new ArrayList<Agendavel>(), new ArrayList<Reserva>()};
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Usuario> usuarios = (List<Usuario>) ois.readObject();
            List<Agendavel> itens = (List<Agendavel>) ois.readObject();
            List<Reserva> reservas = (List<Reserva>) ois.readObject();
            return new Object[]{usuarios, itens, reservas};
        } catch (Exception e) {
            System.out.println("Criando nova base de dados limpa...");
            return new Object[]{new ArrayList<Usuario>(), new ArrayList<Agendavel>(), new ArrayList<Reserva>()};
        }
    }
}