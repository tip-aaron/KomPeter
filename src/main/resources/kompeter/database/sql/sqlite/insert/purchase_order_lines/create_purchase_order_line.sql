INSERT INTO purchase_order_lines (
        _product_id,
        _purchase_order_id,
        quantity,
        unit_price
    )
VALUES (
        :_product_id,
        :_purchase_order_id,
        :quantity,
        :unit_price
    );