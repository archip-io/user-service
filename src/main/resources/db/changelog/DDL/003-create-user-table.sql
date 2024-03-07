CREATE TABLE IF NOT EXISTS users
(
    id        UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username  VARCHAR(321) NOT NULL UNIQUE,
    email     VARCHAR(321) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    role_id   INTEGER REFERENCES roles (id),
    create_at TIMESTAMPTZ  NOT NULL,
    update_at TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE users IS 'Пользователи';

COMMENT ON COLUMN users.id IS 'ID';
COMMENT ON COLUMN users.username IS 'Имя пользователя';
COMMENT ON COLUMN users.email IS 'Email';
COMMENT ON COLUMN users.password IS 'Пароль';
COMMENT ON COLUMN users.create_at IS 'Время создания';
COMMENT ON COLUMN users.update_at IS 'Время последнего изменения';