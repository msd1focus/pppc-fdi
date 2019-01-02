<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">51781ec8-16b4-4620-9886-6b200de023af</identifier>
  </ID>
  <name>BUDGET_POSTING_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_POSTING_ID IS NULL THEN
      SELECT BUDGET_POSTING_SEQ.NEXTVAL INTO :NEW.BUDGET_POSTING_ID FROM DUAL;
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
  <source>CREATE TRIGGER BUDGET_POSTING_TRG 
BEFORE INSERT ON BUDGET_POSTING 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_POSTING_ID IS NULL THEN
      SELECT BUDGET_POSTING_SEQ.NEXTVAL INTO :NEW.BUDGET_POSTING_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BUDGET_POSTING</name>
    <identifier class="java.lang.String">78015df3-aa83-4bdf-9601-89623ad30884</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
