package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements org.yearup.data.ShoppingCartDao {
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    public ShoppingCart getByUserId(int userId){
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new HashMap<>());
        String sql = """
                SELECT sc.product_id, sc.quantity,p.name,
                       p.price, p.category_id, p.description, p.subcategory,
                       p.stock, p.image_url, p.featured
                FROM shopping_cart sc
                JOIN products USING (product_id)
                WHERE sc.user_id = ?;
                """;
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(mapRow(resultSet));
                    item.setQuantity(resultSet.getInt("quantity"));
                    cart.getItems().put(item.getProductId(), item);
                }
            }

            return cart;
        }catch (SQLException e){
            throw new RuntimeException("Error on getting item from cart");
        }

    }
    public void addToCart(int userId, int productId){

    }
    public void clearCart(int userId){

    }
    public void deleteFromCart(int productId){

    }

    protected static Product mapRow(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    }
}
