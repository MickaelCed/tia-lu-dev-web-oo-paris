import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static ServicoCadastro servicoCadastro = new ServicoCadastro();
    private static ServicoPedido servicoPedido = new ServicoPedido();
    private static CentralDados centralDados = CentralDados.getInstancia();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "Seja bem-vindo (a) ao sistema oficial de Pedidos do Web Delivery! Oque você deseja fazer hoje?"
            );
        
        String input;
        int opcaoSelecionada;
        do {
            exibirMenuPrincipal();
            input = scanner.nextLine();

            try {
                opcaoSelecionada = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcaoSelecionada = -1;
            }

            switch (opcaoSelecionada) {

                // ADICIONAR ITEM
                case 1: 
                    System.out.println("Digite nome do item a ser adicionado: ");
                    String nomeItem = scanner.nextLine();
                    
                    Double preco = null;
                    while (preco == null) {
                        System.out.println("Digite o preço: ");
                        String precoString = scanner.nextLine();
                        try {  // Formatar no formato correto de Double
                            preco = Double.parseDouble(precoString.replace(",", "."));
                        } catch (NumberFormatException e) {
                            System.out.println("Preço inválido! Tente novamente.");
                        };
                    }

                    try { 
                        ItemCardapio i = servicoCadastro.cadastrarItemCardapio(nomeItem, preco);
                    
                        System.out.println("Item cadastrado com sucesso!\n" + "Nome: " + i.getNome() + " || R$: " + i.getPreco());
                    } catch (IllegalArgumentException err) {
                        // Mostrar a exception que o ServicoCadastro nos enviou
                        System.out.println("ERRO, seu item não foi cadastrado : " + err.getMessage());
                    };

                    break;
                

                // VISUALIZAR CARDÁPIO
                case 2:
                    System.out.println(" \n..... Cardapio .....");
                    for (ItemCardapio i : centralDados.getCardapio())
                        System.out.println(" \nNome: " + i.getNome() + " || R$" + i.getPreco());
                    break;

                // ADICIONAR CLIENTE
                case 3: 

                    System.out.println("Digite o nome: ");
                    String nomeCliente = scanner.nextLine();
                    System.out.println("Digite o telefone: ");
                    String telefone = scanner.nextLine();

                    try { 
                        Cliente c = servicoCadastro.cadastarCliente(nomeCliente, telefone);
                    
                        System.out.println("Cliente cadastrado com sucesso!\n" + "Nome: " + c.getNome() + " || Telefone: " + c.getTelefone());
                        System.out.println(c.getId());
                    } catch (IllegalArgumentException err) {
                        // Mostrar a exception que o ServicoCadastro nos enviou
                        System.out.println("ERRO, seu cliente não foi cadastrado : " + err.getMessage());
                    };

                    break;
                

                // VISUALIZAR CLIENTES
                case 4:
                    System.out.println("\n .....Clientes Cadastrados.....");
                    for (Cliente cliente : centralDados.getClientes())
                        System.out.println(" \nNome: " + cliente.getNome() + " telefone: " + cliente.getTelefone());
                    break;
        

                // REGISTRAR NOVO PEDIDO
                case 5:
                    try {
                        // Primero, associa-se um cliente ao pedido..
                        System.out.println("Selecione o cliente");
                        Cliente c = selecionarCliente(scanner);

                        // Depois, adicionam-se os itens do cardápio um a um, cada um com
                        // sua respectiva quantidade
                        List<ItemPedido> itensPedidos = new ArrayList<>();
                        while (true) {
                            ItemPedido i = selecionarItem(scanner);

                            boolean jaExiste = false;

                            for (ItemPedido ip : itensPedidos) {
                                // Compara para ver se está pedindo o mesmo ItemCardapio
                                if (ip.getItemCardapio().equals(i.getItemCardapio())) { 
                                    jaExiste = true;
                                    break;
                                };
                            };

                            if (jaExiste) {
                                System.out.println("Item já pedido, tente outro!");
                            } else {
                                itensPedidos.add(i);
                            };

                            System.out.println("Deseja adicionar outro item? (s/n)");
                            String resposta = scanner.nextLine().trim().toLowerCase();
                            if (!resposta.equals("s")) {
                                break;
                            };
                        };

                        // Confirmar e criar pedido
                        Pedido p = servicoPedido.criarPedido(c, itensPedidos);

                        System.out.println("Pedido registrado no sistema com sucesso. Status = " + p.getStatus());


                    } catch (NoSuchElementException err) {
                        System.out.println("ERRO : " + err);
                    };
                    

                    break;

                // ATUALIZAR PEDIDO
                case 6:
                    try {
                        System.out.println("Por favor selecione qual pedido deseja atualizar:");
                        Pedido p = selecionarPedido(scanner);
                        System.out.println(
                            "Tem certeza que deseja atualizar o Status de " 
                            + p.getStatus() + " para " + p.getStatus().proximo()
                            + "? (s/n)"
                        );
            
                        String resposta = scanner.nextLine().trim().toLowerCase();
                        if (resposta.equals("s")) {
                            Pedido.Status novoStatus = p.avancarStatus();
                            if (novoStatus != null) {
                                System.out.println("Status atualizado com sucesso! Novo status: " + novoStatus);
                            } else {
                                System.out.println("O pedido já está no status final e não pode ser atualizado.");
                            }
                        } else {
                            System.out.println("Atualização de status cancelada.");
                        }

                    } catch (NoSuchElementException err) {
                        System.out.println("ERRO : " + err);
                    };

                    break;

                // CONSULTAR PEDIDOS POR STATUS
                case 7:
                    List<Pedido> filtrados = selecionarStatus(scanner);
                    for (Pedido p : filtrados) {
                        System.out.println(
                                " || Cliente: " + p.getCliente().getNome() +
                                " || Status: " + p.getStatus() +
                                " || Data: " + p.getData()
                        );
                        System.out.println("Itens do Pedido:");
                        for (ItemPedido ip : p.getItensPedidos()) {
                            System.out.println("- " + ip.getQuantidade() + " x " + ip.getItemCardapio().getNome() + " (R$" + ip.getItemCardapio().getPreco() + ")");
                        };
                    };

                    break;

                // GERAR RELATÓRIO DE VENDAS (SIMPLES E DETALHADO)
                case 8:
                    if (centralDados.getPedidos().isEmpty()) {
                        System.out.println("Não há pedidos registrados para gerar o relatório.");
                        break;
                    };

                    int tipo;
                    while (true) {
                        System.out.println("Gerar relatório (1 - Simples, 2 - Detalhado):");
                        try {
                            tipo = Integer.parseInt(scanner.nextLine());
                            if (tipo == 1 || tipo == 2) break;
                        } catch (NumberFormatException e) {
                            // ignora, cai no loop de novo
                        };
                        System.out.println("Opção inválida. Tente novamente.");
                    };

                    if (tipo == 1) {
                        gerarRelatorioSimples(centralDados.getPedidos());
                    } else {
                        gerarRelatorioDetalhado(centralDados.getPedidos());
                    };
                    break;

                default:
                    if (opcaoSelecionada != 9) {
                        System.out.println("Opção inválida, tente novamente!");
                    };
                    break;
                };

        } while (opcaoSelecionada != 9);

        scanner.close();
    };

    private static void exibirMenuPrincipal() {
        System.out.println("\nSelecione uma das opções: ");            

        System.out.println("\n1- Cadastrar Novo Item no Cardápio");
        System.out.println("2- Exibir Todos Itens do Cardápio");
        
        System.out.println("\n3- Cadastrar Novo Cliente");
        System.out.println("4- Exibir Todos os Clientes Cadastrados");

        System.out.println("\n5- Registrar Novo Pedido");
        System.out.println("6- Atualizar Pedido");
        System.out.println("7- Consultar Pedidos por Status");
    
        System.out.println("\n8- Gerar Relatório de Vendas");

        System.out.println("\n9- Sair do Sistema");
    };

    private static Cliente selecionarCliente(Scanner scanner) {
        // Pede para o usuário selecionar um dos clientes registrados e retorna o cliente
        List<Cliente> clientes = CentralDados.getInstancia().getClientes();
    
        if (clientes.isEmpty()) {
            throw new NoSuchElementException("Não há clientes registrados no sistema.");
        };     

        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            System.out.println((i + 1) + " || " + c.getNome() + " || Telefone : " + c.getTelefone());
        };

        System.out.println("Escolha o número do cliente :");
        int escolha = Integer.parseInt(scanner.nextLine());

        if (escolha < 1 || escolha > clientes.size()) {
            System.out.println("Escolha inválida. Tente novamente.");
            return selecionarCliente(scanner); // recursivo até escolher válido
        };

    return clientes.get(escolha - 1);
    };

    private static ItemPedido selecionarItem(Scanner scanner) {
        // Pede para o usuário selecionar um item e quantidade, e retorna uma instância de ItemPedido
        List<ItemCardapio> cardapio = CentralDados.getInstancia().getCardapio();
    
        if (cardapio.isEmpty()) {
            throw new NoSuchElementException("Não há itens registrados no sistema.");
        };

        for (int i = 0; i < cardapio.size(); i++) {
            ItemCardapio item = cardapio.get(i);
            System.out.println((i + 1) + " - " + item.getNome() + " || R$" + item.getPreco());
        };

        System.out.println("Escolha o número do item:");
        int escolha = Integer.parseInt(scanner.nextLine());

        if (escolha < 1 || escolha > cardapio.size()) {
            System.out.println("Escolha inválida. Tente novamente.");
            return selecionarItem(scanner); // recursivo até escolher válido
        };

        ItemCardapio itemEscolhido = cardapio.get(escolha - 1);
        System.out.println("Digite a quantidade:");
        int quantidade = Integer.parseInt(scanner.nextLine());

        if (quantidade <= 0) {
            System.out.println("Quantidade inválida. Tente novamente.");
            return selecionarItem(scanner);
        };

        return new ItemPedido(itemEscolhido, quantidade);
    };

    private static Pedido selecionarPedido(Scanner scanner) {
        // Pede para o usuário selecionar um pedido da lista e retorna ele.
        List<Pedido> pedidos = CentralDados.getInstancia().getPedidos();

        if (pedidos.isEmpty()) {
            throw new NoSuchElementException("Não há pedidos registrados no sistema.");
        };

        List<Pedido> filtrados = selecionarStatus(scanner);

        if (filtrados.isEmpty()) {
            throw new NoSuchElementException("Não há pedidos com esse critério.");
        };

        Pedido pedidoEscolhido = null;
        while (pedidoEscolhido == null) {
            for (int i = 0; i < filtrados.size(); i++) {
                Pedido p = filtrados.get(i);
                System.out.println(
                        (i + 1) + " - Pedido " +
                        " | Cliente: " + p.getCliente().getNome() +
                        " | Status: " + p.getStatus());
            };

            System.out.println("Escolha o número do pedido:");
            try {
                int escolhaPedido = Integer.parseInt(scanner.nextLine());
                if (escolhaPedido >= 1 && escolhaPedido <= filtrados.size()) {
                    pedidoEscolhido = filtrados.get(escolhaPedido - 1);
                } else {
                    System.out.println("Opção inválida, tente novamente.");
                };
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, digite um número.");
            };
        };

        return pedidoEscolhido;
    };

    private static List<Pedido> selecionarStatus(Scanner scanner) {
        // Pede para o usuário selecionar um status, e retorna uma lista de pedidos com esse status
        List<Pedido> pedidos = CentralDados.getInstancia().getPedidos();
        if (pedidos.size() == 1) {
            return pedidos; // só tem um, não faz sentido filtrar
        };

        
        System.out.println("Deseja filtrar por status específico? (s/n)");
        String resposta = scanner.nextLine().trim().toLowerCase();

        List<Pedido> filtrados = new ArrayList<>();
        if (resposta.equals("s")) {
            Pedido.Status[] statuses = Pedido.Status.values();

            Pedido.Status statusEscolhido = null;
            while (statusEscolhido == null) {
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + " - " + statuses[i]);
                };

                System.out.println("Escolha o número do status:");

                try {
                    int escolha = Integer.parseInt(scanner.nextLine());
                    if (escolha >= 1 && escolha <= statuses.length) {
                        statusEscolhido = statuses[escolha - 1];
                    } else {
                        System.out.println("Opção inválida, tente novamente!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida, digite um número!");
                };
            };

            for (Pedido p : pedidos) {
                if (p.getStatus() == statusEscolhido) {
                    filtrados.add(p);
                };
            };
        } else {
            filtrados = pedidos; // sem filtro
        };
        return filtrados;
    };

    private static void gerarRelatorioSimples(List<Pedido> pedidos) {
        System.out.println("\n..... Relatório Simples de Vendas .....\n");

        int totalPedidos = pedidos.size();
        double valorTotal = 0;

        for (Pedido p : pedidos) {
            for (ItemPedido item : p.getItensPedidos()) {
                valorTotal += item.getItemCardapio().getPreco() * item.getQuantidade();
            };
        };

        System.out.println("Total de pedidos: " + totalPedidos);
        System.out.println("Valor total arrecadado: R$ " + valorTotal);
    };
    private static void gerarRelatorioDetalhado(List<Pedido> pedidos) {
        System.out.println("\n..... Relatório Detalhado de Vendas .....\n");
        for (Pedido p : pedidos) {
            System.out.println("Cliente: " + p.getCliente().getNome());
            System.out.println("Data: " + p.getData());
            System.out.println("Status: " + p.getStatus());

            double total = 0;
            for (ItemPedido item : p.getItensPedidos()) {
                double subtotal = item.getItemCardapio().getPreco() * item.getQuantidade();
                total += subtotal;
                System.out.println(" - " + item.getItemCardapio().getNome() +
                                " x" + item.getQuantidade() +
                                " = R$ " + subtotal);
            }

            System.out.println("Total: R$ " + total);
            System.out.println("-----------------------------");
        };
    };
};

