<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">d786fce2-ee9c-4671-8ceb-34c5c0a736b0</identifier>
  </ID>
  <name>REP_FRM_PP_DOUBLE_TRG</name>
  <baseType>TABLE</baseType>
  <code>BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.REP_FRM_PP_DOUBLE_ID IS NULL THEN
      SELECT REP_FRM_PP_DOUBLE_SEQ.NEXTVAL INTO :NEW.REP_FRM_PP_DOUBLE_ID FROM DUAL;
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
  <source>CREATE TRIGGER REP_FRM_PP_DOUBLE_TRG 
BEFORE INSERT ON REP_FRM_PP_DOUBLE 
FOR EACH ROW 
BEGIN
  &lt;&lt;COLUMN_SEQUENCES&gt;&gt;
  BEGIN
    IF :NEW.REP_FRM_PP_DOUBLE_ID IS NULL THEN
      SELECT REP_FRM_PP_DOUBLE_SEQ.NEXTVAL INTO :NEW.REP_FRM_PP_DOUBLE_ID FROM DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.IdentifierBasedID">
    <name>REP_FRM_PP_DOUBLE</name>
    <identifier class="java.lang.String">b150b51b-fe6e-4121-b3f1-5ccea3e57e46</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>BEFORE</timing>
</trigger>
