SELECT s_session_id,
    s._created_at,
    s.expires_at,
    s.session_token,
    u._user_id,
    u.display_name,
    u.first_name,
    u.last_name,
    (u.first_name || ' ' || u.last_name) AS full_name,
    u.display_image,
    GROUP_CONCAT(r.name, ',') AS roles,
    a.email
FROM sessions s
    INNER JOIN users u ON u._user_id = s._user_id
    INNER JOIN accounts a ON a._user_id = s._user_id
    INNER JOIN user_roles ur ON ur._user_id = s._user_id
    INNER JOIN roles r ON r._role_id = ur._role_id
WHERE s.session_token = ?
GROUP BY s._session_id,
    s.session_token,
    u.display_name,
    u.first_name,
    u.last_name,
    u.display_image,
    a.email;