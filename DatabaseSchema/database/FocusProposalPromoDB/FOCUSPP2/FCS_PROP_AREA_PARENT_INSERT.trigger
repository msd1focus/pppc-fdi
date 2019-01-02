<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">6a8edf92-4e59-4e71-bef2-f7be9fab7ba3</identifier>
  </ID>
  <name>FCS_PROP_AREA_PARENT_INSERT</name>
  <baseType>TABLE</baseType>
  <code>DECLARE
   --PRAGMA AUTONOMOUS_TRANSACTION;
   V_PROPID   NUMBER;
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 30-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT REGION CODE KE TABLE PROP_REGION
   V_PROPID := :NEW.PROPOSAL_ID;

   INSERT INTO PROP_REGION (PROPOSAL_ID, REGION_CODE, DESCRIPTION)
        SELECT DISTINCT V_PROPID, AC.ATTRIBUTE3, FFVV.DESCRIPTION
        FROM APPS.AR_CUSTOMERS AC, APPS.FND_FLEX_VALUE_SETS FFV, APPS.FND_FLEX_VALUES_VL FFVV
        WHERE 1=1 AND AC.ATTRIBUTE4 = :NEW.AREA_CODE
        AND AC.ATTRIBUTE3 NOT IN (SELECT REGION_CODE FROM PROP_REGION WHERE PROPOSAL_ID = V_PROPID)
        AND FFV.FLEX_VALUE_SET_ID = FFVV.FLEX_VALUE_SET_ID
        AND FFV.FLEX_VALUE_SET_NAME = &apos;FCSAR_TERRITORY_REGION&apos;
        AND FFVV.ENABLED_FLAG = &apos;Y&apos;
        AND FLEX_VALUE = AC.ATTRIBUTE3
        GROUP BY AC.ATTRIBUTE3, FFVV.DESCRIPTION;
END;</code>
  <enabled>true</enabled>
  <events>
    <event>INSERT</event>
  </events>
  <properties>
    <entry>
      <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
      <value class="java.lang.String">focuspp</value>
    </entry>
    <entry>
      <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
      <value class="oracle.javatools.db.ReferenceID">
        <name>FCS_PROP_AREA_PARENT_INSERT</name>
        <identifier class="java.math.BigDecimal">111980</identifier>
        <schemaName>FOCUSPP2</schemaName>
        <type>TRIGGER</type>
      </value>
    </entry>
  </properties>
  <schema>
    <name>FOCUSPP2</name>
  </schema>
  <source>TRIGGER FOCUSPP2.FCS_PROP_AREA_PARENT_INSERT
AFTER INSERT
ON FOCUSPP2.PROP_REGION_AREA 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
   --PRAGMA AUTONOMOUS_TRANSACTION;
   V_PROPID   NUMBER;
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 30-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT REGION CODE KE TABLE PROP_REGION
   V_PROPID := :NEW.PROPOSAL_ID;

   INSERT INTO PROP_REGION (PROPOSAL_ID, REGION_CODE, DESCRIPTION)
        SELECT DISTINCT V_PROPID, AC.ATTRIBUTE3, FFVV.DESCRIPTION
        FROM APPS.AR_CUSTOMERS AC, APPS.FND_FLEX_VALUE_SETS FFV, APPS.FND_FLEX_VALUES_VL FFVV
        WHERE 1=1 AND AC.ATTRIBUTE4 = :NEW.AREA_CODE
        AND AC.ATTRIBUTE3 NOT IN (SELECT REGION_CODE FROM PROP_REGION WHERE PROPOSAL_ID = V_PROPID)
        AND FFV.FLEX_VALUE_SET_ID = FFVV.FLEX_VALUE_SET_ID
        AND FFV.FLEX_VALUE_SET_NAME = &apos;FCSAR_TERRITORY_REGION&apos;
        AND FFVV.ENABLED_FLAG = &apos;Y&apos;
        AND FLEX_VALUE = AC.ATTRIBUTE3
        GROUP BY AC.ATTRIBUTE3, FFVV.DESCRIPTION;
END; 
</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.ReferenceID">
    <name>PROP_REGION_AREA</name>
    <identifier class="java.math.BigDecimal">111781</identifier>
    <schemaName>FOCUSPP2</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>AFTER</timing>
</trigger>
