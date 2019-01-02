<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">3ac474a3-7e6a-41d4-b65b-10cc446dc5c8</identifier>
  </ID>
  <name>BUDGET_CUST_TRAN_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUST_TRAN_ID IS NULL THEN
      SELECT BUDGET_CUST_TRAN_SEQ.NEXTVAL INTO :NEW.BUDGET_CUST_TRAN_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</code>
  <enabled>true</enabled>
  <events>
    <event>INSERT</event>
  </events>
  <schema>
    <name>FOCUSPP</name>
  </schema>
  <source>CREATE TRIGGER BUDGET_CUST_TRAN_TRG 
BEFORE INSERT ON BUDGET_CUST_TRAN 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUST_TRAN_ID IS NULL THEN
      SELECT BUDGET_CUST_TRAN_SEQ.NEXTVAL INTO :NEW.BUDGET_CUST_TRAN_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BUDGET_CUST_TRAN</name>
    <identifier class="java.lang.String">5c429d15-17dc-4735-9b64-095f1efe4844</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
