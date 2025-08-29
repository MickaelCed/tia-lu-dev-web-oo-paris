public class ServicoCadastro {
    // Classe que cuida da lógica do cadastro de clientes e itens  
    private static CentralDados centralDados = CentralDados.getInstancia();

    public Cliente cadastarCliente(String nome, String telefone){
        // Verificações de item válido
        if (nome == null || nome.isBlank()) {  // Nome inválido
            throw new IllegalArgumentException("Nome vazio ou inválido!");
        };
        if (telefone == null || telefone.isBlank()) {  // Telefone inválido
            throw new IllegalArgumentException("Telefone vazio ou inválido!");
        };
        if (centralDados.buscarClientePorTelefone(telefone) != null){  // Telefone não único
            throw new IllegalArgumentException("Já possui um cliente cadastrado com esse telefone!");
        };
        
        Cliente cliente = new Cliente(nome.strip().toUpperCase(), telefone);
        centralDados.adicionarCliente(cliente);  

        return cliente;
    }
    public ItemCardapio cadastrarItemCardapio(String nome, double preco){
        // Verificações de item válido
        if (nome == null || nome.isBlank()) {  // Nome inválido
            throw new IllegalArgumentException("Nome vazio ou inválido!");
        };
        if (preco <= 0) { // Preço inválido
            throw new IllegalArgumentException("Valor de preço inválido!");
        };
        if (centralDados.buscarItemPorNome(nome.strip().toUpperCase()) != null){  // Nome não único
            throw new IllegalArgumentException("Já possui um item cadastrado com esse nome!");
        };
        
        ItemCardapio itemCardapio = new ItemCardapio(nome.strip().toUpperCase(), preco);
        centralDados.adicionarItemCardapio(itemCardapio);
        
        return itemCardapio;
    }
}
