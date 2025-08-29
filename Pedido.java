import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Pedido {
    public enum Status {
        ACEITO,
        PREPARANDO,
        FEITO,
        AGUARDANDO_ENTREGADOR,
        SAIU_PARA_ENTREGA,
        ENTREGUE,;

        public Status proximo() {
            // Retorna o próximo status, ou null se já for o último
            int index = this.ordinal();
            Status[] valores = Status.values();
            if (index < valores.length - 1) {
              return valores[index + 1];
            } else {
                return null; // já é o último
            }
        };
    };

    private String id;
    private LocalDateTime data;
    private Cliente cliente;
    private List<ItemPedido> itensPedidos;
    private Status status;

    public Pedido(Cliente cliente, List<ItemPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
        this.cliente = cliente;

        this.id = UUID.randomUUID().toString();
        this.data = LocalDateTime.now();
        this.status = Status.ACEITO;
    };

    public Status avancarStatus() {  // Só avança para o próximo estágio, sem pular nenhum
        Status proximoStatus = this.status.proximo();
        if (proximoStatus != null) {
            this.status = proximoStatus;
        }

        return proximoStatus;
    }

    public String getId() {
        return id;
    };
    public Cliente getCliente() {
        return cliente;
    };
    public List<ItemPedido> getItensPedidos() {
        return itensPedidos;
    };
    public LocalDateTime getData() {
        return data;
    };
    public Status getStatus() {
        return status;
    };
}
