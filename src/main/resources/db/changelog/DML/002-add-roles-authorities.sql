WITH user_authorities AS (SELECT *
                          FROM (VALUES ('UPDATE_USERNAME'),
                                       ('UPDATE_EMAIL'),
                                       ('UPDATE_PASSWORD'),
                                       ('DELETE_ACCOUNT'),
                                       ('CREATE_PROJECT'),
                                       ('UPDATE_PROJECT'),
                                       ('DELETE_PROJECT'),
                                       ('ADD_FAVORITE_PROJECT'),
                                       ('DELETE_FAVORITE_PROJECT')) AS user_authorities(name)),
     admin_authorities AS (SELECT *
                           FROM (VALUES ('BAN_USER'),
                                        ('UNBAN_USER'),
                                        ('DELETE_USER'),
                                        ('DELETE_PROJECT')) AS admin_authorities(name)),
     user_authority_ids AS (SELECT a.id AS user_authority_id
                            FROM authorities a
                                     JOIN user_authorities ua ON a.name = ua.name),
     admin_authority_ids AS (SELECT a.id AS admin_authority_id
                             FROM authorities a
                                      JOIN admin_authorities aa ON a.name = aa.name),
     user_role_id AS (SELECT id FROM roles r WHERE r.name = 'USER'),
     admin_role_id AS (SELECT id FROM roles r WHERE r.name = 'ADMIN')
INSERT
INTO roles_authorities (role_id, authority_id)
SELECT (SELECT * FROM user_role_id), user_authority_id
FROM user_authority_ids
UNION ALL
SELECT (SELECT * FROM admin_role_id), user_authority_id
FROM user_authority_ids
UNION ALL
SELECT (SELECT * FROM admin_role_id), admin_authority_id
FROM admin_authority_ids
ON CONFLICT DO NOTHING