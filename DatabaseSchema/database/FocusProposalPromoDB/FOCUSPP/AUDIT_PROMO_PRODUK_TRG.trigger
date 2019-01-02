<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">4bc1ed86-9746-46f5-a242-8e4153685dcc</identifier>
  </ID>
  <name>AUDIT_PROMO_PRODUK_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_PRODUK_ID IS NULL THEN
      SELECT AUDIT_PROMO_PRODUK_SEQ.NEXTVAL INTO :NEW.PROMO_PRODUK_ID FROM DUAL;
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
  <source>CREATE TRIGGER AUDIT_PROMO_PRODUK_TRG 
BEFORE INSERT ON AUDIT_PROMO_PRODUK 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_PRODUK_ID IS NULL THEN
      SELECT AUDIT_PROMO_PRODUK_SEQ.NEXTVAL INTO :NEW.PROMO_PRODUK_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>AUDIT_PROMO_PRODUK</name>
    <identifier class="java.lang.String">e6b7230e-e66b-4b41-a25d-b43842e60937</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
