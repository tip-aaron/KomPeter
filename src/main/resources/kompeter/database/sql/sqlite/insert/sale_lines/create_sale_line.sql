INSERT INTO sale_lines (
        _sale_id,
        _product_id,
        net_price,
        quantity
    )
VALUES (
        :_sale_id,
        :_product_id,
        :net_price,
        :quantity
    );