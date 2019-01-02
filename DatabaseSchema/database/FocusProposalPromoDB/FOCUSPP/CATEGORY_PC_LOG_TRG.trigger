<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">bdd90c96-3718-40c5-91da-5831daf7eabf</identifier>
  </ID>
  <name>CATEGORY_PC_LOG_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.CATEGORY_PC_LOG_ID IS NULL THEN
      SELECT CATEGORY_PC_LOG_SEQ.NEXTVAL INTO :NEW.CATEGORY_PC_LOG_ID FROM DUAL;
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
  <source>CREATE TRIGGER CATEGORY_PC_LOG_TRG 
BEFORE INSERT ON CATEGORY_PC_LOG 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.CATEGORY_PC_LOG_ID IS NULL THEN
      SELECT CATEGORY_PC_LOG_SEQ.NEXTVAL INTO :NEW.CATEGORY_PC_LOG_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>CATEGORY_PC_LOG</name>
    <identifier class="java.lang.String">ccc203d3-6e5d-4b8b-997f-57b021b74591</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
