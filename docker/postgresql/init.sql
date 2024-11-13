-- Schéma relationnel
CREATE TABLE test (
    id SERIAL PRIMARY KEY,
    testName VARCHAR(50) NOT NULL
);

-- Données de seed
INSERT INTO test (testName) VALUES 
    ('test1'),
    ('test2');