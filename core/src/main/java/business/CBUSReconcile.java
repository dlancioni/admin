/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import entity.CENTData;
import entity.CENTException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import persistence.CDALReconcile;

/**
 *
 * @author David
 */
public class CBUSReconcile extends CBUS
{
    public CBUSReconcile(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }

    /*
     * Constants related to recon proess
     */    
    private final int COMPARE_TYPE_KEY = 1;
    private final int COMPARE_TYPE_CRITERIA = 2;

    private String FIELD_ID_FIELD = "";
    private String FIELD_ID_FIELD_TYPE = "";
    private String FIELD_ID_OPERATOR = "";
    private String FIELD_TOLERANCE_VALUE = "";
    private String FIELD_TOLERANCE_TYPE = "";    
    
    /*
     * Constants to help processing the matches
     */
    private final String FIELD_MATCH_DIFF = "_text_3";

    /*
     * Catalogs related to the reconciliations process
     */    
    private List<CENTData> ListCENTCatalogReconcile1 = null;
    private List<CENTData> ListCENTCatalogStep1 = null;
    private List<CENTData> ListCENTCatalogStepRule1 = null;
    private List<CENTData> ListCENTCatalogStepRuleField1 = null;
    private List<CENTData> ListCENTCatalogMatch1 = null;
    private List<CENTData> ListCENTCatalogMatchItem1 = null;
    private List<CENTData> ListCENTCatalogTransaction1 = null;        

    
    public void Reconciliation(List<CENTData> ListCENTReconcile1) throws CENTException, SQLException, Exception
    {               
        boolean boolMissing = true;        

        long x = 0;
        long y = 0;
        long j = 0;

        int intMin = 0;
        int intMax = 0;            
        int intNextMatchId = 0;    
        int intPageSize = 500;
        int intIdArea = 0;
        int intIdProcess = 0;    
        int intIdReconcile = 0;      
        int intIdStep = 0;  
        int intIdRule = 0;    
        int intIdTransaction = 0;            
        int intIdType = 0;
        int intIdResult = 0;
        int intIdView1 = 0;
        int intIdView2 = 0;
        int intIdMatchAs = 0;
        int intRowsAffected = 0;
        int intCountRowsToReconcile = 0;
        int intFieldId = 0;
        
        String strCode1 = "";
        String strCode2 = "";
        
        String strReconcile = "";
        String strStep = "";
        String strRule = "";
        
        CENTData CENTFilter1 = null;
        CBUSCore CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
        CDALReconcile CDALReconcile1 = new CDALReconcile(this.GetConnection(), this.GetSession());
        
        List<String> ListPage1 = new ArrayList<String>();
        List<String> ListFieldKey1 = new ArrayList<String>();        
        
        List<CENTData> ListCENTReconcile2 = null;
        List<CENTData> ListCENTStep1 = null;
        List<CENTData> ListCENTRule1 = null;
        List<CENTData> ListCENTField1 = null;                    
        
        List<String> ListFields1 = new ArrayList<String>();
        
        ResultSet ResultSet1 = null;
        ResultSet ResultSet2 = null;

        try
        {            
            /*
             * Initialize important objects
             */            
            ListCENTCatalogReconcile1 = CBUSCore1.GetCatalog(TRN_RECONCILIATION);
            ListCENTCatalogStep1 = CBUSCore1.GetCatalog(TRN_RECONCILIATION_STEP);
            ListCENTCatalogStepRule1 = CBUSCore1.GetCatalog(TRN_RECONCILIATION_STEP_RULE);
            ListCENTCatalogStepRuleField1 = CBUSCore1.GetCatalog(TRN_RECONCILIATION_STEP_RULE_FIELD);            
            ListCENTCatalogMatch1 = CBUSCore1.GetCatalog(TRN_MATCH);
            ListCENTCatalogMatchItem1 = CBUSCore1.GetCatalog(TRN_MATCH_ITEM);            
            
            /*
             * Start processing the reconciliations, the list of it is inside the same transactions, all or nothing
             */
            for (CENTData CENTReconcile1 : ListCENTReconcile1)
            {
                /*
                 * Keep the unique id process
                 */                
                intIdProcess = CENTReconcile1.GetInt(SYSTEM_FIELD_ID_PROCESS);                

                /*
                 * Create temporary table to process data
                 */
                CDALReconcile1.CreateTemporaryTable();

                /*
                 * Create index
                 */
                CDALReconcile1.CreateIndex();                
                
                /*
                 * Get the current reconcile details
                 */            
                CBUSCore1.SetIdTransaction(TRN_RECONCILIATION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTReconcile2 = CBUSCore1.GetList(CENTReconcile1);

                if (ListCENTReconcile2.isEmpty())
                {
                    throw new CENTException("EXCEPTION_MISSING_RECONCILE", "Id: " + CENTReconcile1.GetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcile1)));
                }
                
                CENTReconcile1 = ListCENTReconcile2.get(0);   
                intIdReconcile = CENTReconcile1.GetInt(GetFieldObject("id", ListCENTCatalogReconcile1));
                strReconcile = CENTReconcile1.GetText(GetFieldObject("name", ListCENTCatalogReconcile1));
                intIdArea = CENTReconcile1.GetInt(GetFieldObject("id_area", ListCENTCatalogReconcile1));                
                intIdTransaction = CENTReconcile1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogReconcile1));
                intIdType = CENTReconcile1.GetInt(GetFieldObject("id_type", ListCENTCatalogReconcile1));
                intIdResult = CENTReconcile1.GetInt(GetFieldObject("id_result", ListCENTCatalogReconcile1));
                intIdView1 = CENTReconcile1.GetInt(GetFieldObject("id_view_1", ListCENTCatalogReconcile1));
                intIdView2 = CENTReconcile1.GetInt(GetFieldObject("id_view_2", ListCENTCatalogReconcile1));   
                ListCENTCatalogTransaction1 = CBUSCore1.GetCatalog(intIdTransaction);                
                
                /*
                 * 1/2 line or columns, decide if the differences will be showed in the same line or not
                 */                
                intIdMatchAs = Integer.valueOf(this.GetPropertieValue("MATCH_AS"));
                
                /*
                 * Get the list of steps
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id", ListCENTCatalogReconcile1), intIdReconcile);
                CENTFilter1.SetInt(GetFieldObject("id_enabled", ListCENTCatalogStep1), Yes);
                CBUSCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);                
                ListCENTStep1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTStep1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_MISSING_RECONCILE_STEP", "Id: " + intIdReconcile);
                }

                /*
                 * Initialize information for each side related to the reconciliation
                 */

                for (CENTData CENTStep1 : ListCENTStep1)
                {
                    CENTStep1 = ListCENTStep1.get(0);
                    intIdStep = CENTStep1.GetInt(GetFieldObject("id", ListCENTCatalogStep1)); 
                    strStep = CENTStep1.GetText(GetFieldObject("name", ListCENTCatalogStep1)); 

                    /*
                     * Import data side 1
                     */
                    strCode1 = CENTStep1.GetText(this.GetFieldObject("code_1", ListCENTCatalogStep1));
                    intRowsAffected = CDALReconcile1.GetStepData(intIdProcess, intIdTransaction, strCode1, intIdView1);
                    intCountRowsToReconcile += intRowsAffected;                    

                    /*
                     * Import data side 2
                     */
                    strCode2 = CENTStep1.GetText(this.GetFieldObject("code_2", ListCENTCatalogStep1));
                    intRowsAffected = CDALReconcile1.GetStepData(intIdProcess, intIdTransaction, strCode2, intIdView2);
                    intCountRowsToReconcile += intRowsAffected;                    

                    /*
                     * According to the sides, decide the number of loops
                     */
                    strCode1 = CENTStep1.GetText(this.GetFieldObject("code_1", ListCENTCatalogStep1));
                    strCode2 = CENTStep1.GetText(this.GetFieldObject("code_2", ListCENTCatalogStep1));                    

                    /*
                     * Get the list of rules
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogStepRule1), intIdReconcile);
                    CENTFilter1.SetInt(GetFieldObject("id_step", ListCENTCatalogStepRule1), intIdStep);
                    CENTFilter1.SetInt(GetFieldObject("id_enabled", ListCENTCatalogStepRule1), Yes);
                    CBUSCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
                    CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTRule1 = CBUSCore1.GetList(CENTFilter1);                    

                    if (ListCENTRule1.isEmpty())
                    {
                        throw new CENTException("EXCEPTION_MISSING_RECONCILE_STEP_RULE", "Id: " + intIdReconcile);
                    }

                    /*
                     * For each rule
                     */
                    for (CENTData CENTRule1 : ListCENTRule1)
                    {                                
                        if (CENTRule1.GetInt(GetFieldObject("id_enabled", ListCENTCatalogStepRule1)) == Yes)
                        {
                            intIdRule = CENTRule1.GetInt(GetFieldObject("id", ListCENTCatalogStepRule1));                    
                            strRule = CENTRule1.GetText(GetFieldObject("name", ListCENTCatalogStepRule1));
                            
                            /*
                             * Get the list of rule specification (fields)
                             */
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogStepRuleField1), intIdReconcile);
                            CENTFilter1.SetInt(GetFieldObject("id_step", ListCENTCatalogStepRuleField1), intIdStep);
                            CENTFilter1.SetInt(GetFieldObject("id_rule", ListCENTCatalogStepRuleField1), intIdRule);
                            CENTFilter1.SetInt(GetFieldObject("id_enabled", ListCENTCatalogStepRuleField1), Yes);
                            CBUSCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
                            CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                            ListCENTField1 = CBUSCore1.GetList(CENTFilter1);                        

                            if (ListCENTField1.isEmpty())
                            {
                                throw new CENTException("EXCEPTION_MISSING_RECONCILE_STEP_RULE_FIELD", "Id: " + intIdReconcile);
                            }

                            /*
                             * Reconciliation key is mandatory
                             */
                            for (CENTData CENTField1 : ListCENTField1)
                            {                                                                
                                if (CENTField1.GetInt(GetFieldObject("id_type", ListCENTCatalogStepRuleField1)) == KEY_SEARCH)
                                {
                                    boolMissing = false;
                                    break;
                                }                                
                            }

                            if (boolMissing)
                            {
                                throw new CENTException("EXCEPTION_MISSING_MATCH_KEY", "Id: " + intIdReconcile);                                    
                            }                                
                            
                            /*
                             * Keep list of fields to be reconciled
                             */            
                            ListFields1 = new ArrayList<String>();
                            
                            for (CENTData CENTField1 : ListCENTField1)
                            {                                
                                ListFields1.add(GetFieldObject(CENTField1.GetInt(GetFieldObject("id_field", ListCENTCatalogStepRuleField1)), ListCENTCatalogTransaction1));
                            }

                            /*
                             * Keep field information, to speed up performance
                             */            
                            FIELD_ID_FIELD = GetFieldObject("id_field", ListCENTCatalogStepRuleField1);
                            FIELD_ID_FIELD_TYPE = GetFieldObject("id_type", ListCENTCatalogStepRuleField1);
                            FIELD_ID_OPERATOR = GetFieldObject("id_operator", ListCENTCatalogStepRuleField1);
                            FIELD_TOLERANCE_VALUE = GetFieldObject("tolerance_value", ListCENTCatalogStepRuleField1);
                            FIELD_TOLERANCE_TYPE = GetFieldObject("id_tolerance_type", ListCENTCatalogStepRuleField1);   

                            /*
                             * Isolate the fields key according to the rule
                             */
                            ListFieldKey1 = new ArrayList<>();
                            
                            for (CENTData CENTField1 : ListCENTField1)
                            {
                                if (CENTField1.GetInt(GetFieldObject("id_type", ListCENTCatalogStepRuleField1)) == KEY_SEARCH)
                                {
                                    intFieldId = CENTField1.GetInt(GetFieldObject("id_field", ListCENTCatalogStepRuleField1));

                                    for (CENTData CENTField2 : ListCENTCatalogTransaction1)
                                    {
                                        if (CENTField2.GetInt(SYSTEM_FIELD_ID) == intFieldId)
                                        {
                                            ListFieldKey1.add(CENTField2.GetText(FIELD_OBJECT));
                                        }
                                    }                    
                                }
                            }                            
                            
                            /*
                             * Match the items (Matched only)
                             */
                            intRowsAffected = CDALReconcile1.Match(intIdProcess, intIdReconcile, intIdStep, intIdRule, intIdTransaction, MATCH_STATUS_MATCHED, strCode1, strCode2, ListCENTField1, ListFieldKey1, strReconcile, strStep, strRule);
                            System.out.println("intRowsAffected: " + intRowsAffected);
                            
                        } // end rule enabled

                    } // rule list

                } // End step
                
                /*
                 * Match the items (Orphan Only)
                 */
                intRowsAffected = CDALReconcile1.Match(intIdProcess, intIdReconcile, intIdStep, intIdRule, intIdTransaction, MATCH_STATUS_ORFAN, strCode1, strCode2, ListCENTField1, ListFieldKey1, strReconcile, strStep, strRule);
                System.out.println("intRowsAffected: " + intRowsAffected);
                                    
                /*
                 * 1:M and M:M must group the records
                 */
                switch (intIdType)
                {
                    case RECONCILE_1_1:                    
                        
                        /*
                         * Order the matched records
                         */
                        intRowsAffected = CDALReconcile1.OrderData(intIdProcess, intIdTransaction, strCode1, intIdView1, ListFieldKey1);
                        intRowsAffected = CDALReconcile1.OrderData(intIdProcess, intIdTransaction, strCode2, intIdView2, ListFieldKey1);
                        
                        /*
                         * Calculate the pages (one side is enough)
                         */                
                        intMin = 1;
                        intMax = intRowsAffected;
                        ListPage1 = this.GetPages(intMin, intMax, intPageSize);
                        
                        break;
                    
                    case RECONCILE_1_M:

                        /*
                         * Group and order the matched records
                         */
                        intRowsAffected = CDALReconcile1.GroupData(intIdProcess, intIdTransaction, strCode1, intIdView1, ListFieldKey1);
                        intRowsAffected = CDALReconcile1.GroupData(intIdProcess, intIdTransaction, strCode2, intIdView2, ListFieldKey1);
                        
                        /*
                         * Calculate the pages (one side is enough)
                         */                
                        intMin = 1;
                        intMax = intRowsAffected;
                        ListPage1 = this.GetPages(intMin, intMax, intPageSize);
                        
                        break;
                                        
                    case RECONCILE_M_M:

                        /*
                         * Group and order the matched records
                         */
                        intRowsAffected = CDALReconcile1.GroupData(intIdProcess, intIdTransaction, strCode1, intIdView1, ListFieldKey1);                        
                        intRowsAffected = CDALReconcile1.GroupData(intIdProcess, intIdTransaction, strCode2, intIdView2, ListFieldKey1);                                                
                        
                        /*
                         * Calculate the pages (one side is enough)
                         */                
                        intMin = 1;
                        intMax = intRowsAffected;
                        ListPage1 = this.GetPages(intMin, intMax, intPageSize);
                        
                        break;
                }
                
                /*
                 * Start the next match as the last match
                 */                
                intNextMatchId = CDALReconcile1.GetLastMatchId();
                
                /*
                 * Relegate the matched items to proposed when necessary
                 */
                for (String strPage1 : ListPage1)
                {                    
                    /*
                     * Get data to compare
                     */
                    ResultSet1 = CDALReconcile1.GetData(intIdProcess, intIdTransaction, strPage1, strCode1, intIdView1, intIdRule, intIdType, ListFieldKey1);
                    ResultSet2 = CDALReconcile1.GetData(intIdProcess, intIdTransaction, strPage1, strCode2, intIdView2, intIdRule, intIdType, ListFieldKey1);

                    /*
                     * Compare the records and relegate to proposed when necessary
                     */                    
                    while (ResultSet1.next())
                    {
                        x++;
                        while (ResultSet2.next())
                        {
                            y++;
                            SetMatchedAsProposed(intIdTransaction, intNextMatchId, ListCENTField1, ResultSet1, ResultSet2, intIdMatchAs);
                            break;
                        }
                        
                        System.out.println("Comparing line: " + x);
                    }
                    
                    /*
                     * Close resultset
                     */
                    ResultSet1.close();
                    ResultSet2.close();
                }
                
                /*
                 * Delete deprecated matchs caused by multiple rules
                 */                
                intRowsAffected = CDALReconcile1.SaveMatchAndMatchItem(intIdProcess, intIdArea, intIdResult, ListCENTCatalogMatch1, ListCENTCatalogMatchItem1);
                
                /*
                 * Update reconciliation area
                 */
                intRowsAffected = CDALReconcile1.UpdateReconArea(intIdProcess, intIdTransaction, strCode1, strCode2, ListCENTCatalogMatch1, ListCENTCatalogMatchItem1, ListFields1);

                /*
                 * Update reconciliation area
                 */                
                System.out.println("Reconciliation done");
                
                
            } // End reconciliation list from the parameter        
            
            /*
             * If zero, no data was found to reconcile, its better to return an exception and alert the user
             */
            if (intCountRowsToReconcile == 0)
            {
                throw new CENTException("EXCEPTION_NO_INFORMATION_TO_RECONCILE");
            }
            
        }
        catch (CENTException CENTException1)
        {
            CENTException1.SetCode(this.Translate(CENTException1.GetCode()));
            CENTException1.SetMessage(this.Translate(CENTException1.GetMessage()));
            CENTException1.SetFieldName(this.Translate(CENTException1.GetFieldName()));
            
            /*
             * Throw it
             */            
            throw CENTException1;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {            
            /*
             * Destroy the objects
             */
            ListCENTCatalogReconcile1 = null;
            ListCENTCatalogStep1 = null;
            ListCENTCatalogStepRule1 = null;
            ListCENTCatalogStepRuleField1 = null;
            ListCENTCatalogMatch1 = null;
            ListCENTCatalogTransaction1 = null;

            ListCENTReconcile2 = null;
            ListCENTStep1 = null;
            ListCENTRule1 = null;
            ListCENTField1 = null;    
            ListFields1 = null;
        
            strCode1 = null;
            strCode2 = null;    
            
            CENTFilter1 = null;
            CDALReconcile1 = null;

            ListPage1 = null;
            
        }
    }                

    /*
     * Private methods
     */    
    private List<String> GetPages(int intMin, int intMax, int intPageSize)
    {
        List<String> ListPage1 = new ArrayList<String>();
        
        try
        {
            int x = 0;
            int y = 0;
            int i = 0;
            int j = 0;
            
            int intPage = 0;
            int intPages = 0;
            
            
            if (intPageSize > 0)
            {            
                intPages = (intMax - intMin) / intPageSize;
                j = intPageSize;        

                for (i=intMin; i<=intMax; i++)
                {                
                    if (i == (j+intMin))
                    {
                        x = j - intPageSize + intMin;
                        y = j + intMin-1;

                        ListPage1.add(x + ":" + y);
                        intPage ++;       
                        j = j + intPageSize;
                    }   

                    if ((intPage + 1) > intPages)
                    {
                        x = i;
                        y = intMax;
                        ListPage1.add(x + ":" + y);
                        break;
                    }
                }
            }
            else
            {
                ListPage1.add(intMin + ":" + intMax);
            }
                        
            return ListPage1;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListPage1 = null;
        }
    }    
    
    private int SetMatchedAsProposed(int intIdTransaction, int intNextMatchId, List<CENTData> ListCENTField1, ResultSet ResultSet1, ResultSet ResultSet2, int intIdMatchAs) throws Exception
    {
        /*
         * General Declaration
         */
        int intIdCompareType = 0;                

        CENTData CENTKeyToUpdateMatch1 = new CENTData();        
        List<CENTData> ListCENTMatchKey1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTMatchValue1 = new ArrayList<CENTData>();
        CDALReconcile CDALReconcile1 = new CDALReconcile(this.GetConnection(), this.GetSession());

        try
        {            
            /*
             * Get the key to update the match
             */            
            for (CENTData CENTField1 : ListCENTField1)
            {
                intIdCompareType = CENTField1.GetInt(FIELD_ID_FIELD_TYPE);

                if (intIdCompareType == COMPARE_TYPE_KEY) 
                {
                    CENTKeyToUpdateMatch1 = Compare(CENTField1, ResultSet1, ResultSet2);
                    ListCENTMatchKey1.add(CENTKeyToUpdateMatch1);                    
                }
            }    

            /*
             * Get the difference to create the proposed match
             */
            for (CENTData CENTField1 : ListCENTField1)
            {
                intIdCompareType = CENTField1.GetInt(FIELD_ID_FIELD_TYPE);

                if (intIdCompareType == COMPARE_TYPE_CRITERIA) 
                {
                    /*
                     * Get the difference in the key
                     */
                    CENTKeyToUpdateMatch1 = Compare(CENTField1, ResultSet1, ResultSet2);

                    /*
                     * If a difference is found, accumulate field and difference value.
                     */
                    if (!CENTKeyToUpdateMatch1.GetText(FIELD_MATCH_DIFF).equals(""))
                    {
                        CENTKeyToUpdateMatch1.SetInt(FIELD_ID_FIELD, CENTField1.GetInt(FIELD_ID_FIELD));
                        ListCENTMatchValue1.add(CENTKeyToUpdateMatch1);
                    }

                } // Compare key

            } // Field list            
            
            
            if (ListCENTMatchValue1.size() > 0)
            {
                intNextMatchId = CDALReconcile1.MatchedToProposed(intNextMatchId, ListCENTMatchKey1, ListCENTMatchValue1, ListCENTCatalogMatch1, ListCENTCatalogMatchItem1, intIdTransaction, FIELD_ID_FIELD, FIELD_MATCH_DIFF, intIdMatchAs);
            }    
            
            /*
             * Return new match id to control the flow
             */            
            return intNextMatchId;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTKeyToUpdateMatch1 = null;
            ListCENTMatchKey1 = null;
            ListCENTMatchValue1 = null;
            CDALReconcile1 = null;
        }            
    
}

    private CENTData Compare (CENTData CENTField1, ResultSet ResultSet1, ResultSet ResultSet2) throws Exception
    {
        /*
         * General Declaration
         */                
        int intValue1 = 0;
        int intValue2 = 0;      
        double dblValue1 = 0;
        double dblValue2 = 0;
        String strValue1 = "";
        String strValue2 = "";        
        Date datValue1 = null;
        Date datValue2 = null;        
                
        int intIdField = 0;         
        int intFieldType = 0;
        int intIdOperator = 0;
        int intIdToleranceType = 0;
       
        double dblDifference = 0;        
        double dblTolerance = 0;
       
        String strDifference = "";
        String strFieldObject = "";
        String strSeparator = " <> ";        

        BigDecimal decTolerance = null;
        BigDecimal decValue1 = null;
        BigDecimal decValue2 = null;
        BigDecimal decDifference = null;
        
        CENTData CENTKeyToUpdateMatch1 = new CENTData();

        try
        {           
            /*
             * Get the reconcile setup  values
             */
            intIdOperator = CENTField1.GetInt(FIELD_ID_OPERATOR);
            intIdField = CENTField1.GetInt(FIELD_ID_FIELD);
            strFieldObject = this.GetFieldObject(intIdField, ListCENTCatalogTransaction1);
            intFieldType = this.GetFieldType(intIdField, ListCENTCatalogTransaction1);
            dblTolerance = CENTField1.GetDouble(FIELD_TOLERANCE_VALUE);
            intIdToleranceType = CENTField1.GetInt(FIELD_TOLERANCE_TYPE);

            /*
             * Compare the information
             */                    
            switch (intFieldType)
            {
                case TYPE_INT:

                    intValue1 = ResultSet1.getInt(strFieldObject);
                    intValue2 = ResultSet2.getInt(strFieldObject);    
                    
                    CENTKeyToUpdateMatch1.SetText(MATCH_FIELD_KEY_NAME, strFieldObject);
                    CENTKeyToUpdateMatch1.SetInt(MATCH_FIELD_KEY_VALUE, intValue1);
                    CENTKeyToUpdateMatch1.SetInt(MATCH_FIELD_KEY_TYPE, TYPE_INT);
                    
                    switch (intIdToleranceType)
                    {
                        case TOLERANCE_TYPE_ABSOLUTE:
                            dblTolerance = dblTolerance;
                            break;

                        case TOLERANCE_TYPE_PERCENTUAL:
                            dblTolerance = (intValue2 * dblTolerance/100);
                            break;
                    }                    

                    /*
                     * Compare according to the operator, toletances are applied only to equality (= operator)
                     */
                    switch (intIdOperator)
                    {
                        case OPERATOR_EQUALS:
                            
                            if (intValue1 != intValue2)    
                            {
                                dblDifference = Math.abs((intValue1 - intValue2));                        

                                if (dblDifference > dblTolerance)
                                {
                                    strDifference = String.valueOf(dblDifference);
                                }    
                            }
                            
                            break;
                            
                        case OPERATOR_NOT_EQUALS:
                            
                            if (intValue1 != intValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_GREATER:
                            
                            if (intValue1 > intValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_GREATER_EQUALS:
                            
                            if (intValue1 >= intValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_SMALLER:
                            
                            if (intValue1 < intValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_SMALLER_EQUALS:
                            
                            if (intValue1 <= intValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }     
                            
                            break;
                            
                        case OPERATOR_IN:
                                                        
                            if (String.valueOf(intValue2).contains(String.valueOf(intValue1)))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_NOT_IN:
                                                        
                            if (!String.valueOf(intValue2).contains(String.valueOf(intValue1)))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = intValue1 + strSeparator + intValue2;
                            }     
                            
                            break;
                    }
                    
                    break;

                case TYPE_TEXT:

                    strValue1 = ResultSet1.getString(strFieldObject).trim();
                    strValue2 = ResultSet2.getString(strFieldObject).trim();
                    
                    CENTKeyToUpdateMatch1.SetText(MATCH_FIELD_KEY_NAME, strFieldObject);
                    CENTKeyToUpdateMatch1.SetText(MATCH_FIELD_KEY_VALUE, strValue1);
                    CENTKeyToUpdateMatch1.SetInt(MATCH_FIELD_KEY_TYPE, TYPE_TEXT);
                    
                    switch (intIdOperator)
                    {
                        case OPERATOR_EQUALS:
                            
                            strSeparator = " <> ";
                            
                            if (strValue1.equals(strValue2))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = strValue1 + strSeparator + strValue2;
                            }

                            break;
                            
                        case OPERATOR_NOT_EQUALS:
                            
                            strSeparator = " = ";
                            
                            if (!strValue1.equals(strValue2))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = strValue1 + strSeparator + strValue2;
                            }
                            
                            break;

                        case OPERATOR_IN:

                            strSeparator = " !C ";
                            
                            if (strValue2.contains(strValue1))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = strValue1 + strSeparator + strValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_NOT_IN:

                            strSeparator = " C ";                            
                            
                            if (!strValue2.contains(strValue1))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = strValue1 + strSeparator + strValue2;
                            }                                                
                        
                            break;                        
                    }

                    break;

                case TYPE_DATE:

                    datValue1 = ResultSet1.getDate(strFieldObject);
                    datValue2 = ResultSet2.getDate(strFieldObject);                
                    
                    CENTKeyToUpdateMatch1.SetText(MATCH_FIELD_KEY_NAME, strFieldObject);
                    CENTKeyToUpdateMatch1.SetDate(MATCH_FIELD_KEY_VALUE, datValue1);
                    CENTKeyToUpdateMatch1.SetInt(MATCH_FIELD_KEY_TYPE, TYPE_DATE);

                    /*
                     * Compare according to the operator, toletances are applied only to equality (= operator)
                     */
                    switch (intIdOperator)
                    {
                        case OPERATOR_EQUALS:
                            
                            strSeparator = " <> ";
                            
                            if (datValue2 != null)
                            {
                                if (datValue1.equals(datValue2))    
                                {
                                    strDifference = "";
                                }
                                else
                                {                                                                                                        
                                    dblDifference = datValue1.getTime() - datValue2.getTime();
                                    dblDifference = (dblDifference / (24 * 60 * 60 * 1000));
                                    dblDifference = (int) Math.abs(dblDifference);      
                                    dblDifference = Math.abs(dblDifference);                                
                                    strDifference = String.valueOf(Integer.valueOf((int)dblDifference));
                                    strDifference += " " + Translate("Label.Day");

                                    switch (intIdToleranceType)     
                                    {
                                        case TOLERANCE_TYPE_ABSOLUTE:                                                            

                                            if (dblDifference <= dblTolerance)
                                            {
                                                strDifference = "";
                                            }
                                            break;                                                            

                                        case TOLERANCE_TYPE_PERCENTUAL:

                                            dblTolerance = dblDifference * (dblTolerance/100);
                                            dblTolerance = Math.ceil((dblValue1 * (dblTolerance/100)));

                                            if (dblDifference <= dblTolerance)
                                            {
                                                strDifference = "";
                                            }            
                                            break;
                                    }
                                }
                            }
                            else
                            {
                                strDifference = "N/A";
                            }
                        
                            break;
                            
                        case OPERATOR_NOT_EQUALS:
                            
                            strSeparator = " = ";                            
                            
                            if (datValue1 != datValue2)
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = datValue1 + strSeparator + datValue2;
                            }
                            
                            break;
                            
                        case OPERATOR_GREATER:
                            
                            strSeparator = " <= ";
                            
                            if (datValue1.after(datValue2))    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = datValue1 + strSeparator + datValue2;                                
                            }
                            break;
                            
                            
                        case OPERATOR_SMALLER:
                            
                            strSeparator = " >= ";                            
                            
                            if (datValue1.before(datValue2))    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = datValue1 + strSeparator + datValue2;
                            }
                            break;                           
                    }
                    
                    break;

                case TYPE_DOUBLE:

                    dblValue1 = ResultSet1.getDouble(strFieldObject);
                    dblValue2 = ResultSet2.getDouble(strFieldObject); 

                    CENTKeyToUpdateMatch1.SetText(MATCH_FIELD_KEY_NAME, strFieldObject);
                    CENTKeyToUpdateMatch1.SetDouble(MATCH_FIELD_KEY_VALUE, dblValue1);
                    CENTKeyToUpdateMatch1.SetInt(MATCH_FIELD_KEY_TYPE, TYPE_DOUBLE);
                    
                    /*
                     * Compare according to the operator, toletances are applied only to equality (= operator)
                     */
                    switch (intIdOperator)
                    {
                        case OPERATOR_EQUALS:
                            
                            strSeparator = " <> ";
                            
                            if (dblValue1 != dblValue2)    
                            {
                                decTolerance = BigDecimal.valueOf(dblTolerance);                                
                                decValue1 = BigDecimal.valueOf(dblValue1);
                                decValue2 = BigDecimal.valueOf(dblValue2);
                                decDifference = decValue1.subtract(decValue2).abs();                                
                                
                                if (decDifference.compareTo(decTolerance) == 1)
                                {
                                    strDifference = String.valueOf(decDifference);
                                }                   
                            }

                            break;
                            
                        case OPERATOR_NOT_EQUALS:
                            
                            strSeparator = " = ";                            
                            
                            if (dblValue1 != dblValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }
                            
                            break;
                            
                        case OPERATOR_GREATER:
                            
                            strSeparator = " <= ";                            
                            
                            if (dblValue1 > dblValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }
                            
                            break;
                            
                        case OPERATOR_GREATER_EQUALS:
                            
                            strSeparator = " < ";                            
                            
                            if (dblValue1 >= dblValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }
                            
                            break;
                            
                        case OPERATOR_SMALLER:
                            
                            strSeparator = " >= ";                            
                            
                            if (dblValue1 < dblValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }
                            
                            break;
                            
                        case OPERATOR_SMALLER_EQUALS:
                            
                            strSeparator = " > ";                            
                            
                            if (dblValue1 <= dblValue2)    
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }     
                            
                            break;
                            
                        case OPERATOR_IN:
                                             
                            strSeparator = " !C ";
                            
                            if (String.valueOf(dblValue2).contains(String.valueOf(dblValue1)))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }
                            
                            break;
                            
                        case OPERATOR_NOT_IN:

                            strSeparator = " C ";                            
                            
                            if (!String.valueOf(dblValue2).contains(String.valueOf(dblValue1)))
                            {
                                strDifference = "";
                            }
                            else
                            {
                                strDifference = FormatDecimal(dblValue1) + strSeparator + FormatDecimal(dblValue2);
                            }     
                            
                            break;
                    }
                    
                    break;

            } // Field Type                                    

            
            /*
             * Return the difference (if found) and the key to update as orfan, proposed or matched
             */
            CENTKeyToUpdateMatch1.SetText(FIELD_MATCH_DIFF, strDifference);            
            
            return CENTKeyToUpdateMatch1;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strValue1 = null;
            strValue2 = null;

            datValue1 = null;
            datValue2 = null;

            strFieldObject = null;
            strDifference = null;   

            decTolerance = null;
            decValue1 = null;
            decValue2 = null;
            decDifference = null;            
            
            CENTField1 = null;
            ResultSet1 = null;
            ResultSet2 = null;
        }            
    }    


}
