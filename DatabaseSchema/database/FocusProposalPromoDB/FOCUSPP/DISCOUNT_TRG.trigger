<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">29610925-a6b6-431b-bab4-1160e1566b21</identifier>
  </ID>
  <name>DISCOUNT_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.DISCOUNT_ID IS NULL THEN
      SELECT DISCOUNT_SEQ.NEXTVAL INTO :NEW.DISCOUNT_ID FROM DUAL;
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
  <source>CREATE TRIGGER DISCOUNT_TRG 
BEFORE INSERT ON DISCOUNT 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.DISCOUNT_ID IS NULL THEN
      SELECT DISCOUNT_SEQ.NEXTVAL INTO :NEW.DISCOUNT_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>DISCOUNT</name>
    <identifier class="java.lang.String">a33a179f-bb87-4b9b-9d7d-d958f3c4423e</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
