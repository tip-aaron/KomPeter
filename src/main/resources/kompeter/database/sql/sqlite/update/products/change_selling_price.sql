UPDATE products
SET net_price = :net_price,
    average_cost = :average_cost,
    average_cost_vat_rate = :average_cost_vat_rate
WHERE _product_id = :_product_id;