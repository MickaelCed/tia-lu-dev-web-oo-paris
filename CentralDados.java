import java.util.ArrayList;
import java.util.List;

public class CentralDados {
    private static CentralDados instancia = new CentralDados();
    private static CentralDados novaInstancia = new CentralDados();
    private List<Cliente> clientes;
    private List<ItemCardapio> cardapio;
    private List<Pedido> pedidos;

    private CentralDados() {  // Singleton
        // Construtor privado para evitar instanciação externa
        clientes = new ArrayList<>();
        cardapio = new ArrayList<>();  
        pedidos = new ArrayList<>();
    };
    // Para pegar a instância única..
    public static CentralDados getInstancia()  {
        return instancia;
    };


    public List<Cliente> getClientes() {
        return clientes;
    };
    public List<ItemCardapio> getCardapio() {
        return cardapio;
    };
    public List<Pedido> getPedidos() {
        return pedidos;
    };

    public Cliente buscarClientePorNome(String nome) {
        for (Cliente c : clientes) {
            if (c.getNome().equals(nome)){
                return c;
            };
        };
        return null;
    };
    public Cliente buscarClientePorTelefone(String telefone) {
        for (Cliente c : clientes) {
            if (c.getTelefone().equals(telefone)){
                return c;
            };
        };
        return null;
    };
    public ItemCardapio buscarItemPorNome(String nome) {
        for (ItemCardapio i : cardapio) {
            if (i.getNome().equals(nome)) {
                return i;
            };
        };
        return null;
    };

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    };
    public void adicionarItemCardapio(ItemCardapio itemCardapio) {
        cardapio.add(itemCardapio);
    };
    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    };
};