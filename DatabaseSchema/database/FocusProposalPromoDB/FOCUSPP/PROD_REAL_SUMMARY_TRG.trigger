<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">6ed06546-127b-4d2c-a429-cee5d0c9588a</identifier>
  </ID>
  <name>PROD_REAL_SUMMARY_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROD_REAL_SUM_ID IS NULL THEN
      SELECT PROD_REAL_SUMMARY_SEQ.NEXTVAL INTO :NEW.PROD_REAL_SUM_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROD_REAL_SUMMARY_TRG 
BEFORE INSERT ON PROD_REAL_SUMMARY 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROD_REAL_SUM_ID IS NULL THEN
      SELECT PROD_REAL_SUMMARY_SEQ.NEXTVAL INTO :NEW.PROD_REAL_SUM_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROD_REAL_SUMMARY</name>
    <identifier class="java.lang.String">72b94a6d-5548-4cdd-8576-5d8f563502f0</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
