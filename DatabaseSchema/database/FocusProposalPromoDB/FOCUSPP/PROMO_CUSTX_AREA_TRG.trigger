<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">3b56cdbb-c889-4c0d-a614-a01717b2786a</identifier>
  </ID>
  <name>PROMO_CUSTX_AREA_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTX_AREA_ID IS NULL THEN
      SELECT PROMO_CUSTX_AREA_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTX_AREA_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_CUSTX_AREA_TRG 
BEFORE INSERT ON PROMO_CUSTX_AREA 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTX_AREA_ID IS NULL THEN
      SELECT PROMO_CUSTX_AREA_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTX_AREA_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_CUSTX_AREA</name>
    <identifier class="java.lang.String">5244776f-faf1-459b-b1f4-d42d10250858</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
