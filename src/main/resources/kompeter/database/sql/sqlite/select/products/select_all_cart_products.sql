SELECT _product_id,
    name,
    display_image,
    net_price,
    quantity_in_hand
FROM products
WHERE is_active = TRUE AND
is_deleted = FALSE;
