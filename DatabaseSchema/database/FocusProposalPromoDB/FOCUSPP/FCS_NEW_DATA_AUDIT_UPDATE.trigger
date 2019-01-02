<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">87fef760-62ba-49a0-81ef-55de249b2773</identifier>
  </ID>
  <name>FCS_NEW_DATA_AUDIT_UPDATE</name>
  <baseType>TABLE</baseType>
  <code>DECLARE
   PRAGMA AUTONOMOUS_TRANSACTION;
   V_AUDIT_ID        NUMBER;
   V_PREV_AUDIT_ID   NUMBER;
   V_NEW_PROP_ID     NUMBER;
   V_IF_EXISTS       NUMBER;
   V_MODIFIED_BY     VARCHAR (30);
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 10-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT KE TABLE DATA_AUDIT

   IF (:NEW.ACTION IS NOT NULL OR :NEW.ACTION &lt;&gt; &apos;FINISHED&apos;)
   THEN
      V_AUDIT_ID := AUDIT_PROPOSAL_SEQ.NEXTVAL;
      V_NEW_PROP_ID := :NEW.PROPOSAL_ID;
      V_MODIFIED_BY := :NEW.ACTION_TO;
      
      SELECT COUNT(1) INTO V_IF_EXISTS
        FROM   AUDIT_PROPOSAL PR
       WHERE   PR.MODIFIED_BY = V_MODIFIED_BY
               AND PR.PROPOSAL_ID = V_NEW_PROP_ID;

      IF V_IF_EXISTS &gt; 0 THEN 
          
          SELECT   NVL(PR.AUDIT_ID,0)
            INTO   V_PREV_AUDIT_ID
            FROM   AUDIT_PROPOSAL PR
           WHERE   PR.MODIFIED_BY = V_MODIFIED_BY
                   AND PR.PROPOSAL_ID = V_NEW_PROP_ID;


          -- DELETE PREVIOUS AUDIT DATA
          DELETE FROM   AUDIT_PROPOSAL PR
                WHERE   PR.PROPOSAL_ID = V_NEW_PROP_ID
                        AND PR.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PROMO_PRODUK PP
                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID
                        AND PP.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PRODUK_VARIANT PV
                WHERE   PV.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PV.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PRODUK_ITEM PI
                WHERE   PI.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PI.AUDIT_ID = V_AUDIT_ID;

          DELETE FROM   AUDIT_TARGET TG
                WHERE   TG.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND TG.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_BIAYA BI
                WHERE   BI.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND BI.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_DISCOUNT DS
                WHERE   DS.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND DS.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PROMO_BONUS PB
                WHERE   PB.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PB.AUDIT_ID = V_PREV_AUDIT_ID;
          
          DELETE FROM   AUDIT_PROMO_BONUS_VARIANT PBV
                WHERE   PBV.PROMO_BONUS_ID IN
                              (SELECT   PB.PROMO_BONUS_ID
                                 FROM   AUDIT_PROMO_BONUS PB, AUDIT_PROMO_PRODUK PP
                                WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PBV.AUDIT_ID = V_PREV_AUDIT_ID;
          
          DELETE FROM   AUDIT_PROMO_BONUS_ITEM PBI
                WHERE   PBI.PROMO_BONUS_ID IN
                              (SELECT   PB.PROMO_BONUS_ID
                                 FROM   AUDIT_PROMO_BONUS PB, AUDIT_PROMO_PRODUK PP
                                WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PBI.AUDIT_ID = V_PREV_AUDIT_ID;
      END IF;

      -- INSERT PROPOSAL AUDIT
      INSERT INTO AUDIT_PROPOSAL (AUDIT_ID,
                                  PROPOSAL_ID,
                                  CREATED_BY,
                                  CREATION_DATE,
                                  MODIFIED_BY,
                                  MODIFIED_DATE,
                                  APPROVAL_ACTION)
         SELECT   V_AUDIT_ID,
                  :NEW.PROPOSAL_ID,
                  :NEW.CREATED_BY,
                  :NEW.CREATION_DATE,
                  :NEW.LAST_UPDATED_BY,
                  :NEW.LAST_UPDATE_DATE,
                  :NEW.ACTION
           FROM   DUAL;

      -- INSERT PROMO PRODUK AUDIT
      INSERT INTO AUDIT_PROMO_PRODUK (AUDIT_ID,
                                      PROMO_PRODUK_ID,
                                      PROPOSAL_ID,
                                      AMOUNT_PROMO,
                                      PRODUCT_CATEGORY,
                                      PRODUCT_CLASS,
                                      PRODUCT_BRAND,
                                      PRODUCT_EXT,
                                      PRODUCT_PACK,
                                      DESCR,
                                      MEKANISME,
                                      EST_BUDGET_PROP,
                                      REG_CUST_FLAG,
                                      KODE_POSTING,
                                      BUDGET_BY,
                                      VALID_COMB,
                                      ITEM_EXPENSE,
                                      KODE_POSTING_MF,
                                      EXCL_CUST_BY,
                                      DISC_MF,
                                      DISC_ON_TOP,
                                      DISC_RASIO_MF,
                                      DISC_RASIO_ON_TOP,
                                      DISC_RASIO_TOTAL,
                                      BRG_BONUS_MF,
                                      BRG_BONUS_ON_TOP,
                                      BRG_BONUS_RASIO_MF,
                                      BRG_BONUS_RASIO_ON_TOP,
                                      BRG_BONUS_RASIO_TOTAL,
                                      BIA_MF,
                                      BIA_ONTOP,
                                      BIA_RASIO_MF,
                                      BIA_RASION_ONTOP,
                                      BIA_RASIO_TOTAL,
                                      PPID_REF,
                                      PRODUCT_APPROVAL,
                                      PRODUK_ROW_NUM,
                                      CLOSE_FLAG,
                                      PAKET_FLAG,
                                      CREATED_BY,
                                      CREATION_DATE)
         SELECT   V_AUDIT_ID,
                  PROMO_PRODUK_ID,
                  PROPOSAL_ID,
                  AMOUNT_PROMO,
                  PRODUCT_CATEGORY,
                  PRODUCT_CLASS,
                  PRODUCT_BRAND,
                  PRODUCT_EXT,
                  PRODUCT_PACK,
                  DESCR,
                  MEKANISME,
                  EST_BUDGET_PROP,
                  REG_CUST_FLAG,
                  KODE_POSTING,
                  BUDGET_BY,
                  VALID_COMB,
                  ITEM_EXPENSE,
                  KODE_POSTING_MF,
                  EXCL_CUST_BY,
                  DISC_MF,
                  DISC_ON_TOP,
                  DISC_RASIO_MF,
                  DISC_RASIO_ON_TOP,
                  DISC_RASIO_TOTAL,
                  BRG_BONUS_MF,
                  BRG_BONUS_ON_TOP,
                  BRG_BONUS_RASIO_MF,
                  BRG_BONUS_RASIO_ON_TOP,
                  BRG_BONUS_RASIO_TOTAL,
                  BIA_MF,
                  BIA_ONTOP,
                  BIA_RASIO_MF,
                  BIA_RASION_ONTOP,
                  BIA_RASIO_TOTAL,
                  PPID_REF,
                  PRODUCT_APPROVAL,
                  PRODUK_ROW_NUM,
                  CLOSE_FLAG,
                  PAKET_FLAG,
                  CREATED_BY,
                  CREATION_DATE
           FROM   PROMO_PRODUK PP
          WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID
                  AND NOT EXISTS
                        (SELECT   1
                           FROM   AUDIT_PROMO_PRODUK APP
                          WHERE   APP.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND APP.AUDIT_ID = V_AUDIT_ID);

      -- INSERT PRODUK VARIANT AUDIT
      INSERT INTO AUDIT_PRODUK_VARIANT (PROMO_PRODUK_ID,
                                        PROD_VARIANT,
                                        VARIANT_DESC,
                                        AUDIT_ID)
         SELECT   PV.PROMO_PRODUK_ID,
                  PV.PROD_VARIANT,
                  PV.VARIANT_DESC,
                  V_AUDIT_ID
           FROM   PRODUK_VARIANT PV, PROMO_PRODUK PP
          WHERE   PV.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT PRODUK ITEM AUDIT
      INSERT INTO AUDIT_PRODUK_ITEM (PROMO_PRODUK_ID,
                                     PROD_ITEM,
                                     ITEM_DESC,
                                     AUDIT_ID)
         SELECT   PI.PROMO_PRODUK_ID,
                  PI.PROD_ITEM,
                  PI.ITEM_DESC,
                  V_AUDIT_ID
           FROM   PRODUK_ITEM PI, PROMO_PRODUK PP
          WHERE   PI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT TARGET AUDIT
      INSERT INTO AUDIT_TARGET (TARGET_ID,
                                PROMO_PRODUK_ID,
                                GROWTH_BY,
                                GROWTH_PERCENT,
                                PRICE,
                                QTY,
                                UOM,
                                TARGET_VALUE,
                                PRICE_BASED,
                                PRICE_LIST,
                                VALUE,
                                AVG_QTY,
                                AUDIT_ID)
         SELECT   TG.TARGET_ID,
                  TG.PROMO_PRODUK_ID,
                  TG.GROWTH_BY,
                  TG.GROWTH_PERCENT,
                  TG.PRICE,
                  TG.QTY,
                  TG.UOM,
                  TG.TARGET_VALUE,
                  TG.PRICE_BASED,
                  TG.PRICE_LIST,
                  TG.VALUE,
                  TG.AVG_QTY,
                  V_AUDIT_ID
           FROM   TARGET TG, PROMO_PRODUK PP
          WHERE   TG.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT BIAYA AUDIT
      INSERT INTO AUDIT_BIAYA (BIAYA_ID,
                               PROMO_PRODUK_ID,
                               DESCR,
                               BIAYA_YEARLY,
                               BIAYA_NON_YEARLY,
                               BIAYA_QTY,
                               BIAYA_UOM,
                               BIAYA_PRICE,
                               BIAYA_TOT_AMT,
                               AUDIT_ID)
         SELECT   BI.BIAYA_ID,
                  BI.PROMO_PRODUK_ID,
                  BI.DESCR,
                  BI.BIAYA_YEARLY,
                  BI.BIAYA_NON_YEARLY,
                  BI.BIAYA_QTY,
                  BI.BIAYA_UOM,
                  BI.BIAYA_PRICE,
                  BI.BIAYA_TOT_AMT,
                  V_AUDIT_ID
           FROM   BIAYA BI, PROMO_PRODUK PP
          WHERE   BI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT DISCOUNT AUDIT
      INSERT INTO AUDIT_DISCOUNT (DISCOUNT_ID,
                                  PROMO_PRODUK_ID,
                                  TIPE_PERHITUNGAN,
                                  UOM,
                                  QTY_FROM,
                                  QTY_TO,
                                  TIPE_POTONGAN,
                                  DISC_NON_YEARLY,
                                  DISC_YEARLY,
                                  KELIPATAN,
                                  AUDIT_ID)
         SELECT   DI.DISCOUNT_ID,
                  DI.PROMO_PRODUK_ID,
                  DI.TIPE_PERHITUNGAN,
                  DI.UOM,
                  DI.QTY_FROM,
                  DI.QTY_TO,
                  DI.TIPE_POTONGAN,
                  DI.DISC_NON_YEARLY,
                  DI.DISC_YEARLY,
                  DI.KELIPATAN,
                  V_AUDIT_ID
           FROM   DISCOUNT DI, PROMO_PRODUK PP
          WHERE   DI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT PROMO BONUS (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS (PROMO_BONUS_ID,
                                     PROMO_PRODUK_ID,
                                     PRODUCT_CATEGORY,
                                     PRODUCT_CLASS,
                                     PRODUCT_BRAND,
                                     PRODUCT_EXT,
                                     PRODUCT_PACK,
                                     UOM,
                                     QTY_FROM,
                                     TIPE_POTONGAN,
                                     VALUE_POTONGAN,
                                     DISC_NON_YEARLY,
                                     DISC_YEARLY,
                                     INPUT_PRICE_BY,
                                     PRICE_VAL,
                                     QTY_MUL_PRICE,
                                     AUDIT_ID)
         SELECT   PB.PROMO_BONUS_ID,
                  PB.PROMO_PRODUK_ID,
                  PB.PRODUCT_CATEGORY,
                  PB.PRODUCT_CLASS,
                  PB.PRODUCT_BRAND,
                  PB.PRODUCT_EXT,
                  PB.PRODUCT_PACK,
                  PB.UOM,
                  PB.QTY_FROM,
                  PB.TIPE_POTONGAN,
                  PB.VALUE_POTONGAN,
                  PB.DISC_NON_YEARLY,
                  PB.DISC_YEARLY,
                  PB.INPUT_PRICE_BY,
                  PB.PRICE_VAL,
                  PB.QTY_MUL_PRICE,
                  V_AUDIT_ID
           FROM   PROMO_BONUS PB, PROMO_PRODUK PP
          WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;
            
      -- INSERT PROMO BONUS - VARIANT (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS_VARIANT (PROMO_BONUS_ID,
                                     AUDIT_ID,
                                     PROD_VARIANT,
                                     VARIANT_DESC)
        SELECT PBV.PROMO_BONUS_ID,
               V_AUDIT_ID,
               PBV.PROD_VARIANT,
               PBV.VARIANT_DESC 
        FROM PROMO_BONUS_VARIANT PBV, PROMO_BONUS PB, PROMO_PRODUK PP
        WHERE PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID 
        AND PBV.PROMO_BONUS_ID = PB.PROMO_BONUS_ID
        AND PP.PROPOSAL_ID = V_NEW_PROP_ID;      
            
      -- INSERT PROMO BONUS - ITEM (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS_ITEM (PROMO_BONUS_ID,
                                     AUDIT_ID,
                                     PROD_ITEM,
                                     ITEM_DESC)
        SELECT PBI.PROMO_BONUS_ID,
               V_AUDIT_ID,
               PBI.PROD_ITEM,
               PBI.ITEM_DESC 
        FROM PROMO_BONUS_PROD_ITEM PBI, PROMO_BONUS PB, PROMO_PRODUK PP
        WHERE PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID 
        AND PBI.PROMO_BONUS_ID = PB.PROMO_BONUS_ID
        AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      COMMIT;
   END IF;
END;</code>
  <enabled>true</enabled>
  <events>
    <event>UPDATE</event>
  </events>
  <properties>
    <entry>
      <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
      <value class="java.lang.String">focuspp</value>
    </entry>
    <entry>
      <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
      <value class="oracle.javatools.db.ReferenceID">
        <name>FCS_NEW_DATA_AUDIT_UPDATE</name>
        <identifier class="java.math.BigDecimal">109967</identifier>
        <schemaName>FOCUSPP</schemaName>
        <type>TRIGGER</type>
      </value>
    </entry>
  </properties>
  <schema>
    <name>FOCUSPP</name>
  </schema>
  <source>TRIGGER FOCUSPP.FCS_NEW_DATA_AUDIT_UPDATE
AFTER UPDATE
ON FOCUSPP.DOC_APPROVAL 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE
   PRAGMA AUTONOMOUS_TRANSACTION;
   V_AUDIT_ID        NUMBER;
   V_PREV_AUDIT_ID   NUMBER;
   V_NEW_PROP_ID     NUMBER;
   V_IF_EXISTS       NUMBER;
   V_MODIFIED_BY     VARCHAR (30);
BEGIN
   -- CREATED BY   : MII - AHMADKU
   -- CREATE DATE  : 10-AGT-2018
   -- REASON       : DIGUNAKAN UNTUK INSERT KE TABLE DATA_AUDIT

   IF (:NEW.ACTION IS NOT NULL OR :NEW.ACTION &lt;&gt; &apos;FINISHED&apos;)
   THEN
      V_AUDIT_ID := AUDIT_PROPOSAL_SEQ.NEXTVAL;
      V_NEW_PROP_ID := :NEW.PROPOSAL_ID;
      V_MODIFIED_BY := :NEW.ACTION_TO;
      
      SELECT COUNT(1) INTO V_IF_EXISTS
        FROM   AUDIT_PROPOSAL PR
       WHERE   PR.MODIFIED_BY = V_MODIFIED_BY
               AND PR.PROPOSAL_ID = V_NEW_PROP_ID;

      IF V_IF_EXISTS &gt; 0 THEN 
          
          SELECT   NVL(PR.AUDIT_ID,0)
            INTO   V_PREV_AUDIT_ID
            FROM   AUDIT_PROPOSAL PR
           WHERE   PR.MODIFIED_BY = V_MODIFIED_BY
                   AND PR.PROPOSAL_ID = V_NEW_PROP_ID;


          -- DELETE PREVIOUS AUDIT DATA
          DELETE FROM   AUDIT_PROPOSAL PR
                WHERE   PR.PROPOSAL_ID = V_NEW_PROP_ID
                        AND PR.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PROMO_PRODUK PP
                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID
                        AND PP.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PRODUK_VARIANT PV
                WHERE   PV.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PV.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PRODUK_ITEM PI
                WHERE   PI.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PI.AUDIT_ID = V_AUDIT_ID;

          DELETE FROM   AUDIT_TARGET TG
                WHERE   TG.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND TG.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_BIAYA BI
                WHERE   BI.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND BI.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_DISCOUNT DS
                WHERE   DS.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND DS.AUDIT_ID = V_PREV_AUDIT_ID;

          DELETE FROM   AUDIT_PROMO_BONUS PB
                WHERE   PB.PROMO_PRODUK_ID IN
                              (SELECT   PP.PROMO_PRODUK_ID
                                 FROM   AUDIT_PROMO_PRODUK PP
                                WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PB.AUDIT_ID = V_PREV_AUDIT_ID;
          
          DELETE FROM   AUDIT_PROMO_BONUS_VARIANT PBV
                WHERE   PBV.PROMO_BONUS_ID IN
                              (SELECT   PB.PROMO_BONUS_ID
                                 FROM   AUDIT_PROMO_BONUS PB, AUDIT_PROMO_PRODUK PP
                                WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PBV.AUDIT_ID = V_PREV_AUDIT_ID;
          
          DELETE FROM   AUDIT_PROMO_BONUS_ITEM PBI
                WHERE   PBI.PROMO_BONUS_ID IN
                              (SELECT   PB.PROMO_BONUS_ID
                                 FROM   AUDIT_PROMO_BONUS PB, AUDIT_PROMO_PRODUK PP
                                WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND   PP.PROPOSAL_ID = V_NEW_PROP_ID)
                        AND PBI.AUDIT_ID = V_PREV_AUDIT_ID;
      END IF;

      -- INSERT PROPOSAL AUDIT
      INSERT INTO AUDIT_PROPOSAL (AUDIT_ID,
                                  PROPOSAL_ID,
                                  CREATED_BY,
                                  CREATION_DATE,
                                  MODIFIED_BY,
                                  MODIFIED_DATE,
                                  APPROVAL_ACTION)
         SELECT   V_AUDIT_ID,
                  :NEW.PROPOSAL_ID,
                  :NEW.CREATED_BY,
                  :NEW.CREATION_DATE,
                  :NEW.LAST_UPDATED_BY,
                  :NEW.LAST_UPDATE_DATE,
                  :NEW.ACTION
           FROM   DUAL;

      -- INSERT PROMO PRODUK AUDIT
      INSERT INTO AUDIT_PROMO_PRODUK (AUDIT_ID,
                                      PROMO_PRODUK_ID,
                                      PROPOSAL_ID,
                                      AMOUNT_PROMO,
                                      PRODUCT_CATEGORY,
                                      PRODUCT_CLASS,
                                      PRODUCT_BRAND,
                                      PRODUCT_EXT,
                                      PRODUCT_PACK,
                                      DESCR,
                                      MEKANISME,
                                      EST_BUDGET_PROP,
                                      REG_CUST_FLAG,
                                      KODE_POSTING,
                                      BUDGET_BY,
                                      VALID_COMB,
                                      ITEM_EXPENSE,
                                      KODE_POSTING_MF,
                                      EXCL_CUST_BY,
                                      DISC_MF,
                                      DISC_ON_TOP,
                                      DISC_RASIO_MF,
                                      DISC_RASIO_ON_TOP,
                                      DISC_RASIO_TOTAL,
                                      BRG_BONUS_MF,
                                      BRG_BONUS_ON_TOP,
                                      BRG_BONUS_RASIO_MF,
                                      BRG_BONUS_RASIO_ON_TOP,
                                      BRG_BONUS_RASIO_TOTAL,
                                      BIA_MF,
                                      BIA_ONTOP,
                                      BIA_RASIO_MF,
                                      BIA_RASION_ONTOP,
                                      BIA_RASIO_TOTAL,
                                      PPID_REF,
                                      PRODUCT_APPROVAL,
                                      PRODUK_ROW_NUM,
                                      CLOSE_FLAG,
                                      PAKET_FLAG,
                                      CREATED_BY,
                                      CREATION_DATE)
         SELECT   V_AUDIT_ID,
                  PROMO_PRODUK_ID,
                  PROPOSAL_ID,
                  AMOUNT_PROMO,
                  PRODUCT_CATEGORY,
                  PRODUCT_CLASS,
                  PRODUCT_BRAND,
                  PRODUCT_EXT,
                  PRODUCT_PACK,
                  DESCR,
                  MEKANISME,
                  EST_BUDGET_PROP,
                  REG_CUST_FLAG,
                  KODE_POSTING,
                  BUDGET_BY,
                  VALID_COMB,
                  ITEM_EXPENSE,
                  KODE_POSTING_MF,
                  EXCL_CUST_BY,
                  DISC_MF,
                  DISC_ON_TOP,
                  DISC_RASIO_MF,
                  DISC_RASIO_ON_TOP,
                  DISC_RASIO_TOTAL,
                  BRG_BONUS_MF,
                  BRG_BONUS_ON_TOP,
                  BRG_BONUS_RASIO_MF,
                  BRG_BONUS_RASIO_ON_TOP,
                  BRG_BONUS_RASIO_TOTAL,
                  BIA_MF,
                  BIA_ONTOP,
                  BIA_RASIO_MF,
                  BIA_RASION_ONTOP,
                  BIA_RASIO_TOTAL,
                  PPID_REF,
                  PRODUCT_APPROVAL,
                  PRODUK_ROW_NUM,
                  CLOSE_FLAG,
                  PAKET_FLAG,
                  CREATED_BY,
                  CREATION_DATE
           FROM   PROMO_PRODUK PP
          WHERE   PP.PROPOSAL_ID = V_NEW_PROP_ID
                  AND NOT EXISTS
                        (SELECT   1
                           FROM   AUDIT_PROMO_PRODUK APP
                          WHERE   APP.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                                  AND APP.AUDIT_ID = V_AUDIT_ID);

      -- INSERT PRODUK VARIANT AUDIT
      INSERT INTO AUDIT_PRODUK_VARIANT (PROMO_PRODUK_ID,
                                        PROD_VARIANT,
                                        VARIANT_DESC,
                                        AUDIT_ID)
         SELECT   PV.PROMO_PRODUK_ID,
                  PV.PROD_VARIANT,
                  PV.VARIANT_DESC,
                  V_AUDIT_ID
           FROM   PRODUK_VARIANT PV, PROMO_PRODUK PP
          WHERE   PV.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT PRODUK ITEM AUDIT
      INSERT INTO AUDIT_PRODUK_ITEM (PROMO_PRODUK_ID,
                                     PROD_ITEM,
                                     ITEM_DESC,
                                     AUDIT_ID)
         SELECT   PI.PROMO_PRODUK_ID,
                  PI.PROD_ITEM,
                  PI.ITEM_DESC,
                  V_AUDIT_ID
           FROM   PRODUK_ITEM PI, PROMO_PRODUK PP
          WHERE   PI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT TARGET AUDIT
      INSERT INTO AUDIT_TARGET (TARGET_ID,
                                PROMO_PRODUK_ID,
                                GROWTH_BY,
                                GROWTH_PERCENT,
                                PRICE,
                                QTY,
                                UOM,
                                TARGET_VALUE,
                                PRICE_BASED,
                                PRICE_LIST,
                                VALUE,
                                AVG_QTY,
                                AUDIT_ID)
         SELECT   TG.TARGET_ID,
                  TG.PROMO_PRODUK_ID,
                  TG.GROWTH_BY,
                  TG.GROWTH_PERCENT,
                  TG.PRICE,
                  TG.QTY,
                  TG.UOM,
                  TG.TARGET_VALUE,
                  TG.PRICE_BASED,
                  TG.PRICE_LIST,
                  TG.VALUE,
                  TG.AVG_QTY,
                  V_AUDIT_ID
           FROM   TARGET TG, PROMO_PRODUK PP
          WHERE   TG.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT BIAYA AUDIT
      INSERT INTO AUDIT_BIAYA (BIAYA_ID,
                               PROMO_PRODUK_ID,
                               DESCR,
                               BIAYA_YEARLY,
                               BIAYA_NON_YEARLY,
                               BIAYA_QTY,
                               BIAYA_UOM,
                               BIAYA_PRICE,
                               BIAYA_TOT_AMT,
                               AUDIT_ID)
         SELECT   BI.BIAYA_ID,
                  BI.PROMO_PRODUK_ID,
                  BI.DESCR,
                  BI.BIAYA_YEARLY,
                  BI.BIAYA_NON_YEARLY,
                  BI.BIAYA_QTY,
                  BI.BIAYA_UOM,
                  BI.BIAYA_PRICE,
                  BI.BIAYA_TOT_AMT,
                  V_AUDIT_ID
           FROM   BIAYA BI, PROMO_PRODUK PP
          WHERE   BI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT DISCOUNT AUDIT
      INSERT INTO AUDIT_DISCOUNT (DISCOUNT_ID,
                                  PROMO_PRODUK_ID,
                                  TIPE_PERHITUNGAN,
                                  UOM,
                                  QTY_FROM,
                                  QTY_TO,
                                  TIPE_POTONGAN,
                                  DISC_NON_YEARLY,
                                  DISC_YEARLY,
                                  KELIPATAN,
                                  AUDIT_ID)
         SELECT   DI.DISCOUNT_ID,
                  DI.PROMO_PRODUK_ID,
                  DI.TIPE_PERHITUNGAN,
                  DI.UOM,
                  DI.QTY_FROM,
                  DI.QTY_TO,
                  DI.TIPE_POTONGAN,
                  DI.DISC_NON_YEARLY,
                  DI.DISC_YEARLY,
                  DI.KELIPATAN,
                  V_AUDIT_ID
           FROM   DISCOUNT DI, PROMO_PRODUK PP
          WHERE   DI.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      -- INSERT PROMO BONUS (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS (PROMO_BONUS_ID,
                                     PROMO_PRODUK_ID,
                                     PRODUCT_CATEGORY,
                                     PRODUCT_CLASS,
                                     PRODUCT_BRAND,
                                     PRODUCT_EXT,
                                     PRODUCT_PACK,
                                     UOM,
                                     QTY_FROM,
                                     TIPE_POTONGAN,
                                     VALUE_POTONGAN,
                                     DISC_NON_YEARLY,
                                     DISC_YEARLY,
                                     INPUT_PRICE_BY,
                                     PRICE_VAL,
                                     QTY_MUL_PRICE,
                                     AUDIT_ID)
         SELECT   PB.PROMO_BONUS_ID,
                  PB.PROMO_PRODUK_ID,
                  PB.PRODUCT_CATEGORY,
                  PB.PRODUCT_CLASS,
                  PB.PRODUCT_BRAND,
                  PB.PRODUCT_EXT,
                  PB.PRODUCT_PACK,
                  PB.UOM,
                  PB.QTY_FROM,
                  PB.TIPE_POTONGAN,
                  PB.VALUE_POTONGAN,
                  PB.DISC_NON_YEARLY,
                  PB.DISC_YEARLY,
                  PB.INPUT_PRICE_BY,
                  PB.PRICE_VAL,
                  PB.QTY_MUL_PRICE,
                  V_AUDIT_ID
           FROM   PROMO_BONUS PB, PROMO_PRODUK PP
          WHERE   PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID
                  AND PP.PROPOSAL_ID = V_NEW_PROP_ID;
            
      -- INSERT PROMO BONUS - VARIANT (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS_VARIANT (PROMO_BONUS_ID,
                                     AUDIT_ID,
                                     PROD_VARIANT,
                                     VARIANT_DESC)
        SELECT PBV.PROMO_BONUS_ID,
               V_AUDIT_ID,
               PBV.PROD_VARIANT,
               PBV.VARIANT_DESC 
        FROM PROMO_BONUS_VARIANT PBV, PROMO_BONUS PB, PROMO_PRODUK PP
        WHERE PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID 
        AND PBV.PROMO_BONUS_ID = PB.PROMO_BONUS_ID
        AND PP.PROPOSAL_ID = V_NEW_PROP_ID;      
            
      -- INSERT PROMO BONUS - ITEM (PROMO BARANG) AUDIT
      INSERT INTO AUDIT_PROMO_BONUS_ITEM (PROMO_BONUS_ID,
                                     AUDIT_ID,
                                     PROD_ITEM,
                                     ITEM_DESC)
        SELECT PBI.PROMO_BONUS_ID,
               V_AUDIT_ID,
               PBI.PROD_ITEM,
               PBI.ITEM_DESC 
        FROM PROMO_BONUS_PROD_ITEM PBI, PROMO_BONUS PB, PROMO_PRODUK PP
        WHERE PB.PROMO_PRODUK_ID = PP.PROMO_PRODUK_ID 
        AND PBI.PROMO_BONUS_ID = PB.PROMO_BONUS_ID
        AND PP.PROPOSAL_ID = V_NEW_PROP_ID;

      COMMIT;
   END IF;
END; 
</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.ReferenceID">
    <name>DOC_APPROVAL</name>
    <identifier class="java.math.BigDecimal">110955</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>AFTER</timing>
</trigger>
