WITH RECURSIVE calendar(date) AS (
                       SELECT DATE(?)              -- start date
                       UNION ALL
                       SELECT DATE(date, '+1 day')
                       FROM calendar
                       WHERE date < DATE(?)        -- end date
                     ),
                     daily_purchases AS (
                       SELECT 
                           DATE(p.item_purchase_datetime) AS purchase_date,
                           SUM(p.item_purchase_quantity * i.item_selling_price) AS purchased_value
                       FROM items_purchase p
                       JOIN items i ON i.item_id = p.item_id
                       GROUP BY DATE(p.item_purchase_datetime)
                     ),
                     daily_sales AS (
                       SELECT 
                           DATE(s.item_sold_datetime) AS sold_date,
                           SUM(s.item_sold_quantity * i.item_selling_price) AS sold_value
                       FROM items_sold s
                       JOIN items i ON i.item_id = s.item_id
                       GROUP BY DATE(s.item_sold_datetime)
                     ),
                     daily_net AS (
                       SELECT 
                           c.date,
                           COALESCE(dp.purchased_value, 0) AS total_purchased_value,
                           COALESCE(ds.sold_value, 0) AS total_sold_value
                       FROM calendar c
                       LEFT JOIN daily_purchases dp ON dp.purchase_date = c.date
                       LEFT JOIN daily_sales ds     ON ds.sold_date = c.date
                     ),
                     running_inventory AS (
                       SELECT 
                           dn.date,
                           dn.total_purchased_value,
                           dn.total_sold_value,
                           SUM(dn.total_purchased_value - dn.total_sold_value) 
                              OVER (ORDER BY dn.date) AS total_inventory_value
                       FROM daily_net dn
                     )
                     SELECT 
                         date,
                         total_inventory_value,
                         total_purchased_value,
                         total_sold_value
                     FROM running_inventory
                     ORDER BY date;