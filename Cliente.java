import java.util.UUID;

public class Cliente {
    private String id;
    private String nome;
    private String telefone; 

    public Cliente(String nome, String telefone){
        this.nome = nome;
        this.telefone = telefone;
        this.id = UUID.randomUUID().toString();  // Convertido para String para uso futuro no banco de dados
    };


    public String getId() {
        return id;
    };
    public String getNome() {
        return nome;
    };
    public String getTelefone() {
        return telefone;
    };

    public void setNome(String nome) {
        this.nome = nome;
    };
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    };

     @Override
    public String toString() {
        return "Cliente:" + id + " - " + nome + " (" + telefone + ")";
}    
};