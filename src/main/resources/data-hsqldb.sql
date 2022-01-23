
INSERT INTO BANKS (ID, NAME) VALUES
(UUID(), 'Sberbank'),
(UUID(), 'Tinkoff'),
(UUID(), 'VTB'),
(UUID(), 'Raiffeisen');

INSERT INTO CLIENTS (ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, PHONE, MAIL, PASSPORT) VALUES
(UUID(), 'Yevgeniy', '', 'Onegin', '89993031303', 'heartbreaker@mail.ru', '4520181932'),
(UUID(), 'Ivan', 'Ivanovich', 'Pererepenko', '89023087216', 'quarrelsome@yandex.ru', '3723182456'),
(UUID(), 'Ivan', 'Nikiforovich', 'Dovgochkhun', '89610524903', 'landlord@yandex.ru', '3719182383'),
(UUID(), 'Pavel', 'Ivanovich', 'Chichikov', '89277042274', 'shady.dealer500@mail.ru', '4328183173'),
(UUID(), 'Platon', 'Kuzmich', 'Kovalev', '89603028941', 'rhinoplain@yandex.ru', '3917312903'),
(UUID(), 'Akakij', 'Akakievich', 'Bashmachkin', '89031203387', 'coat@mail.ru', '4503230007'),
(UUID(), 'Grigorij', 'Aleksandrovich', 'Pechorin', '89291038032', 'soldier@mail.ru', '2303143037'),
(UUID(), 'Ivan', 'Ilyich', 'Golovin', '89096664213', 'morbidlife@mail.ru', '2301037083'),
(UUID(), 'Rodion', 'Romanovich', 'Raskolnikov', '89376042103', 'poorstudent@mail.ru', '2830183903'),
(UUID(), 'Aleksej', 'Ivanovich', 'Velchaninov', '89069123393', 'maritalbliss@gmail.com', '4503308901'),
(UUID(), 'Pyotr', 'Ivanovich', 'Blinov', '89073228432', 'dreamer@gmail.com', '1703894752');

INSERT INTO CREDITS (ID, LIMIT, INTEREST, BANK_ID) VALUES
(UUID(), 100000, 12, (SELECT ID FROM BANKS WHERE NAME = 'Sberbank')),
(UUID(), 5000000, 5.99, (SELECT ID FROM BANKS WHERE NAME = 'Raiffeisen')),
(UUID(), 1000000, 8, (SELECT ID FROM BANKS WHERE NAME = 'Tinkoff')),
(UUID(), 2000000, 6.5, (SELECT ID FROM BANKS WHERE NAME = 'VTB'));

INSERT INTO CLIENTS_BANKS (CLIENT_ID, BANK_ID) VALUES
((SELECT ID FROM CLIENTS WHERE PASSPORT = '3723182456'), (SELECT ID FROM BANKS WHERE NAME = 'Sberbank')),
((SELECT ID FROM CLIENTS WHERE PASSPORT = '4328183173'), (SELECT ID FROM BANKS WHERE NAME = 'Raiffeisen')),
((SELECT ID FROM CLIENTS WHERE PASSPORT = '1703894752'), (SELECT ID FROM BANKS WHERE NAME = 'Sberbank')),
((SELECT ID FROM CLIENTS WHERE PASSPORT = '2830183903'), (SELECT ID FROM BANKS WHERE NAME = 'Sberbank')),
((SELECT ID FROM CLIENTS WHERE PASSPORT = '2303143037'), (SELECT ID FROM BANKS WHERE NAME = 'VTB')),
((SELECT ID FROM CLIENTS WHERE PASSPORT = '1703894752'), (SELECT ID FROM BANKS WHERE NAME = 'Tinkoff'));