<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">9c18ef0b-3540-4bc7-a845-7283f3cfc3c2</identifier>
  </ID>
  <name>FCS_PROD_AREA_PARENT_INSERT</name>
  <baseType>TABLE</baseType>
  <code>DECLARE
   --PRAGMA AUTONOMOUS_TRANSACTION;
   V_PPID   NUMBER;
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 30-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT REGION CODE KE TABLE PROD_REGION
   V_PPID := :NEW.PROMO_PRODUK_ID;

   INSERT INTO PROD_REGION (PROMO_PRODUK_ID, REGION_CODE, NOTES)
        SELECT DISTINCT V_PPID, AC.ATTRIBUTE3, FFVV.DESCRIPTION
        FROM APPS.AR_CUSTOMERS AC, APPS.FND_FLEX_VALUE_SETS FFV, APPS.FND_FLEX_VALUES_VL FFVV
        WHERE 1=1 AND AC.ATTRIBUTE4 = :NEW.AREA_CODE
        AND AC.ATTRIBUTE3 NOT IN (SELECT REGION_CODE FROM PROD_REGION WHERE PROMO_PRODUK_ID = V_PPID)
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
        <name>FCS_PROD_AREA_PARENT_INSERT</name>
        <identifier class="java.math.BigDecimal">111882</identifier>
        <schemaName>FOCUSPP2</schemaName>
        <type>TRIGGER</type>
      </value>
    </entry>
  </properties>
  <schema>
    <name>FOCUSPP2</name>
  </schema>
  <source>TRIGGER FOCUSPP2.FCS_PROD_AREA_PARENT_INSERT
AFTER INSERT
ON FOCUSPP2.PROD_REGION_AREA 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
   --PRAGMA AUTONOMOUS_TRANSACTION;
   V_PPID   NUMBER;
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 30-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT REGION CODE KE TABLE PROD_REGION
   V_PPID := :NEW.PROMO_PRODUK_ID;

   INSERT INTO PROD_REGION (PROMO_PRODUK_ID, REGION_CODE, NOTES)
        SELECT DISTINCT V_PPID, AC.ATTRIBUTE3, FFVV.DESCRIPTION
        FROM APPS.AR_CUSTOMERS AC, APPS.FND_FLEX_VALUE_SETS FFV, APPS.FND_FLEX_VALUES_VL FFVV
        WHERE 1=1 AND AC.ATTRIBUTE4 = :NEW.AREA_CODE
        AND AC.ATTRIBUTE3 NOT IN (SELECT REGION_CODE FROM PROD_REGION WHERE PROMO_PRODUK_ID = V_PPID)
        AND FFV.FLEX_VALUE_SET_ID = FFVV.FLEX_VALUE_SET_ID
        AND FFV.FLEX_VALUE_SET_NAME = &apos;FCSAR_TERRITORY_REGION&apos;
        AND FFVV.ENABLED_FLAG = &apos;Y&apos;
        AND FLEX_VALUE = AC.ATTRIBUTE3
        GROUP BY AC.ATTRIBUTE3, FFVV.DESCRIPTION;
END; 
</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.ReferenceID">
    <name>PROD_REGION_AREA</name>
    <identifier class="java.math.BigDecimal">111757</identifier>
    <schemaName>FOCUSPP2</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>AFTER</timing>
</trigger>
