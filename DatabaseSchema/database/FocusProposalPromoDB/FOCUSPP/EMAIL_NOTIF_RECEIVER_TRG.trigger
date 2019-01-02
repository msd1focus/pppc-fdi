<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">44b18a51-d808-412d-b295-80dd206935ab</identifier>
  </ID>
  <name>EMAIL_NOTIF_RECEIVER_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.MNR_ID IS NULL THEN
      SELECT EMAIL_NOTIF_RECEIVER_SEQ.NEXTVAL INTO :NEW.MNR_ID FROM DUAL;
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
  <source>CREATE TRIGGER EMAIL_NOTIF_RECEIVER_TRG 
BEFORE INSERT ON EMAIL_NOTIF_RECEIVER 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.MNR_ID IS NULL THEN
      SELECT EMAIL_NOTIF_RECEIVER_SEQ.NEXTVAL INTO :NEW.MNR_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>EMAIL_NOTIF_RECEIVER</name>
    <identifier class="java.lang.String">63620532-b622-4af5-8c60-1188f11ffed5</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
