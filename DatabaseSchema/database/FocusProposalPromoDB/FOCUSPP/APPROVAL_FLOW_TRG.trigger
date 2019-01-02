<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">09104d4d-3dfe-4d13-92e5-75c64be1e8bd</identifier>
  </ID>
  <name>APPROVAL_FLOW_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.ID_APRVL_FLOW IS NULL THEN
      SELECT APPROVAL_FLOW_SEQ.NEXTVAL INTO :NEW.ID_APRVL_FLOW FROM DUAL;
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
  <source>CREATE TRIGGER APPROVAL_FLOW_TRG 
BEFORE INSERT ON APPROVAL_FLOW 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.ID_APRVL_FLOW IS NULL THEN
      SELECT APPROVAL_FLOW_SEQ.NEXTVAL INTO :NEW.ID_APRVL_FLOW FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>APPROVAL_FLOW</name>
    <identifier class="java.lang.String">a27d0ec6-57c1-499e-b5a7-d286978c5907</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
