<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">418559ac-8040-4216-8120-f46675d02d84</identifier>
  </ID>
  <name>PROMO_CUSTX_HO_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTX_HO_ID IS NULL THEN
      SELECT PROMO_CUSTX_HO_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTX_HO_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_CUSTX_HO_TRG 
BEFORE INSERT ON PROMO_CUSTX_HO 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTX_HO_ID IS NULL THEN
      SELECT PROMO_CUSTX_HO_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTX_HO_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_CUSTX_HO</name>
    <identifier class="java.lang.String">be406cac-032e-46ba-a25f-73bbae0d322b</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
