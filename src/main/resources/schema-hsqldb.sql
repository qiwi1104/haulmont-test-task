CREATE TABLE clients (
  id UUID PRIMARY KEY NOT NULL,
  name VARCHAR(255),
  phone VARCHAR(255),
  mail VARCHAR(255),
  passport VARCHAR(255),
  bank_id INTEGER
);
CREATE TABLE banks (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE clients_banks (
    client_id UUID NOT NULL,
    bank_id UUID NOT NULL,
    CONSTRAINT client_id_constraint FOREIGN KEY (client_id) REFERENCES clients (id)
    CONSTRAINT bank_id_constraint FOREIGN KEY (bank_id) REFERENCES banks (id)
);
CREATE TABLE credits (
    id UUID PRIMARY KEY NOT NULL,
    limit DOUBLE,
    interest DOUBLE,
    bank_id INTEGER,
    client_id,
    CONSTRAINT bank_id_constraint FOREIGN KEY (bank_id) REFERENCES banks (id)
    CONSTRAINT client_id_constraint FOREIGN KEY (client_id) REFERENCES clients (id)
);
--CREATE TABLE credits_clients (
--    client_id UUID NOT NULL,
--    credit_id UUID NOT NULL,
--    CONSTRAINT client_id_constraint FOREIGN KEY (client_id) REFERENCES clients (id)
--    CONSTRAINT credit_id_constraint FOREIGN KEY (credit_id) REFERENCES credits (id)
--);