DELETE FROM user_roles
WHERE _user_id = :_user_id
    AND _role_id = :_role_id;