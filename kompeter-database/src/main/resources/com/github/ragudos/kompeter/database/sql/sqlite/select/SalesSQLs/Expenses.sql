WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?) -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?) -- end date
  )
  SELECT 
      c.date,
      COALESCE(SUM(i.item_purchasing_price * p.item_purchase_quantity), 0) AS total_expense
  FROM calendar c
  LEFT JOIN items_purchase p 
         ON DATE(p.item_purchase_datetime) = c.date
  LEFT JOIN items i 
         ON i.item_id = p.item_id
  GROUP BY c.date
  ORDER BY c.date;