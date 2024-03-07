CREATE TABLE IF NOT EXISTS authorities
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    create_at   TIMESTAMPTZ  NOT NULL,
    update_at   TIMESTAMPTZ  NOT NULL
);

COMMENT ON TABLE authorities IS 'Полномочия';

COMMENT ON COLUMN authorities.id IS 'ID';
COMMENT ON COLUMN authorities.name IS 'Наименование';
COMMENT ON COLUMN authorities.description IS 'Описание';
COMMENT ON COLUMN authorities.create_at IS 'Время создания';
COMMENT ON COLUMN authorities.update_at IS 'Время последнего изменения';