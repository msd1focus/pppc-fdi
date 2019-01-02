<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">0c40c964-908e-44f1-83ab-3599eb996769</identifier>
  </ID>
  <name>PROD_BUDGET_BY_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_BY_ID IS NULL THEN
      SELECT PROD_BUDGET_BY_SEQ.NEXTVAL INTO :NEW.BUDGET_BY_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROD_BUDGET_BY_TRG 
BEFORE INSERT ON PROD_BUDGET_BY 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_BY_ID IS NULL THEN
      SELECT PROD_BUDGET_BY_SEQ.NEXTVAL INTO :NEW.BUDGET_BY_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROD_BUDGET_BY</name>
    <identifier class="java.lang.String">a6316842-fa8c-4a25-9a90-67b6db97283c</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
