
# Desafio: Plataforma de Reserva de Ambientes e Recursos Acadêmicos 🏫

Este projeto consiste em um ecossistema de software para gerenciamento e reserva de recursos e ambientes acadêmicos (como laboratórios, auditórios e equipamentos), desenvolvido como solução para o Desafio prático de Programação Orientada a Objetos (POO).

O sistema foi projetado utilizando o paradigma de POO em Java, contando com validação dinâmica de regras de negócio por perfil, tratamento de concorrência de horários e uma camada de persistência externa binária para geração de painéis analíticos.

---

## 🧠 Engenharia de Software & Pilares de POO Aplicados

A arquitetura do software demonstra o domínio prático dos conceitos fundamentais exigidos pelo edital:

* **Abstração & Herança:** A classe abstrata `Usuario` serve como modelo base intransitável, sendo estendida pelas classes concretas `Aluno` e `Professor` (Reutilização de código através da relação *"É UM"*).
* **Encapsulamento:** Todos os atributos das classes de modelo foram definidos como `private`, limitando o acesso e a modificação estritamente por meio de métodos públicos acessores (`Getters` e `Setters`).
* **Polimorfismo por Substituição (Sobrescrita):** * Os métodos `getLimiteReservasSimultaneas()` e `requerAprovacaoCoordenacao()` adaptam-se dinamicamente na inicialização de reservas dependendo da instância do usuário.
  * O método `calcularPenalidadeAtraso(long horas)` na interface `Agendavel` calcula multas de forma distinta para `Ambiente` (taxa fixa por espaço) e `Recurso` (taxa variável baseada na fragilidade).
* **Interfaces:** Uso da interface `Agendavel` para unificar o comportamento de ambientes e equipamentos, permitindo que o motor de reservas manipule as entidades de forma genérica.
* **Persistência de Objetos (Serialização):** Uso de `ObjectOutputStream` e `ObjectInputStream` para ler e salvar os estados do sistema em um arquivo local, preservando inclusive as métricas do painel analítico.

---

## 🛠️ Pré-requisitos de Sistema

Antes de iniciar, certifique-se de ter instalado em sua máquina:
* **Java Development Kit (JDK):** Versão 8 ou superior.
* **IDE/Editor:** VS Code (com o *Extension Pack for Java*), IntelliJ IDEA ou Eclipse.

---

## 💻 Como Inicializar e Executar o Projeto

Siga os passos abaixo para rodar a plataforma no seu ambiente local:

### Opção 1: Executando via Terminal (Prompt de Comando)
1. Abra o terminal na pasta raiz do projeto (onde os arquivos estão extraídos).
2. Compile todas as classes do sistema executando o comando:
   ```bash
   javac -d bin src/com/instituicao/reserva/model/*.java src/com/instituicao/reserva/repository/*.java src/com/instituicao/reserva/service/*.java src/com/instituicao/reserva/view/*.java
