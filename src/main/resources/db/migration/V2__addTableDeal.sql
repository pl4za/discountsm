CREATE TABLE deal (
     id UUID PRIMARY KEY,
     title TEXT NOT NULL,
     description TEXT NOT NULL,
     new_price_amount NUMERIC(19,2) NOT NULL,
     new_price_currency VARCHAR(3) NOT NULL,
     old_price_amount NUMERIC(19,2) NOT NULL,
     old_price_currency VARCHAR(3) NOT NULL,
     up_votes INTEGER NOT NULL,
     down_votes INTEGER NOT NULL,
     posted TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     expiry TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     deal_link TEXT NOT NULL,
     image_link TEXT NOT NULL
     );