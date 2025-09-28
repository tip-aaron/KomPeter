SELECT 
    i.item_name,
    SUM(s.item_sold_quantity) AS total_sold
FROM items_sold s
INNER JOIN items i 
    ON s.item_id = i.item_id
GROUP BY i.item_id, i.item_name
ORDER BY total_sold DESC;