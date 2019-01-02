<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">c029c40b-d067-42f1-8756-67913f76c1f9</identifier>
  </ID>
  <name>PROMO_CUSTOMER_AREA_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTOMER_ID IS NULL THEN
      SELECT PROMO_CUSTOMER_AREA_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTOMER_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_CUSTOMER_AREA_TRG 
BEFORE INSERT ON PROMO_CUSTOMER_AREA 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTOMER_ID IS NULL THEN
      SELECT PROMO_CUSTOMER_AREA_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTOMER_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_CUSTOMER_AREA</name>
    <identifier class="java.lang.String">06b02338-7058-447c-a06c-33070bb1725a</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
