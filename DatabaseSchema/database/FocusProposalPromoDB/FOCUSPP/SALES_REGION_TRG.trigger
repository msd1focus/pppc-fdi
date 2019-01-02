<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">a4efb11b-54e6-40a2-84cc-cd15feae85c7</identifier>
  </ID>
  <name>SALES_REGION_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.SALES_REGION_ID IS NULL THEN
      SELECT SALES_REGION_SEQ.NEXTVAL INTO :NEW.SALES_REGION_ID FROM DUAL;
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
  <source>CREATE TRIGGER SALES_REGION_TRG 
BEFORE INSERT ON SALES_REGION 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.SALES_REGION_ID IS NULL THEN
      SELECT SALES_REGION_SEQ.NEXTVAL INTO :NEW.SALES_REGION_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>SALES_REGION</name>
    <identifier class="java.lang.String">ecb81724-d020-4a0e-b1b0-5de38534e3bc</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
