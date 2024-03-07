CREATE TABLE IF NOT EXISTS roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    create_at   TIMESTAMPTZ  NOT NULL,
    update_at   TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE roles IS 'Роли';

COMMENT ON COLUMN roles.id IS 'ID';
COMMENT ON COLUMN roles.name IS 'Наименование';
COMMENT ON COLUMN roles.description IS 'Описание';
COMMENT ON COLUMN roles.create_at IS 'Время создания';
COMMENT ON COLUMN roles.update_at IS 'Время последнего изменения';