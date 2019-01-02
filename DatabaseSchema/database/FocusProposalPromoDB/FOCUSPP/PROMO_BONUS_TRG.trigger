<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">e8d841ff-23a5-47a4-8a0e-c19564dc125d</identifier>
  </ID>
  <name>PROMO_BONUS_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_BONUS_ID IS NULL THEN
      SELECT PROMO_BONUS_SEQ.NEXTVAL INTO :NEW.PROMO_BONUS_ID FROM DUAL;
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
  <source>CREATE TRIGGER PROMO_BONUS_TRG 
BEFORE INSERT ON PROMO_BONUS 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.PROMO_BONUS_ID IS NULL THEN
      SELECT PROMO_BONUS_SEQ.NEXTVAL INTO :NEW.PROMO_BONUS_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>PROMO_BONUS</name>
    <identifier class="java.lang.String">59c25971-d7bd-4c34-84a1-768ad7e5c70f</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
