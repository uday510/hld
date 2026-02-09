---SESSION 1-----

UPDATE iso_test
SET balance = 100
WHERE name = 'Alice';

COMMIT;

SELECT * FROM iso_test;

SELECT balance
FROM iso_test
WHERE name = 'Alice';


BEGIN ISOLATION LEVEL REPEATABLE READ;

SHOW transaction_isolation;

SELECT balance
FROM iso_test
WHERE name = 'Alice';

---SESSION2-----

UPDATE iso_test
SET balance = 300
WHERE name = 'Alice';

COMMIT;