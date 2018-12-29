CREATE TABLE ACCOUNT (
  ID        IDENTITY      NOT NULL,
  NUMBER    TEXT          NOT NULL,
  BALANCE   DECIMAL(10,2)
);

INSERT INTO ACCOUNT VALUES(1, 'A-B-C-D', '12.44');
INSERT INTO ACCOUNT VALUES(2, 'E-F-G-H', '44.44');