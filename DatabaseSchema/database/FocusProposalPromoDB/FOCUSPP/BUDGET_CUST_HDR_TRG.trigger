<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">932f2052-7508-46fb-ae30-ac52af768138</identifier>
  </ID>
  <name>BUDGET_CUST_HDR_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUST_HDR_ID IS NULL THEN
      SELECT BUDGET_CUST_HDR_SEQ.NEXTVAL INTO :NEW.BUDGET_CUST_HDR_ID FROM DUAL;
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
  <source>CREATE TRIGGER BUDGET_CUST_HDR_TRG 
BEFORE INSERT ON BUDGET_CUST_HDR 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.BUDGET_CUST_HDR_ID IS NULL THEN
      SELECT BUDGET_CUST_HDR_SEQ.NEXTVAL INTO :NEW.BUDGET_CUST_HDR_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>BUDGET_CUST_HDR</name>
    <identifier class="java.lang.String">771faaf0-497f-4373-9d36-bbaea78cdc4c</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
