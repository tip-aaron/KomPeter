WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?) -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?) -- end date
 ),
 TOTAL_REVENUE AS (
 SELECT
    c.date,
        COALESCE(SUM(i.item_selling_price * s.item_sold_quantity), 0) AS total_revenue
    FROM calendar c
    LEFT JOIN items_sold s 
        ON DATE(s.item_sold_datetime) = c.date
    LEFT JOIN items i 
        ON i.item_id = s.item_id
    GROUP BY c.date
 ),
 TOTAL_EXPENSES AS (
 SELECT
    c.date,
        COALESCE(SUM(i.item_purchasing_price * p.item_purchase_quantity), 0) AS total_expense
    FROM calendar c
    LEFT JOIN items_purchase p 
           ON DATE(p.item_purchase_datetime) = c.date
    LEFT JOIN items i 
           ON i.item_id = p.item_id
    GROUP BY c.date
 )
 SELECT
    c.date,
    COALESCE(tr.total_revenue, 0) - COALESCE(te.total_expense, 0) as total_profit
 FROM calendar c
 LEFT JOIN TOTAL_REVENUE tr 
        ON tr.date = c.date
 LEFT JOIN TOTAL_EXPENSES te 
        ON te.date = c.date
 ORDER BY c.date;