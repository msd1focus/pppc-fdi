<?xml version = '1.0' encoding = 'UTF-8'?>
<trigger xmlns="http://xmlns.oracle.com/jdeveloper/1111/offlinedb">
  <ID class="oracle.javatools.db.IdentifierBasedID">
    <identifier class="java.lang.String">2ed53f5f-a735-48b9-8d33-dc315b69a635</identifier>
  </ID>
  <name>FCS_UPDATE_END_DATE_UPD</name>
  <baseType>TABLE</baseType>
  <code>DECLARE
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    -- CREATED BY   : MII - SODIQ MR.
    -- CREATE DATE  : 13-APR-2018
    -- REASON       : DIGUNAKAN UNTUK UPDATE END DATE KETIKA ADDENDUM DI CANCEL dan UPDATE USED BUDGET
    
    IF :OLD.ADDENDUM_KE IS NOT NULL AND :NEW.STATUS = &apos;CANCELED&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; AND :OLD.MEKANISME_PENAGIHAN = &apos;ONINVOICE&apos; THEN
        
        UPDATE FOCUSPP.PROD_BUDGET_BY PB
        SET PB.STATUS = &apos;N&apos;
        WHERE 1=1
        AND EXISTS
        (
            SELECT 1
            FROM FOCUSPP.PROMO_PRODUK PP
            WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
            AND PP.PPID_REF = PB.PROMO_PRODUK_ID
        );
        
        COMMIT;
        
        UPDATE BUDGET_CUSTOMER BC
        SET BC.BUDGET_AS_TO_DATE_USED = BC.BUDGET_AS_TO_DATE_USED + NVL(
                                                                    (
                                                                        SELECT SUM(NVL(PB.AMOUNT,0))
                                                                        FROM FOCUSPP.PROD_BUDGET_BY PB,
                                                                             FOCUSPP.PROMO_PRODUK PP
                                                                        WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
                                                                        AND PP.PPID_REF = PB.PROMO_PRODUK_ID
                                                                        AND PB.BUDGET_CUST_ID = BC.BUDGET_CUSTOMER_ID         
                                                                    ),0)
        WHERE 1=1
        AND EXISTS
        (
            SELECT 1
            FROM FOCUSPP.PROD_BUDGET_BY PB,
                 FOCUSPP.PROMO_PRODUK PP
            WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
            AND PP.PPID_REF = PB.PROMO_PRODUK_ID
            AND PB.BUDGET_CUST_ID = BC.BUDGET_CUSTOMER_ID
        );
        
        COMMIT;
        
    END IF;
    

    IF :OLD.ADDENDUM_KE IS NOT NULL AND :NEW.STATUS = &apos;CANCELED&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; THEN
        
        UPDATE FOCUSPP.PROPOSAL PC
        SET PC.PERIODE_PROG_TO = (
                                    SELECT PC3.PP_PROG_TO_ORI
                                    FROM FOCUSPP.PROPOSAL PC3
                                    WHERE PC3.CONFIRM_NO = :OLD.CONFIRM_NO
                                    AND NVL(PC3.ADDENDUM_KE,0) = 
                                    (
                                        SELECT MAX(NVL(PC2.ADDENDUM_KE,0))
                                        FROM FOCUSPP.PROPOSAL PC2
                                        WHERE PC2.CONFIRM_NO = :OLD.CONFIRM_NO
                                        AND PC2.STATUS = &apos;ACTIVE&apos;
                                        AND NVL(PC2.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
                                    )
                                 )
            , PC.PROG_DAYS = (
                                SELECT (PC3.PP_PROG_TO_ORI - PC3.PP_PROG_FROM_ORI)+1
                                FROM FOCUSPP.PROPOSAL PC3
                                WHERE PC3.CONFIRM_NO = :OLD.CONFIRM_NO
                                AND NVL(PC3.ADDENDUM_KE,0) = 
                                (
                                    SELECT MAX(NVL(PC2.ADDENDUM_KE,0))
                                    FROM FOCUSPP.PROPOSAL PC2
                                    WHERE PC2.CONFIRM_NO = :OLD.CONFIRM_NO
                                    AND PC2.STATUS = &apos;ACTIVE&apos;
                                    AND NVL(PC2.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
                                )
                             )
        WHERE PC.CONFIRM_NO = :OLD.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :OLD.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
        );
        
        COMMIT;
        
    END IF;
    
    IF NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; NVL(:OLD.ADDENDUM_KE,&apos;0&apos;) AND NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; 0 AND :NEW.STATUS = &apos;ACTIVE&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; THEN
    
        UPDATE FOCUSPP.PROPOSAL PC
        SET PC.PERIODE_PROG_TO = :NEW.PERIODE_PROG_FROM-1
          , PC.PROG_DAYS = (:NEW.PERIODE_PROG_FROM - PC.PERIODE_PROG_FROM)
        WHERE PC.CONFIRM_NO = :NEW.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :NEW.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :NEW.ADDENDUM_KE
        );
        
        COMMIT;
        
    END IF;
    
    IF NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; NVL(:OLD.ADDENDUM_KE,&apos;0&apos;) AND NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; 0 AND :NEW.STATUS = &apos;ACTIVE&apos; AND :OLD.DISCOUNT_TYPE = &apos;BIAYA&apos; THEN
        UPDATE FOCUSPP.PROPOSAL PC
        SET STATUS = &apos;CANCELED&apos;
        WHERE PC.CONFIRM_NO = :NEW.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :NEW.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :NEW.ADDENDUM_KE
        );
        
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
        <name>FCS_UPDATE_END_DATE_UPD</name>
        <identifier class="java.math.BigDecimal">105230</identifier>
        <schemaName>FOCUSPP</schemaName>
        <type>TRIGGER</type>
      </value>
    </entry>
  </properties>
  <schema>
    <name>FOCUSPP</name>
  </schema>
  <source>TRIGGER FOCUSPP.FCS_UPDATE_END_DATE_UPD
AFTER UPDATE ON &quot;FOCUSPP&quot;.&quot;PROPOSAL&quot;
FOR EACH ROW
DECLARE
    PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    -- CREATED BY   : MII - SODIQ MR.
    -- CREATE DATE  : 13-APR-2018
    -- REASON       : DIGUNAKAN UNTUK UPDATE END DATE KETIKA ADDENDUM DI CANCEL dan UPDATE USED BUDGET
    
    IF :OLD.ADDENDUM_KE IS NOT NULL AND :NEW.STATUS = &apos;CANCELED&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; AND :OLD.MEKANISME_PENAGIHAN = &apos;ONINVOICE&apos; THEN
        
        UPDATE FOCUSPP.PROD_BUDGET_BY PB
        SET PB.STATUS = &apos;N&apos;
        WHERE 1=1
        AND EXISTS
        (
            SELECT 1
            FROM FOCUSPP.PROMO_PRODUK PP
            WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
            AND PP.PPID_REF = PB.PROMO_PRODUK_ID
        );
        
        COMMIT;
        
        UPDATE BUDGET_CUSTOMER BC
        SET BC.BUDGET_AS_TO_DATE_USED = BC.BUDGET_AS_TO_DATE_USED + NVL(
                                                                    (
                                                                        SELECT SUM(NVL(PB.AMOUNT,0))
                                                                        FROM FOCUSPP.PROD_BUDGET_BY PB,
                                                                             FOCUSPP.PROMO_PRODUK PP
                                                                        WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
                                                                        AND PP.PPID_REF = PB.PROMO_PRODUK_ID
                                                                        AND PB.BUDGET_CUST_ID = BC.BUDGET_CUSTOMER_ID         
                                                                    ),0)
        WHERE 1=1
        AND EXISTS
        (
            SELECT 1
            FROM FOCUSPP.PROD_BUDGET_BY PB,
                 FOCUSPP.PROMO_PRODUK PP
            WHERE PP.PROPOSAL_ID = :OLD.PROPOSAL_ID
            AND PP.PPID_REF = PB.PROMO_PRODUK_ID
            AND PB.BUDGET_CUST_ID = BC.BUDGET_CUSTOMER_ID
        );
        
        COMMIT;
        
    END IF;
    

    IF :OLD.ADDENDUM_KE IS NOT NULL AND :NEW.STATUS = &apos;CANCELED&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; THEN
        
        UPDATE FOCUSPP.PROPOSAL PC
        SET PC.PERIODE_PROG_TO = (
                                    SELECT PC3.PP_PROG_TO_ORI
                                    FROM FOCUSPP.PROPOSAL PC3
                                    WHERE PC3.CONFIRM_NO = :OLD.CONFIRM_NO
                                    AND NVL(PC3.ADDENDUM_KE,0) = 
                                    (
                                        SELECT MAX(NVL(PC2.ADDENDUM_KE,0))
                                        FROM FOCUSPP.PROPOSAL PC2
                                        WHERE PC2.CONFIRM_NO = :OLD.CONFIRM_NO
                                        AND PC2.STATUS = &apos;ACTIVE&apos;
                                        AND NVL(PC2.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
                                    )
                                 )
            , PC.PROG_DAYS = (
                                SELECT (PC3.PP_PROG_TO_ORI - PC3.PP_PROG_FROM_ORI)+1
                                FROM FOCUSPP.PROPOSAL PC3
                                WHERE PC3.CONFIRM_NO = :OLD.CONFIRM_NO
                                AND NVL(PC3.ADDENDUM_KE,0) = 
                                (
                                    SELECT MAX(NVL(PC2.ADDENDUM_KE,0))
                                    FROM FOCUSPP.PROPOSAL PC2
                                    WHERE PC2.CONFIRM_NO = :OLD.CONFIRM_NO
                                    AND PC2.STATUS = &apos;ACTIVE&apos;
                                    AND NVL(PC2.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
                                )
                             )
        WHERE PC.CONFIRM_NO = :OLD.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :OLD.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :OLD.ADDENDUM_KE
        );
        
        COMMIT;
        
    END IF;
    
    IF NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; NVL(:OLD.ADDENDUM_KE,&apos;0&apos;) AND NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; 0 AND :NEW.STATUS = &apos;ACTIVE&apos; AND :OLD.DISCOUNT_TYPE &lt;&gt; &apos;BIAYA&apos; THEN
    
        UPDATE FOCUSPP.PROPOSAL PC
        SET PC.PERIODE_PROG_TO = :NEW.PERIODE_PROG_FROM-1
          , PC.PROG_DAYS = (:NEW.PERIODE_PROG_FROM - PC.PERIODE_PROG_FROM)
        WHERE PC.CONFIRM_NO = :NEW.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :NEW.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :NEW.ADDENDUM_KE
        );
        
        COMMIT;
        
    END IF;
    
    IF NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; NVL(:OLD.ADDENDUM_KE,&apos;0&apos;) AND NVL(:NEW.ADDENDUM_KE,&apos;0&apos;) &gt; 0 AND :NEW.STATUS = &apos;ACTIVE&apos; AND :OLD.DISCOUNT_TYPE = &apos;BIAYA&apos; THEN
        UPDATE FOCUSPP.PROPOSAL PC
        SET STATUS = &apos;CANCELED&apos;
        WHERE PC.CONFIRM_NO = :NEW.CONFIRM_NO
        AND NVL(PC.ADDENDUM_KE,0) =
        (
            SELECT MAX(NVL(PC1.ADDENDUM_KE,0))
            FROM FOCUSPP.PROPOSAL PC1
            WHERE PC1.CONFIRM_NO = :NEW.CONFIRM_NO
            AND PC1.STATUS = &apos;ACTIVE&apos;
            AND NVL(PC1.ADDENDUM_KE,0) &lt; :NEW.ADDENDUM_KE
        );
        
        COMMIT;
        
    END IF;
    
    
END; 
</source>
  <statementLevel>false</statementLevel>
  <tableID class="oracle.javatools.db.ReferenceID">
    <name>PROPOSAL</name>
    <identifier class="java.math.BigDecimal">111021</identifier>
    <schemaName>FOCUSPP</schemaName>
    <type>TABLE</type>
  </tableID>
  <timing>AFTER</timing>
</trigger>
