WITH RECURSIVE calendar(date) AS (
    SELECT DATE(?)              -- start date
    UNION ALL
    SELECT DATE(date, '+1 day')
    FROM calendar
    WHERE date < DATE(?)        -- end date
  ),
  daily_purchases AS (
    SELECT 
        DATE(item_purchase_datetime) AS purchase_date,
        SUM(item_purchase_quantity) AS purchased_qty
    FROM items_purchase
    GROUP BY DATE(item_purchase_datetime)
  ),
  daily_sales AS (
    SELECT 
        DATE(item_sold_datetime) AS sold_date,
        SUM(item_sold_quantity) AS sold_qty
    FROM items_sold
    GROUP BY DATE(item_sold_datetime)
  ),
  daily_net AS (
    SELECT 
        c.date,
        COALESCE(dp.purchased_qty, 0) AS total_purchased,
        COALESCE(ds.sold_qty, 0) AS total_sold
    FROM calendar c
    LEFT JOIN daily_purchases dp ON dp.purchase_date = c.date
    LEFT JOIN daily_sales ds     ON ds.sold_date = c.date
  ),
  running_inventory AS (
    SELECT 
        dn.date,
        dn.total_purchased,
        dn.total_sold,
        SUM(dn.total_purchased - dn.total_sold) 
           OVER (ORDER BY dn.date) AS total_inventory
    FROM daily_net dn
  )
  SELECT 
      date,
      total_inventory,
      total_purchased,
      total_sold
  FROM running_inventory
  ORDER BY date;