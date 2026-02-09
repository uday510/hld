-----------SESSION 1 ----------
DROP TABLE IF EXISTS iso_test;

CREATE TABLE iso_test (
	id SERIAL PRIMARY KEY,
	name TEXT,
	balance INT
);

INSERT INTO iso_test(name, balance)
VALUES ('Alice', 100);

COMMIT;

SELECT * FROM iso_test;


------------SESSION 2--------

BEGIN;

SHOW transaction_isolation;

SELECT balance
FROM iso_test
WHERE name = 'Alice';

UPDATE iso_test
SET balance = 200
WHERE name = 'Alice';

COMMIT;