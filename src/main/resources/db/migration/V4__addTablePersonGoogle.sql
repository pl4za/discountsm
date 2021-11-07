CREATE TABLE person_google (
     id UUID PRIMARY KEY REFERENCES person(id),
     email TEXT NOT NULL NOT NULL,
     name TEXT NOT NULL,
     image TEXT NOT NULL,
     locale TEXT NOT NULL,
     given_name TEXT NOT NULL,
     family_name TEXT NOT NULL
     );