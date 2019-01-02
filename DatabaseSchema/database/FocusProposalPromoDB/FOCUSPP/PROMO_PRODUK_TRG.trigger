<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">b9bc3568-144d-4cce-b4f3-e4fe1d41f74c</identifier>
  </ID>
  <name>PROMO_PRODUK_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_PRODUK_ID IS NULL THEN
      SELECT PROMO_PRODUK_SEQ.NEXTVAL INTO :NEW.PROMO_PRODUK_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_PRODUK_TRG 
BEFORE INSERT ON PROMO_PRODUK 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_PRODUK_ID IS NULL THEN
      SELECT PROMO_PRODUK_SEQ.NEXTVAL INTO :NEW.PROMO_PRODUK_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_PRODUK</name>
    <identifier class="java.lang.String">f7f1884e-1801-494b-b14b-e1870da93229</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
