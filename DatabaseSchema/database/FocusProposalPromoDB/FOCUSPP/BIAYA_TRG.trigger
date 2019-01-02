<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">1a75c006-825f-45b1-84db-89dfe5d5f8bd</identifier>
  </ID>
  <name>BIAYA_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BIAYA_ID IS NULL THEN
      SELECT BIAYA_SEQ.NEXTVAL INTO :NEW.BIAYA_ID FROM DUAL;
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
  <source>CREATE TRIGGER BIAYA_TRG 
BEFORE INSERT ON BIAYA 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BIAYA_ID IS NULL THEN
      SELECT BIAYA_SEQ.NEXTVAL INTO :NEW.BIAYA_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BIAYA</name>
    <identifier class="java.lang.String">369508f6-debd-48f7-8bb6-9d28b74d3af3</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
