WITH user_authorities AS (SELECT *
                          FROM (VALUES ('UPDATE_USERNAME', 'Изменение имени пользователя'),
                                       ('UPDATE_EMAIL', 'Изменение email'),
                                       ('UPDATE_PASSWORD', 'Изменение пароля'),
                                       ('DELETE_ACCOUNT', 'Удаление аккаунта'),
                                       ('CREATE_PROJECT', 'Создание проекта'),
                                       ('UPDATE_PROJECT', 'Изменение проекта'),
                                       ('DELETE_PROJECT', 'Удаление проекта'),
                                       ('ADD_FAVORITE_PROJECT', 'Добавление проекта в Избранное'),
                                       ('DELETE_FAVORITE_PROJECT',
                                        'Удаление проекта из Избранного')) AS user_authorities(name, description)),
     admin_authorities AS (SELECT *
                           FROM (VALUES ('DELETE_USER_ACCOUNT', 'Удаление аккаунта пользователя'),
                                        ('DELETE_USER_PROJECT',
                                         'Удаление проекта пользователя')) AS admin_authorities(name, description))
INSERT
INTO authorities (name, description, create_at, update_at)
SELECT name, description, NOW(), NOW()
FROM user_authorities
UNION ALL
SELECT name, description, NOW(), NOW()
FROM admin_authorities