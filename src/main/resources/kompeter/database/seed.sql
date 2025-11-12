INSERT INTO roles (name, description)
VALUES (
        'admin',
        'Full system access and management privileges.'
    ),
    (
        'manager',
        'Manages staff, inventory, and purchases.'
    ),
    (
        'cashier',
        'Processes point-of-sale transactions.'
    ),
    (
        'inventory clerk',
        'Manages and updates inventory records.'
    ),
    (
        'auditor',
        'Performs financial and stock audits.'
    ),
    (
        'supplier',
        'External role for supplier data, not a direct system user.'
    );
INSERT INTO users (
        display_name,
        first_name,
        last_name,
        display_image
    )
VALUES (
        'Peter',
        'Peter',
        'Parker',
        '/kompeter/ui/assets/images/peter.png'
    ),
    (
        'Hanz',
        'Hanz',
        'Zimmer',
        '/kompeter/ui/assets/images/peter.png'
    ),
    (
        'Jerick',
        'Jerick',
        'Serrano',
        '/kompeter/ui/assets/images/peter.png'
    ),
    (
        'Aaron',
        'Aaron',
        'Cruz',
        '/kompeter/ui/assets/images/peter.png'
    ),
    (
        'Kurt',
        'Kurt',
        'Cobain',
        '/kompeter/ui/assets/images/peter.png'
    );
INSERT INTO accounts (_user_id, password_hash, password_salt, email)
VALUES (
        1,
        'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=',
        'LGwkiq+nsgf+R7iRlpS3kQ==',
        'peter.admin@example.com'
    ),
    (
        2,
        'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=',
        'LGwkiq+nsgf+R7iRlpS3kQ==',
        'hanz.manager@example.com'
    ),
    (
        3,
        'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=',
        'LGwkiq+nsgf+R7iRlpS3kQ==',
        'jerick.cashier@example.com'
    ),
    (
        4,
        'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=',
        'LGwkiq+nsgf+R7iRlpS3kQ==',
        'aaron.clerk@example.com'
    ),
    (
        5,
        'kHsoVp4WrbrC/mg/a7cqhGKb9u2VBNOE/VLmuIuYFe8=',
        'LGwkiq+nsgf+R7iRlpS3kQ==',
        'kurt.auditor@example.com'
    );
INSERT INTO user_roles (_user_id, _role_id)
VALUES (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5);
INSERT INTO product_categories (name)
VALUES ('PC Components'),
    ('Peripherals'),
    ('Laptops'),
    ('Accessories');
INSERT INTO product_brands (name)
VALUES ('Logitech'),
    ('Kingston'),
    ('AMD'),
    ('ASUS'),
    ('Samsung'),
    ('Intel'),
    ('NVIDIA'),
    ('Cooler Master'),
    ('HyperX'),
    ('Dell');
INSERT INTO db_settings (is_seeded)
VALUES (true);
