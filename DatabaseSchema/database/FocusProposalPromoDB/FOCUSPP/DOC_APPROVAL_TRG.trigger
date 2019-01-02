<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">7be2702a-77cb-47ae-98a1-d521844ab1a0</identifier>
  </ID>
  <name>DOC_APPROVAL_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.DOC_APPROVAL_ID IS NULL THEN
      SELECT DOC_APPROVAL_SEQ.NEXTVAL INTO :NEW.DOC_APPROVAL_ID FROM DUAL;
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
  <source>CREATE TRIGGER DOC_APPROVAL_TRG 
BEFORE INSERT ON DOC_APPROVAL 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.DOC_APPROVAL_ID IS NULL THEN
      SELECT DOC_APPROVAL_SEQ.NEXTVAL INTO :NEW.DOC_APPROVAL_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>DOC_APPROVAL</name>
    <identifier class="java.lang.String">5e8fc7a3-51f4-4c74-8447-862093c4a3c0</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
