<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">3768bc72-969e-442a-b8a2-336efb0373ff</identifier>
  </ID>
  <name>BUDGET_CUSTOMER_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUSTOMER_ID IS NULL THEN
      SELECT BUDGET_CUSTOMER_SEQ.NEXTVAL INTO :NEW.BUDGET_CUSTOMER_ID FROM DUAL;
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
  <source>CREATE TRIGGER BUDGET_CUSTOMER_TRG 
BEFORE INSERT ON BUDGET_CUSTOMER 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUSTOMER_ID IS NULL THEN
      SELECT BUDGET_CUSTOMER_SEQ.NEXTVAL INTO :NEW.BUDGET_CUSTOMER_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BUDGET_CUSTOMER</name>
    <identifier class="java.lang.String">f5698de4-7aa8-4486-a55a-d548b283f1de</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
