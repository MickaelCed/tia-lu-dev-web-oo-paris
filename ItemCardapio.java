import java.util.UUID;

public class ItemCardapio {
    private String id;
    private String nome;
    private double preco;

    public ItemCardapio(String nome, double preco){
        this.nome = nome;
        this.preco = preco;
        this.id = UUID.randomUUID().toString();
    };


    public String getId() {
        return id;
    };
    public String getNome() {
        return nome;
    };
    public double getPreco() {
        return preco;
    };
    
    public void setNome(String nome) {
        this.nome = nome;
    };
    public void setPreco(double preco) {
        this.preco = preco;
    };

     @Override
    public String toString() {
        return "ItemCardapio:" + id + " - " + nome + " (R$ " + String.format("%.2f", preco) + ")";
    };
};
