<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">f02ca02a-5a92-40b6-996a-2b4b3fcfc95f</identifier>
  </ID>
  <name>UPLOAD_DOWNLOAD_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROPOSAL_ID IS NULL THEN
      SELECT UPLOAD_DOWNLOAD_SEQ.NEXTVAL INTO :NEW.PROPOSAL_ID FROM DUAL;
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
  <source>CREATE TRIGGER UPLOAD_DOWNLOAD_TRG 
BEFORE INSERT ON UPLOAD_DOWNLOAD 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROPOSAL_ID IS NULL THEN
      SELECT UPLOAD_DOWNLOAD_SEQ.NEXTVAL INTO :NEW.PROPOSAL_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>UPLOAD_DOWNLOAD</name>
    <identifier class="java.lang.String">e1a5a6cd-9492-4ad6-995d-7b5815cd7412</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
