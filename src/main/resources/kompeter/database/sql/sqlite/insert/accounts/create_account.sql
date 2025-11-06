INSERT INTO accounts (
        _user_id,
        password_hash,
        password_salt,
        email
    )
VALUES (
        :_user_id,
        :password_hash,
        :password_salt,
        :email
    );