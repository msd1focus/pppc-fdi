<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">c4015364-4771-455d-b109-e06fc1947518</identifier>
  </ID>
  <name>PROPOSAL_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    SELECT PROPOSAL_SEQ.NEXTVAL INTO :NEW.PROPOSAL_ID FROM DUAL;
  END COLUMN_SEQUENCES;
END;</code>
  <enabled>true</enabled>
  <events>
    <event>INSERT</event>
  </events>
  <schema>
    <name>FOCUSPP</name>
  </schema>
  <source>CREATE TRIGGER PROPOSAL_TRG 
BEFORE INSERT ON PROPOSAL 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    SELECT PROPOSAL_SEQ.NEXTVAL INTO :NEW.PROPOSAL_ID FROM DUAL;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROPOSAL</name>
    <identifier class="java.lang.String">9e228503-a2ab-4c7b-8bee-51418dedb377</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
