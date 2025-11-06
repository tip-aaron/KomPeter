SELECT EXISTS (
        SELECT 1
        FROM sessions
        WHERE session_token = ?
    );