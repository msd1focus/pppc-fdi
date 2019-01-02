<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">f7e759b8-a14e-46d0-b891-a0056f71e373</identifier>
  </ID>
  <name>REALISASI_ITEM_PAKET_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.REAL_ITEM_PAKET_ID IS NULL THEN
      SELECT REALISASI_ITEM_PAKET_SEQ.NEXTVAL INTO :NEW.REAL_ITEM_PAKET_ID FROM DUAL;
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
  <source>CREATE TRIGGER REALISASI_ITEM_PAKET_TRG 
BEFORE INSERT ON REALISASI_ITEM_PAKET 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.REAL_ITEM_PAKET_ID IS NULL THEN
      SELECT REALISASI_ITEM_PAKET_SEQ.NEXTVAL INTO :NEW.REAL_ITEM_PAKET_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>REALISASI_ITEM_PAKET</name>
    <identifier class="java.lang.String">1a67e02d-43c8-4b44-bf55-0ad14498cc37</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
