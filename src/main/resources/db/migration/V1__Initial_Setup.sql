    CREATE TABLE customer(
    id BIGSERIAL primary key ,
    name TEXT NOT NULL ,
    email TEXT UNIQUE NOT NULL ,
    age INT NOT NULL
);