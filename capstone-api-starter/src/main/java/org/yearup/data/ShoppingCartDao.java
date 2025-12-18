package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addToCart(int userId, int productId);
    void clearCart(int userId);
    void deleteFromCart(int productId);
    // add additional method signatures here
}
