<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">80a49af7-0f59-4fde-ad55-fbfefdb36e2a</identifier>
  </ID>
  <name>AUDIT_PROPOSAL_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.AUDIT_PROPOSAL_ID IS NULL THEN
      SELECT AUDIT_PROPOSAL_SEQ.NEXTVAL INTO :NEW.AUDIT_PROPOSAL_ID FROM DUAL;
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
  <source>CREATE TRIGGER AUDIT_PROPOSAL_TRG 
BEFORE INSERT ON AUDIT_PROPOSAL 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.AUDIT_PROPOSAL_ID IS NULL THEN
      SELECT AUDIT_PROPOSAL_SEQ.NEXTVAL INTO :NEW.AUDIT_PROPOSAL_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>AUDIT_PROPOSAL</name>
    <identifier class="java.lang.String">6f471f14-a1a3-4fcf-af18-1797ca681930</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
