UPDATE products
SET quantity_in_hand = quantity_in_hand + :quantity_to_add
WHERE _product_id = :_product_id;