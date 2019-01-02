<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">260389e1-a511-41a7-8f33-297c6becb5d8</identifier>
  </ID>
  <name>LOOKUP_CODE_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.LOOKUP_ID IS NULL THEN
      SELECT LOOKUP_CODE_SEQ.NEXTVAL INTO :NEW.LOOKUP_ID FROM DUAL;
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
  <source>CREATE TRIGGER LOOKUP_CODE_TRG 
BEFORE INSERT ON LOOKUP_CODE 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.LOOKUP_ID IS NULL THEN
      SELECT LOOKUP_CODE_SEQ.NEXTVAL INTO :NEW.LOOKUP_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>LOOKUP_CODE</name>
    <identifier class="java.lang.String">1568847f-7913-44bd-82d9-bf1c391dda48</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
