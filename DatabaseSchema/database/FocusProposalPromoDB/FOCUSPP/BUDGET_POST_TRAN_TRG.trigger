<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">74c4ab96-6be4-4c8f-9330-5b801a9734ab</identifier>
  </ID>
  <name>BUDGET_POST_TRAN_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_POST_TRAN_ID IS NULL THEN
      SELECT BUDGET_POST_TRAN_SEQ.NEXTVAL INTO :NEW.BUDGET_POST_TRAN_ID FROM DUAL;
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
  <source>CREATE TRIGGER BUDGET_POST_TRAN_TRG 
BEFORE INSERT ON BUDGET_POST_TRAN 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_POST_TRAN_ID IS NULL THEN
      SELECT BUDGET_POST_TRAN_SEQ.NEXTVAL INTO :NEW.BUDGET_POST_TRAN_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BUDGET_POST_TRAN</name>
    <identifier class="java.lang.String">a9979daf-3cd5-43e2-b23f-385c6008faa0</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
