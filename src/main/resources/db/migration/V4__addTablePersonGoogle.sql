CREATE TABLE person_google (
     id UUID PRIMARY KEY REFERENCES person(id),
     email TEXT UNIQUE NOT NULL,
     name TEXT NOT NULL,
     image TEXT,
     locale TEXT,
     given_name TEXT,
     family_name TEXT
     );