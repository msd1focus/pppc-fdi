<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">f510768f-9b2d-4702-b8e7-cbc53aa4d56a</identifier>
  </ID>
  <name>APPROVAL_STEPS_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.ID_APRVL_STEP IS NULL THEN
      SELECT APPROVAL_STEPS_SEQ.NEXTVAL INTO :NEW.ID_APRVL_STEP FROM DUAL;
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
  <source>CREATE TRIGGER APPROVAL_STEPS_TRG 
BEFORE INSERT ON APPROVAL_STEPS 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.ID_APRVL_STEP IS NULL THEN
      SELECT APPROVAL_STEPS_SEQ.NEXTVAL INTO :NEW.ID_APRVL_STEP FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>APPROVAL_STEPS</name>
    <identifier class="java.lang.String">a85a06f6-54e0-4809-afc6-cc2bd62ab062</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
