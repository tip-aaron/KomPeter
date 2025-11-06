SELECT p._product_id,
    p._name,
    p.description,
    p.display_iamge,
    p.markup_rate,
    p.net_price,
    p.average_cost,
    p.average_cost_vat_rate,
    p.quantity_in_hand,
    p.is_active,
    p.is_deleted,
    pc._product_category_id,
    pc.name AS category_name,
    pb._product_brand_id,
    pb.name AS brand_name
FROM products p
    INNER JOIN product_categories pc ON pc._product_category_id = p._product_category_id
    INNER JOIN product_brands pb ON pb._product_brand_id = p._product_brand_id
GROUP BY p._product_id,
    pc._product_category_id,
    pb._product_brand_id;