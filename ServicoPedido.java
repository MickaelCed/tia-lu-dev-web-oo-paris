import java.util.List;

public class ServicoPedido {
    // Classe que cuida de toda a lógica dos pedidos requisitados
    private static CentralDados centralDados = CentralDados.getInstancia();

    public Pedido criarPedido(Cliente cliente, List<ItemPedido> itensPedido) {
        if (itensPedido == null || itensPedido.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item.");
        };
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente vazio.");

        };

        Pedido pedido = new Pedido(cliente, itensPedido);
        centralDados.adicionarPedido(pedido);
        return pedido;
        
    };
}
