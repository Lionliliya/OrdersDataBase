/**
 * Created by lionliliya on 12.09.15.
 */
public class Order {
    private int id;
    private int ProductId;
    private int ClientId;
    private String Info;

    public Order(int id, int productId, int clientId, String info) {
        this.id = id;
        ProductId = productId;
        ClientId = clientId;
        Info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }
}
