SELECT average_cost,
    average_cost_vat_rate
FROM products
WHERE _product_id = ?;