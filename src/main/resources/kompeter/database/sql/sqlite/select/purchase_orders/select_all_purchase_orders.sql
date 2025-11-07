SELECT po._purchase_order_id,
    po.vat_rate,
    po.purchase_date,
    po.purchase_code,
    s._supplier_id,
    s.name AS supplier_name,
    json_group_array (
        json_object (
            '_purchaseOrderId',
            po._purchase_order_id,
            '_productId',
            pol._product_id,
            'quantity',
            pol.quantity,
            'unitPrice',
            pol.unit_price
        )
    ) AS purchase_order_lines
FROM purchase_orders po
    INNER JOIN purchase_order_lines pol po._purchase_order_id = pol._purchase_order_id
    INNER JOIN suppliers s s._supplier_id = po._supplier_id
GROUP BY po._purchase_code_i,
    po._supplier_id,
    po.vat_rate,
    po.purchase_code,
    po.purchase_date,
    s.name;