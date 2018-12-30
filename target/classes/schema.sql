CREATE TABLE ACCOUNT (
  ID        IDENTITY              NOT NULL,
  NUMBER    VARCHAR           UNIQUE NOT NULL,
  BALANCE   DECIMAL(10,2)
);

INSERT INTO ACCOUNT VALUES(1, 'A', '12.44');
INSERT INTO ACCOUNT VALUES(2, 'B', '44.44');
