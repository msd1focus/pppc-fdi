<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">7022d266-30aa-46b9-b3db-39f06ade9598</identifier>
  </ID>
  <name>USER_UPLOAD_LOG_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.USER_UPLOAD_LOG_ID IS NULL THEN
      SELECT USER_UPLOAD_LOG_SEQ.NEXTVAL INTO :NEW.USER_UPLOAD_LOG_ID FROM DUAL;
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
  <source>CREATE TRIGGER USER_UPLOAD_LOG_TRG 
BEFORE INSERT ON USER_UPLOAD_LOG 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.USER_UPLOAD_LOG_ID IS NULL THEN
      SELECT USER_UPLOAD_LOG_SEQ.NEXTVAL INTO :NEW.USER_UPLOAD_LOG_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>USER_UPLOAD_LOG</name>
    <identifier class="java.lang.String">b72798b0-6265-4073-9329-76cfcf22df12</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
