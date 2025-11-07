INSERT INTO purchase_orders (
        _supplier_id,
        purchase_date,
        purchase_code,
        vat_rate
    )
VALUES (
        :_supplier_id,
        :purchase_date,
        :purchase_code,
        :vat_rate
    );