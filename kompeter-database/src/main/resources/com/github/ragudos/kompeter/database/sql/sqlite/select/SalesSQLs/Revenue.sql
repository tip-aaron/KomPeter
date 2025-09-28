WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?) -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?) -- end date
  )
  SELECT 
      c.date,
      COALESCE(SUM(i.item_selling_price * s.item_sold_quantity), 0) AS total_revenue
  FROM calendar c
  LEFT JOIN items_sold s 
         ON DATE(s.item_sold_datetime) = c.date
  LEFT JOIN items i 
         ON i.item_id = s.item_id
  GROUP BY c.date
  ORDER BY c.date;