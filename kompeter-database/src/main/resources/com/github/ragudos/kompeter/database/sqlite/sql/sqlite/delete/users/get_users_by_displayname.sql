SELECT *
FROM accounts
FULL JOIN users on accounts._user_id=users._user_id
WHERE users.display_name = ?;