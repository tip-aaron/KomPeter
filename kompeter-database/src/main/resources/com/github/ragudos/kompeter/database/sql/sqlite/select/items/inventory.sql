/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Peter Dela Cruz
 * Created: Oct 9, 2025
 * This sql query will display items in Main dsiplay floor
 */

SELECT
    i._item_id,
    i._created_at,
    ic.name,
    i.name,
    ib.name,
    ist.unit_price_php,
    issl.quantity,
    sl.name
FROM
    items AS i
INNER JOIN item_category_assignments AS ica ON i._item_id = ica._item_id
INNER JOIN item_categories AS ic ON ica._item_category_id = ic._item_category_id
INNER JOIN item_stocks AS ist ON i._item_id = ist._item_id
LEFT JOIN item_brands AS ib ON ist._item_brand_id = ib._item_brand_id
INNER JOIN item_stock_storage_locations AS issl ON ist._item_stock_id = issl._item_stock_id
INNER JOIN storage_locations AS sl ON issl._storage_location_id = sl._storage_location_id
WHERE sl.name = 'Main Display Floor'
ORDER BY i._item_id;


