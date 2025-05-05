CREATE TABLE nome (
                      id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";