----- SESSION 1-------

BEGIN ISOLATION LEVEL SERIALIZABLE;

UPDATE iso_test
SET balance = 100
WHERE name = 'Alice';


UPDATE iso_test
SET balance = balance + 30
WHERE name = 'Alice';

COMMIT;


----- SESSION 2 ------

BEGIN ISOLATION LEVEL SERIALIZABLE;

SELECT balance
FROM iso_test
WHERE name = 'Alice';


UPDATE iso_test
SET balance = balance + 50
WHERE name = 'Alice';

COMMIT;