<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">e71de153-f9c4-4a67-8f18-2adde6ef3308</identifier>
  </ID>
  <name>UT_USER_ACCESS_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.USER_NAME IS NULL THEN
      SELECT UT_USER_ACCESS_SEQ.NEXTVAL INTO :NEW.USER_NAME FROM DUAL;
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
  <source>CREATE TRIGGER UT_USER_ACCESS_TRG 
BEFORE INSERT ON UT_USER_ACCESS 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.USER_NAME IS NULL THEN
      SELECT UT_USER_ACCESS_SEQ.NEXTVAL INTO :NEW.USER_NAME FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>UT_USER_ACCESS</name>
    <identifier class="java.lang.String">af3e6667-654c-441c-a61d-0e7e426827cc</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
