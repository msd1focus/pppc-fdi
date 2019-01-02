<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">104712b8-4095-48ae-b25f-872aeddaf990</identifier>
  </ID>
  <name>PROMO_CUSTOMER_HO_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTOMER_ID IS NULL THEN
      SELECT PROMO_CUSTOMER_HO_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTOMER_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_CUSTOMER_HO_TRG 
BEFORE INSERT ON PROMO_CUSTOMER_HO 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_CUSTOMER_ID IS NULL THEN
      SELECT PROMO_CUSTOMER_HO_SEQ.NEXTVAL INTO :NEW.PROMO_CUSTOMER_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_CUSTOMER_HO</name>
    <identifier class="java.lang.String">76a29a75-1841-4b1d-a160-7e70f072e9f1</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
