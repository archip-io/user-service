CREATE TABLE IF NOT EXISTS roles_authorities
(
    role_id      INTEGER NOT NULL REFERENCES roles (id),
    authority_id INTEGER NOT NULL REFERENCES authorities (id),
    PRIMARY KEY (role_id, authority_id)
);

COMMENT ON TABLE roles_authorities IS 'Полномочия ролей';

COMMENT ON COLUMN roles_authorities.role_id IS 'ID роли';
COMMENT ON COLUMN roles_authorities.authority_id IS 'ID полномочия';