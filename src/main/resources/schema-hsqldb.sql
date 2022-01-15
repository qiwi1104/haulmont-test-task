CREATE TABLE clients (
  id UUID PRIMARY KEY NOT NULL,
  name VARCHAR(255),
  phone VARCHAR(255),
  mail VARCHAR(255),
  passport VARCHAR(255)
);
CREATE TABLE banks (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE clients_banks (
    client_id UUID PRIMARY KEY NOT NULL,
    bank_id UUID PRIMARY KEY NOT NULL
);
CREATE TABLE credits (
    id UUID PRIMARY KEY NOT NULL,
    limit NUMERIC,
    interest NUMERIC,
    bank_id UUID NOT NULL
);
CREATE TABLE credit_offers (
    id UUID PRIMARY KEY NOT NULL,
    credit_sum NUMERIC,
    client_id UUID NOT NULL,
    credit_id UUID NOT NULL,
    bank_id UUID NOT NULL
);

CREATE TABLE payments (
    id UUID PRIMARY KEY NOT NULL,
    payment_date DATE,
    payment_sum NUMERIC,
    credit_sum NUMERIC,
    interest_sum NUMERIC,
    credit_offer_id UUID NOT NULL
);

ALTER TABLE clients_banks ADD CONSTRAINT client_id_constraint FOREIGN KEY (client_id) REFERENCES clients (id);
ALTER TABLE clients_banks ADD CONSTRAINT bank_id_constraint FOREIGN KEY (bank_id) REFERENCES banks (id);
ALTER TABLE credits ADD CONSTRAINT bank_id_constraint FOREIGN KEY (bank_id) REFERENCES banks (id);
ALTER TABLE credit_offers ADD CONSTRAINT client_id_constraint FOREIGN KEY (client_id) REFERENCES clients (id);
ALTER TABLE credit_offers ADD CONSTRAINT credit_id_constraint FOREIGN KEY (credit_id) REFERENCES credits (id);
ALTER TABLE credit_offers ADD CONSTRAINT bank_id_constraint FOREIGN KEY (bank_id) REFERENCES banks (id);
ALTER TABLE payments ADD CONSTRAINT credit_offer_id_constraint FOREIGN KEY (credit_offer_id) REFERENCES credit_offers (id);