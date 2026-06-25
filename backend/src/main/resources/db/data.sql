INSERT INTO usuarios (username, password, nombre, apellido, rol, activo)
VALUES (
    'admin',
    '$2a$12$sbGqHaVa7xSW3C/SNn6Mdu/RmwfpU8lSjoKz/5Zd/29HFYxpAyi86',
    'Administrador',
    'Taller',
    'ADMIN',
    true
)
ON CONFLICT (username) DO NOTHING;