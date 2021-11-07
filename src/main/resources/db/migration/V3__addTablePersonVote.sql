CREATE TABLE person_vote (
     person_id UUID NOT NULL REFERENCES person(id),
     deal_id UUID NOT NULL REFERENCES deal(id),
     vote INTEGER NOT NULL,
     PRIMARY KEY (person_id, deal_id)
     );