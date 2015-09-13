
import java.sql.*;

/**
 * Created by lionliliya on 12.09.15.
 */
public class Main {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/Ordersdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static final String CREATE_TABLE_Client = "CREATE TABLE IF NOT EXISTS Clients (" +
            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "Namee VARCHAR(25) NOT NULL, " +
            "phone VARCHAR(11))";

    private static final String CREATE_TABLE_Products = "CREATE TABLE IF NOT EXISTS Products (" +
            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "Namee VARCHAR(25) NOT NULL, " +
            "price INT)";

    private static final String CREATE_TABLE_Orders = "CREATE TABLE IF NOT EXISTS Orders (" +
            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
            "ProductId INT REFERENCES Products, " +
            "Quantity INT NOT NULL, " +
            "ClientId INT REFERENCES Clients, " +
            "Info VARCHAR(100))";

    private static Connection getDBConnection() {
        Connection dbConnection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return dbConnection;
    }

    // insert new client into table Clients
    public static void addClient(Client a, Connection connection) {
        int id = a.getId();
        String name = a.getName();
        String phone = a.getPhone();
        try {
            PreparedStatement prstatement = connection.prepareStatement("INSERT INTO Clients (id, Namee, phone) VALUES (?, ?, ?)");
            try {
                prstatement.setInt(1, id);
                prstatement.setString(2, name);
                prstatement.setString(3, phone);

                prstatement.executeUpdate();
                System.out.println("Client added successfully");

            } finally {
                if (prstatement != null) prstatement.close();

            }
        } catch (SQLException ex) {
            System.out.println("Can't add client to the table. See logs");
            System.out.println(ex);
        }
    }

    // insert new product into table Products
    public static void addProduct(Product a, Connection connection) {
        int id = a.getId();
        String name = a.getName();
        int price = a.getPrice();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO Products (id, Namee, price) VALUES (?, ?, ?)");
            try {
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, price);

                preparedStatement.executeUpdate();
                System.out.println("Product added successfully");
            } finally {
                if (preparedStatement != null) preparedStatement.close();
            }

        } catch (SQLException ex) {
            System.out.println("Can't add product to the table. See logs");
            ex.printStackTrace();
        }

    }

    // find product id by name
    public static int getProductId(String productName, Connection connection) {
        int id = 0;
        try {
            Statement statement = connection.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery("SELECT id FROM Products WHERE Namee = '" + productName + "'");
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                } else {
                    System.out.println("Product not fount");
                }
            } finally {
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            System.out.println("Wrong product name!");
            e.printStackTrace();

        }

        return id;
    }

    // find client id by phone
    public static int getClientId(String clientphone, Connection connection) {
        int id = 0;
        try {
            Statement statement = connection.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery("SELECT id FROM Clients WHERE phone = '" + clientphone + "'");
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                } else {
                    System.out.println("Phone not found");
                }
            } finally {
                if (statement != null) statement.close();
            }
        } catch (SQLException e) {
            System.out.println("Wrong client's phone!");
            e.printStackTrace();
        }

        return id;
    }

    //insert data to create a new order
    public static void makeOrder(String ProductName, int quantity, String ClientPhone, String Info, Connection connection) {
        int prId = getProductId(ProductName, connection);
        int clId = getClientId(ClientPhone, connection);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Orders (ProductId, Quantity, ClientId, Info) VALUES (?, ?, ?, ?)");
            try {
                preparedStatement.setInt(1, prId);
                preparedStatement.setInt(2, quantity);
                preparedStatement.setInt(3, clId);
                preparedStatement.setString(4, Info);
                preparedStatement.executeUpdate();
                System.out.println("Order is made successfully.");
            } finally {
                if (preparedStatement != null) preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**show all rows and column in a table
     * FOR EXAMPLE, String query = "SELECT * FROM Clients"**/
    public static void SelectAll(String query, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                try {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        System.out.print(metaData.getColumnName(i) + "\t\t");
                    }
                    System.out.println();
                    while (resultSet.next()) {
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            System.out.print(resultSet.getString(i) + "\t\t");
                        }
                        System.out.println();
                    }

                } finally {
                    resultSet.close();
                }
            } finally {
                if (preparedStatement != null) preparedStatement.close();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Connection connection = getDBConnection();
        if (connection == null) System.out.println("Connection is not found");

        try {
            // create 3 tables
            try {
                Statement statement = connection.createStatement();
                try {
                    statement.execute(CREATE_TABLE_Client);
                    statement.execute(CREATE_TABLE_Products);
                    statement.execute(CREATE_TABLE_Orders);

                } finally {
                    if (statement != null) statement.close();
                }

                // fill in table Clients
                addClient(new Client(1, "Silpo", "0444252525"), connection);
                addClient(new Client(2, "Billa", "04445880000"), connection);
                addClient(new Client(3, "Eco Market", "044525666"), connection);
                addClient(new Client(4, "GoodWine", "0442523030"), connection);

                //fill in table Products
                addProduct(new Product(1, "Frontera White Wine", 89), connection);
                addProduct(new Product(2, "Frontera Red Wine", 87), connection);
                addProduct(new Product(3, "Villa Chardonnay", 110), connection);
                addProduct(new Product(4, "Villa Cabernet", 105), connection);
                addProduct(new Product(5, "Morshynska 0.7", 7), connection);
                addProduct(new Product(6, "Morshynska 0.5", 3), connection);
                addProduct(new Product(7, "Evian 0.3", 25), connection);
                addProduct(new Product(8, "Sandora Orange 1.0", 12), connection);
                addProduct(new Product(9, "Sandora Tomato 1.0", 12), connection);

                //make order and fill table Orders
                makeOrder("Evian 0.3", 12, "0444252525", "New order", connection);
                makeOrder("Frontera White Wine", 10, "0444252525", "New order", connection);
                makeOrder("Villa Cabernet", 10, "0444252525", "New order", connection);
                makeOrder("Sandora Tomato 1.0", 20, "04445880000", "New order", connection);
                makeOrder("Sandora Orange 1.0", 25, "044525666", "New order", connection);

                System.out.println("========================================");
                //SELECT * FROM Clients;
                SelectAll("SELECT * FROM Clients", connection);
                System.out.println("========================================");
                System.out.println();
                System.out.println("========================================");
                //SELECT * FROM Products;
                SelectAll("SELECT * FROM Products", connection);
                System.out.println("========================================");
                System.out.println();
                System.out.println("========================================");
                //SELECT * FROM Orders;
                SelectAll("SELECT * FROM Orders", connection);
                System.out.println("========================================");

            } finally {
                connection.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
