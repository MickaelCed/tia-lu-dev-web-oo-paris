public class ItemPedido {
    private ItemCardapio itemCardapio;
    private int quantidade;

    public ItemPedido(ItemCardapio itemCardapio, int quantidade) {
        this.itemCardapio = itemCardapio;
        this.quantidade = quantidade;
    };


    public ItemCardapio getItemCardapio() {
        return itemCardapio;
    };
    public int getQuantidade() {
        return quantidade;
    };
    public double getPrecoTotal() {
        return itemCardapio.getPreco() * quantidade;
    };
}
