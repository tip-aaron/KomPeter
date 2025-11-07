SELECT s._sale_id,
    s.vat_rate,
    s.sale_code,
    s.sale_date,
    c._customer_id,
    c.name AS customer_name,
    json_group_array (
        json_object (
            '_productId',
            sl._product_id,
            '_saleId',
            sl._sale_id,
            'quantity',
            sl.quantity,
            'netPrice',
            sl.net_price
        )
    ) AS sale_lines,
    json_group_array (
        json_object (
            '_saleDiscountId',
            sd._sale_discount_id,
            '_saleId',
            sd._sale_id,
            'amount',
            sd.amount,
            'discountType',
            sd.discount_type
        )
    ) AS sale_discounts
FROM sales s
    INNER JOIN sale_lines sl ON s._sale_id = sl._sale_id
    INNER JOIN sale_discounts sd ON s._sale_id = sd._sale_id
    INNER JOIN customers c ON c._customer_id = s._customer_id
GROUP BY s._sale_id,
    c._customer_id,
    s.vat_rate,
    s.sale_code,
    s.sale_date,
    c.name;