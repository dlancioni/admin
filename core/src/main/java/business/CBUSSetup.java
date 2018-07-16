package business;

import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import persistence.CDALCore;
import persistence.CDALSetup;

/*
 * David Lancioni
 */
public class CBUSSetup extends CBUS
{    
    public CBUSSetup(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }

    private final int NULLABLE_YES = 1;
    private final int NULLABLE_NO = 2;

    private final int UNIQUE_YES = 1;
    private final int UNIQUE_NO = 2;

    private final int PK_YES = 1;    
    private final int PK_NO = 2;    
    
    private final int FK_NO = 0;        

    /*
     * Must be declared here, it's used at transaction function too
     */
    private final int FUNCTION_NEW = 1;
    private final int FUNCTION_EDIT = 2;
    private final int FUNCTION_DELETE = 3;
    private final int FUNCTION_SAVE = 4;
    private final int FUNCTION_FILTER = 5;
    private final int FUNCTION_IMPORT = 6;
    private final int FUNCTION_EXPORT = 7;
    private final int FUNCTION_RECONCILE = 8;
    private final int FUNCTION_SETUP = 9;
    private final int FUNCTION_DUPLICATE = 10;
    private final int FUNCTION_REPROCESS = 11;    
    
    /*
     * Used to create system tables (DANGEROUS) and the first scenario
     */    
    public void ImplementSystemInstance() throws CENTException, SQLException
    {
        /*
         * General Declaration
         */                           
        CDALSetup CDALSetup1 = null;

        try
        {
            /*
             * Create the Objects
             */
            CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());

            /*
             * Implement system instance at all
             */
            CDALSetup1.CreateSystemTable();

        }    
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_BUSINESS", Exception1.getMessage());
        } 
        finally
        {
            CDALSetup1 = null;
        }
    }        
    
    /*
     * Special methods, must be the first place to change when changing the system catalog
     */    
    public List<CENTData> GetCatalogCatalog() throws Exception
    {
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, False, False, True, 0, "", 1, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Transaction", "id_transaction", TYPE_INT, "int_2", 0, False, False, False, 0, "", 2, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Label", "label", TYPE_TEXT, "text_1", 50, False, False, False, 0, "", 3, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Name", "name", TYPE_TEXT, "text_2", 50, False, False, False, 0, "", 4, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Type", "id_type", TYPE_INT, "int_3", 0, False, False, False, TRN_DOMAIN, "domain_1", 5, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Object", "object", TYPE_TEXT, "text_3", 50, False, False, False, 0, "", 6, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Size", "size", TYPE_INT, "int_4", 0, False, False, False, 0, "", 7, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Nullable", "id_nullable", TYPE_BOOLEAN, "boolean_1", 0, False, False, False, 0, "", 8, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Unique", "id_unique", TYPE_BOOLEAN, "boolean_2", 0, False, False, False, 0, "", 9, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Pk", "id_pk", TYPE_BOOLEAN, "boolean_3", 0, False, False, False, 0, "", 10, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Fk", "id_fk", TYPE_INT, "int_5", 0, True, False, False, 0, "", 11, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Domain", "domain_name", TYPE_TEXT, "text_4", 0, True, False, False, 0, "", 12, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Position", "position", TYPE_INT, "int_6", 0, True, False, False, 0, "", 13, ""));
        ListCENTData1.add(GetCatalog(0, TRN_CATALOG, "Catalog.Note", "note", TYPE_TEXT, "text_5", 0, True, False, False, 0, "", 14, ""));

        return ListCENTData1;
    }    
    
    
    /*
     * System model, all table and field definitions
     */
    public CENTData GetTable(int intId, String strName, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }        
        
    public CENTData GetMenu(int intId, int intIdParent, String strName, int intPosition, int intType, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdParent);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(3, intPosition);
        CENTData1.SetInt(4, intType);
        CENTData1.SetText(2, strNote);

        return CENTData1;
    }    
    
    public CENTData GetTransaction(int intId, String strName, String strPage, int intIdMenu, int intIdTable, int intIdType, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strPage);
        CENTData1.SetInt(2, intIdMenu);
        CENTData1.SetInt(3, intIdTable);
        CENTData1.SetInt(4, intIdType);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }    
    
    public CENTData GetCatalog(int intId, int intIdTrn, String strLabel, String strName, int intIdType, String strObject, int intSize, int blnNullAble, int blnUnique, int blnPk, int intIdFk, String strDomainName, int intPosition, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);                                                 // id
        CENTData1.SetInt(2, intIdTrn);                                              // id_trn
        CENTData1.SetText(1, strLabel);                                             // label
        CENTData1.SetText(2, strName);                                              // name
        CENTData1.SetInt(3, intIdType);                                             // id_type
        CENTData1.SetText(3, strObject);                                            // object
        CENTData1.SetInt(4, intSize);                                               // size            
        CENTData1.SetBoolean(1, blnNullAble);                                       // id_nullable
        CENTData1.SetBoolean(2, blnUnique);                                         // id_uniqaue
        CENTData1.SetBoolean(3, blnPk);                                             // id_pk
        CENTData1.SetInt(5, intIdFk);                                               // id_fk
        CENTData1.SetText(4, strDomainName);                                        // domain_name
        CENTData1.SetInt(6, intPosition);                                           // position
        CENTData1.SetText(5, strNote);                                              // note        
        
        return CENTData1;
    }        
    
    public CENTData GetCompany(int intId, String strName, String strDocument, int intIdCountry, int intIdLanguage, String strMail, Date datExpireDate, int intNumberOfReconcile, int intDaysToKeepHistory, int intNumberOfLines, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strDocument);
        CENTData1.SetInt(1, intIdCountry);
        CENTData1.SetInt(2, intIdLanguage);
        CENTData1.SetText(3, strMail);
        CENTData1.SetDate(1, datExpireDate);
        CENTData1.SetInt(3, intNumberOfReconcile);
        CENTData1.SetInt(4, intDaysToKeepHistory);
        CENTData1.SetInt(5, intNumberOfLines);
        CENTData1.SetText(4, strNote);
        
        return CENTData1;
    }        
    
    public CENTData GetProfileTransaction(int intId, int intIdProfile, int intIdTransaction, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);                                   // id
        CENTData1.SetInt(2, intIdProfile);                                          // id_profile
        CENTData1.SetInt(3, intIdTransaction);                                      // id_trn
        CENTData1.SetText(1, strNote);                                              // note        
        
        return CENTData1;
    }        

    public CENTData GetTransactionFunction(int intId, int intIdProfile, int intIdTransaction, int intIdFunction, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);                                                 // id
        CENTData1.SetInt(2, intIdProfile);                                          // id_profile
        CENTData1.SetInt(3, intIdTransaction);                                      // id_transaction
        CENTData1.SetInt(4, intIdFunction);                                         // id_function
        CENTData1.SetText(1, strNote);                                              // note        
        
        return CENTData1;
    }            

    public CENTData GetCountry(int intId, String strName, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(4, strNote);
        
        return CENTData1;
    }    
    
    public CENTData GetLanguage(int intId, String strName, String strCode, String strCountry, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strCode);
        CENTData1.SetText(3, strCountry);
        CENTData1.SetText(4, strNote);
        
        return CENTData1;
    }
    
    public CENTData GetDictionary(int intId, int intIdLanguage, String strCode, String strValue, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLanguage);
        CENTData1.SetText(1, strCode);
        CENTData1.SetText(2, strValue);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            

    public CENTData GetFunction(int intId, String strName, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();

        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }        
    
    public CENTData GetDomain(int intId, String strName, int intIdDomain, String strDescription, int intDomainType, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(2, intIdDomain);
        CENTData1.SetText(2, strDescription);
        CENTData1.SetInt(3, intDomainType);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }    

    public CENTData GetFieldEvent(int intId, int intIdTransaction, String strFieldName, int intIdAction, int intIdEvent, int intIdFunction, String strParameter1, String strParameter2, String strParameter3, String strParameter4, String strParameter5, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdTransaction);
        CENTData1.SetText(1, strFieldName);
        CENTData1.SetInt(3, intIdAction);
        CENTData1.SetInt(4, intIdEvent);
        CENTData1.SetInt(5, intIdFunction);
        CENTData1.SetText(2, strParameter1);
        CENTData1.SetText(3, strParameter2);
        CENTData1.SetText(4, strParameter3);
        CENTData1.SetText(5, strParameter4);
        CENTData1.SetText(6, strParameter5);
        CENTData1.SetText(7, strNote);
        
        return CENTData1;
    }    
    
    public CENTData GetView(int intId, String strName, int intIdTrn, String strProcedure, int intIdDisplay, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(2, intIdTrn);
        CENTData1.SetText(2, strProcedure);
        CENTData1.SetInt(3, intIdDisplay);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            
    
    public CENTData GetViewDef(int intId, int intIdView, int intIdCommand, int intIdTransaction, int intIdField, int intIdCondition, String strValue, int intPosition, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdView);
        CENTData1.SetInt(3, intIdCommand);
        CENTData1.SetInt(4, intIdTransaction);
        CENTData1.SetInt(5, intIdField);
        CENTData1.SetInt(6, intIdCondition);
        CENTData1.SetInt(7, intPosition);        
        CENTData1.SetText(1, strValue);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }        

    public CENTData GetLayout(int intId, String strName, int intIdArea, int intIdType, int intIdTrn, String strDataSource, int intLineStart, int intPosition, int intSize, int intIdAction, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(2, intIdArea);
        CENTData1.SetInt(3, intIdType);
        CENTData1.SetInt(4, intIdTrn);
        CENTData1.SetText(2, strDataSource);
        CENTData1.SetInt(5, intLineStart);
        CENTData1.SetInt(6, intPosition);
        CENTData1.SetInt(7, intSize);
        CENTData1.SetInt(8, intIdAction);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            
    
    public CENTData GetLayoutSession(int intId, int intIdLayout, String strName, int intIdType, String strIdentifier, int intIdFormat, String strDelimiter, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLayout);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(3, intIdType);
        CENTData1.SetText(2, strIdentifier);
        CENTData1.SetInt(4, intIdFormat);
        CENTData1.SetText(3, strDelimiter);
        CENTData1.SetText(4, strNote);
        
        return CENTData1;
    }            
   
    public CENTData GetLayoutSessionDefinition(int intId, int intIdLayout, int intIdSession, int intStartPosition, int intSize, int intIdTrn, int intIdField, String strMask, String strDefaultValue, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLayout);
        CENTData1.SetInt(3, intIdSession);
        CENTData1.SetInt(4, intStartPosition);
        CENTData1.SetInt(5, intSize);
        CENTData1.SetInt(6, intIdTrn);
        CENTData1.SetInt(7, intIdField);
        CENTData1.SetText(1, strMask);
        CENTData1.SetText(2, strDefaultValue);
        CENTData1.SetText(3, strNote);

        return CENTData1;
    }                

    public CENTData GetLookUp(int intId, String strName, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }
    
    public CENTData GetLookUpItem(int intId, int intIdLookUp, String strCode, String strDescription, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLookUp);
        CENTData1.SetText(1, strCode);
        CENTData1.SetText(2, strDescription);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            
    
    public CENTData GetLayoutLookUp(int intId, int intIdLayout, int intIdTrn, int intIdField, int intIdLookUp, int intRefuseLine, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLayout);
        CENTData1.SetInt(3, intIdTrn);
        CENTData1.SetInt(4, intIdField);
        CENTData1.SetInt(5, intIdLookUp);
        CENTData1.SetInt(6, intRefuseLine);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            
    
    public CENTData GetLayoutFunction(int intId, int intIdLayout, int intIdTrn, int intIdField, int intIdFunction, String strParameter1, String strParameter2, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdLayout);
        CENTData1.SetInt(3, intIdTrn);
        CENTData1.SetInt(4, intIdField);
        CENTData1.SetInt(5, intIdFunction);
        CENTData1.SetText(1, strParameter1);
        CENTData1.SetText(2, strParameter2);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }            
       
    public CENTData GetReconcile(int intId, String strName, int intIdArea, int intIdTrn, int intIdType, int intIdView1, int intIdView2, int intIdResult, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId); 
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(2, intIdArea);
        CENTData1.SetInt(3, intIdTrn);
        CENTData1.SetInt(4, intIdType);
        CENTData1.SetInt(5, intIdView1);
        CENTData1.SetInt(6, intIdView2);
        CENTData1.SetInt(7, intIdResult);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }            

    public CENTData GetReconcileStep(int intId, int intIdReconcile, String strName, String strCode1, String strCode2, int intIdEnabled, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdReconcile);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strCode1);
        CENTData1.SetText(3, strCode2);
        CENTData1.SetInt(3, intIdEnabled);
        CENTData1.SetText(4, strNote);

        return CENTData1;
    }

    public CENTData GetReconcileStepRule(int intId, int intIdReconcile, int intIdStep, String strName, int intIdEnabled, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdReconcile);
        CENTData1.SetInt(3, intIdStep);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(4, intIdEnabled);
        CENTData1.SetText(2, strNote);
        
        return CENTData1;
    }    
    
    public CENTData GetReconcileStepRuleField(int intId, int intIdReconcile, int intIdStep, int intIdRule, int intIdTrn, int intIdField, int intIdType, int intIdOperator, double dblTolerance, int intIdToleranceType, int intIdEnabled, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdReconcile);
        CENTData1.SetInt(3, intIdStep);
        CENTData1.SetInt(4, intIdRule);
        CENTData1.SetInt(5, intIdTrn);
        CENTData1.SetInt(6, intIdField);
        CENTData1.SetInt(7, intIdType);
        CENTData1.SetInt(8, intIdOperator);
        CENTData1.SetDouble(1, dblTolerance);
        CENTData1.SetInt(9, intIdToleranceType);
        CENTData1.SetInt(10, intIdEnabled);
        CENTData1.SetText(1, strNote);
        
        return CENTData1;
    }            

    public CENTData AddProfileTrnFunction(int intId, int intIdProfile, int intIdTrn, int intIdFunction, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdProfile);
        CENTData1.SetInt(3, intIdTrn);
        CENTData1.SetInt(4, intIdFunction);
        CENTData1.SetText(1, strNote);
        
        return CENTData1;
    }

    public CENTData GetProfile(int intId, String strName, String strEmail, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strEmail);
        CENTData1.SetText(3, strNote);

        return CENTData1;
    }            
    
    public CENTData GetUser(int intId, String strUsername, String strPassword, int intIdProfile, int intIdArea, String strEmail, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strUsername);
        CENTData1.SetText(2, strPassword);
        CENTData1.SetInt(2, intIdProfile);
        CENTData1.SetInt(3, intIdArea);
        CENTData1.SetText(3, strEmail);
        CENTData1.SetText(4, strNote);
        
        return CENTData1;
    }

    public CENTData GetArea(int intId, int intIdCompany, String strName, String strMail, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdCompany);
        CENTData1.SetText(1, strName);
        CENTData1.SetText(2, strMail);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }

    public CENTData GetSchedule(int intId, String strName, int intIdArea, int intIdRecurrence, String strTime, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(2, intIdArea);        
        CENTData1.SetInt(3, intIdRecurrence);        
        CENTData1.SetText(2, strTime);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }
    
    public CENTData GetScheduleJob(int intId, int intIdSchedule, String strName, int intIdType, int intIdService, String strNote) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);
        CENTData1.SetInt(2, intIdSchedule);        
        CENTData1.SetText(1, strName);
        CENTData1.SetInt(3, intIdType);        
        CENTData1.SetInt(4, intIdService);
        CENTData1.SetText(3, strNote);
        
        return CENTData1;
    }    
    
    /*
     * Initial information to populate the solution
     */
    public List<CENTData> GetListOfTable() throws Exception
    {
        int i = 0;        
        int intId = 0;        
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        for (i=1; i<=TRN_COUNT; i++)
        {
            ListCENTData1.add(GetTable(++ intId, PREFIX_TABLE_SYSTEM + i, "Tabelas usadas pelo sistema"));
        }

        for (i=1; i<=10; i++)
        {
            ListCENTData1.add(GetTable(++ intId, PREFIX_TABLE_USER + i, "Tabelas criadas para transações"));
        }        
        
        return ListCENTData1;
    }        
    
    public List<CENTData> GetListOfMenu(int intCompanyType) throws Exception
    {
        int intId = 0;
        int intPos = 0;
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetMenu(MENU_SETUP, 0, "MENU_SETUP", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_ACCESS_CONTROL, MENU_SETUP, "MENU_ACCESS_CONTROL", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_TRANSACTION, MENU_SETUP, "MENU_TRANSACTION", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_DICTIONARY, MENU_SETUP, "MENU_DICTIONARY", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_SCHEDULE, MENU_SETUP, "MENU_SCHEDULE", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_ETL, 0, "MENU_ETL", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_ETL_LAYOUT, MENU_ETL, "MENU_ETL_LAYOUT", ++intPos, MENU_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetMenu(MENU_ETL_TRANSLATION, MENU_ETL, "MENU_ETL_TRANSLATION", ++intPos, MENU_TYPE_SYSTEM, ""));
        
        
        if (intCompanyType == COMPANY_TYPE_RECONCILIATION)
        {
            ListCENTData1.add(GetMenu(MENU_RECONCILIATION, 0, "MENU_RECONCILIATION", ++intPos, MENU_TYPE_SYSTEM, ""));
            ListCENTData1.add(GetMenu(MENU_RECONCILIATION_SETUP, MENU_RECONCILIATION, "MENU_RECONCILIATION_SETUP", ++intPos, MENU_TYPE_SYSTEM, ""));
            ListCENTData1.add(GetMenu(MENU_RA, 0, "MENU_RA", ++intPos, MENU_TYPE_SYSTEM, ""));
        }
        
        if (intCompanyType == COMPANY_TYPE_ENTRY_MANAGER)
        {        
            ListCENTData1.add(GetMenu(MENU_ENTRY, 0, "MENU_ENTRY", ++intPos, MENU_TYPE_USER, ""));
            ListCENTData1.add(GetMenu(MENU_ENTITY, MENU_ENTRY, "MENU_ENTITY", ++intPos, MENU_TYPE_USER, ""));
        }

        ListCENTData1.add(GetMenu(MENU_SESSION, 0, "MENU_SESSION", ++intPos, MENU_TYPE_SYSTEM, ""));
        
        
        
        return ListCENTData1;
    }

    public List<CENTData> GetListOfTransactions() throws Exception
    {
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetTransaction(TRN_TABLE, "TRN_TABLE", "main", MENU_TRANSACTION, TABLE_TABLE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_MENU, "TRN_MENU", "main", MENU_TRANSACTION, TABLE_MENU, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_TRANSACTION, "TRN_TRANSACTION", "main", MENU_TRANSACTION, TABLE_TRANSACTION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_CATALOG, "TRN_CATALOG", "main", MENU_TRANSACTION, TABLE_CATALOG, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_FIELD_EVENT, "TRN_FIELD_EVENT", "main", MENU_TRANSACTION, TABLE_FIELD_EVENT, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_VIEW, "TRN_VIEW", "main", MENU_TRANSACTION, TABLE_VIEW, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_VIEW_DEF, "TRN_VIEW_DEF", "main", MENU_TRANSACTION, TABLE_VIEW_DEF, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_DOMAIN, "TRN_DOMAIN", "main", MENU_TRANSACTION, TABLE_DOMAIN, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_COUNTRY, "TRN_COUNTRY", "main", MENU_DICTIONARY, TABLE_COUNTRY, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LANGUAGE, "TRN_LANGUAGE", "main", MENU_DICTIONARY, TABLE_LANGUAGE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_DICTIONARY, "TRN_DICTIONARY", "main", MENU_DICTIONARY, TABLE_DICTIONARY, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_SCHEDULE, "TRN_SCHEDULE", "main", MENU_SCHEDULE, TABLE_SCHEDULE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_SCHEDULE_JOB, "TRN_SCHEDULE_JOB", "main", MENU_SCHEDULE, TABLE_SCHEDULE_JOB, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_QUEUE, "TRN_QUEUE", "main", MENU_SCHEDULE, TABLE_QUEUE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_COMPANY, "TRN_COMPANY", "main", MENU_ACCESS_CONTROL, TABLE_COMPANY, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_AREA, "TRN_AREA", "main", MENU_ACCESS_CONTROL, TABLE_AREA, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_USER, "TRN_USER", "main", MENU_ACCESS_CONTROL, TABLE_USER, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_PROFILE, "TRN_PROFILE", "main", MENU_ACCESS_CONTROL, TABLE_PROFILE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_PROFILE_TRANSACTION, "TRN_PROFILE_TRANSACTION", "main", MENU_ACCESS_CONTROL, TABLE_PROFILE_TRANSACTION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_TRANSACTION_FUNCTION, "TRN_TRANSACTION_FUNCTION", "main", MENU_ACCESS_CONTROL, TABLE_TRANSACTION_FUNCTION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_FUNCTION, "TRN_FUNCTION", "main", MENU_ACCESS_CONTROL, TABLE_FUNCTION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT, "TRN_LAYOUT", "main", MENU_ETL_LAYOUT, TABLE_LAYOUT, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_SESSION, "TRN_LAYOUT_SESSION", "main", MENU_ETL_LAYOUT, TABLE_LAYOUT_SESSION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_SESSION_DEFINITION, "TRN_LAYOUT_SESSION_DEFINITION", "main", MENU_ETL_LAYOUT, TABLE_LAYOUT_SESSION_DEFINITION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_LAYOUT_LOOKUP, "TRN_LAYOUT_LAYOUT_LOOKUP", "main", MENU_ETL_TRANSLATION, TABLE_LAYOUT_LAYOUT_LOOKUP, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_FUNCTION, "TRN_LAYOUT_FUNCTION", "main", MENU_ETL_TRANSLATION, TABLE_LAYOUT_FUNCTION, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_LOOKUP, "TRN_LAYOUT_LOOKUP", "main", MENU_ETL_TRANSLATION, TABLE_LAYOUT_LOOKUP, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LAYOUT_LOOKUP_ITEM, "TRN_LAYOUT_LOOKUP_ITEM", "main", MENU_ETL_TRANSLATION, TABLE_LAYOUT_LOOKUP_ITEM, TRANSACTION_TYPE_SYSTEM, ""));        
        ListCENTData1.add(GetTransaction(TRN_RECONCILIATION, "TRN_RECONCILIATION", "main", MENU_RECONCILIATION_SETUP, TABLE_RECONCILE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_RECONCILIATION_STEP, "TRN_RECONCILIATION_STEP", "main", MENU_RECONCILIATION_SETUP, TABLE_RECONCILE_STEP, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_RECONCILIATION_STEP_RULE, "TRN_RECONCILIATION_STEP_RULE", "main", MENU_RECONCILIATION_SETUP, TABLE_RECONCILE_STEP_RULE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD, "TRN_RECONCILIATION_STEP_RULE_FIELD", "main", MENU_RECONCILIATION_SETUP, TABLE_RECONCILE_STEP_RULE_FIELD, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_MATCH, "TRN_MATCH", "main", MENU_RECONCILIATION, TABLE_MATCH, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_MATCH_ITEM, "TRN_MATCH_ITEM", "main", MENU_RECONCILIATION, TABLE_MATCH_ITEM, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_FILE_MANAGER, "TRN_FILE_MANAGER", "filemanager", MENU_SETUP, TABLE_NONE, TRANSACTION_TYPE_SYSTEM, ""));
        ListCENTData1.add(GetTransaction(TRN_LOGOUT, "TRN_LOGOUT", "logout", MENU_SESSION, TABLE_NONE, TRANSACTION_TYPE_SYSTEM, ""));        
        
        
        return ListCENTData1;
    }    

    public List<CENTData> GetListOfCountry() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        ListCENTData1.add(GetCountry(++ intId, "Brasil", ""));
        ListCENTData1.add(GetCountry(++ intId, "EUA", ""));
        ListCENTData1.add(GetCountry(++ intId, "Espanha", ""));
     
        return ListCENTData1;
    }    
    
    public List<CENTData> GetListOfLanguages() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        ListCENTData1.add(GetLanguage(++ intId, "Português", "pt", "BR", "Vírgula como indicador de decimal e formato padrão de data dd/MM/yyyy"));
        ListCENTData1.add(GetLanguage(++ intId, "English", "en", "US", "Ponto como indicador de decimal e formato padrão de data MM/dd/yyyy"));
        ListCENTData1.add(GetLanguage(++ intId, "Espanõl", "sp", "SP", "Vírgula como indicador de decimal e formato padrão de data dd/MM/yyyy"));
     
        return ListCENTData1;
    }

    public List<CENTData> GetListOfDictionaries() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();       
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_ORACLE_DATABASE_CONNECTION", "Não foi possível conectar ao servidor de banco de dados Oracle", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_ORACLE_DATABASE_CONNECTION", "Cannot connect to Oracle Server Database", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_ORACLE_DATABASE_CONNECTION", "No se pudo conectar con el servidor de base de datos Oracle", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MSSQL_DATABASE_CONNECTION", "Não foi possível conectar ao servidor de banco de dados SQL Server", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MSSQL_DATABASE_CONNECTION", "Cannot connect to SQL Server Server Database", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MSSQL_DATABASE_CONNECTION", "No se pudo conectar con el servidor de base de datos SQL Server", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_BUSINESS", "Erro de processamento na camada de negócio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_BUSINESS", "Processing error in the business layer", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_BUSINESS", "Error de procesamiento en la capa de negocios", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_GET_NEXT_ID", "Falha ao obter o próximo Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_GET_NEXT_ID", "Fail to get next Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_GET_NEXT_ID", "No puede conseguir el próximo Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_GET_TABLE_NAME", "Falha ao obter a tabela de sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_GET_TABLE_NAME", "Fail to get system table", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_GET_TABLE_NAME", "No puede conseguir el nombre de la tabla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_CATALOG_NOT_FOUND", "Não foi possível encontrar o catalogo referente a transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_CATALOG_NOT_FOUND", "Cannot find the catalog related to the transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_CATALOG_NOT_FOUND", "No se puede encontrar el catálogo relacionada con la transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_INSERT_RECORD", "Não foi possível inserir os dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_INSERT_RECORD", "Fail to insert data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_INSERT_RECORD", "No puede conseguir insertar datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_UPDATE_RECORD", "Não foi possível alterar os dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_UPDATE_RECORD", "Fail to update data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_UPDATE_RECORD", "No puede conseguir cambiar datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_DELETE_RECORD", "Não foi possível excluir os dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_DELETE_RECORD", "Fail to delete data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_DELETE_RECORD", "No puede conseguir apagar datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_GETLIST", "Falha de processamento ao consultar os dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_GETLIST", "Processing error when querying the data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_GETLIST", "Error al procesar al consultar los datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PREPARE_VIEW", "Falha ao gerar a consulta baseada na visão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PREPARE_VIEW", "Fail to generate the query based on the view", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PREPARE_VIEW", "No se puede generar la consulta basada en la visión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_CREATE_COMPANY", "Erro de processamento ao criar nova empresa", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_CREATE_COMPANY", "Processing error to create a new company", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_CREATE_COMPANY", "Error de procesamiento para crear una nueva empresa", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_IMPLEMENT_SYSTEM_INSTANCE", "Erro de processamento ao criar nova instancia do sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_IMPLEMENT_SYSTEM_INSTANCE", "Processing error to create a new system instance", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_IMPLEMENT_SYSTEM_INSTANCE", "Error de procesamiento para crear una instancia de sistema", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Não foi possível validar o valor", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Fail to validate the value", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Falla para validar el valor", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PARSE_DATE", "Não foi possível validar a data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PARSE_DATE", "Fail to validate the date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PARSE_DATE", "Falla para validar la fecha", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PARSE_INTEGER", "Não foi possível validar o valor numérico", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PARSE_INTEGER", "Fail to validate the number", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PARSE_INTEGER", "Falla para validar el número", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FILE_NOT_FOUND", "Arquivo não encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FILE_NOT_FOUND", "File not found", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FILE_NOT_FOUND", "Archivo no encontrado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_DATA_TYPE", "Tipo de dado inválido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_DATA_TYPE", "Invalid data type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_DATA_TYPE", "Tipo de datos no válido", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_VIEW_ID", "Id na visão inválido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_VIEW_ID", "Invalid view Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_VIEW_ID", "Inválido Id da Visão", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_VIEW_NOT_FOUND", "Visão não encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_VIEW_NOT_FOUND", "View not found", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_VIEW_NOT_FOUND", "Visión no encontrado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MANDATORY_FIELD", "Campo obrigatório", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MANDATORY_FIELD", "Mandatory Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MANDATORY_FIELD", "Campo obligatorio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_TEXT_TOO_LONG", "Texto informado é maior que o limite", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_TEXT_TOO_LONG", "Text is greater than the limit", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_TEXT_TOO_LONG", "Texto es mayor que el límite", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_UI_GENERAL", "Erro de processamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_UI_GENERAL", "Processing error", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_UI_GENERAL", "Error de procesamiento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "A quantidade de colunas mapeadas é maior que a quantidade de colunas no arquivo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "Number of mapped columns is greater than the file", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "Número de columnas asignadas es mayor que el archivo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE_TO_INT", "Tolerância em dias não podem ser aplicada em campos do tipo inteiro", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE_TO_INT", "Tolerance in days cannot be applied to integer fields", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE_TO_INT", "La tolerancia en día no se puede aplicar a campos de enteros", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_TOLERANCE_TYPE_INT", "Campos do tipo inteiro não aceitam tolerância em dias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_TOLERANCE_TYPE_INT", "Integer fields do not accept tolerance in days", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_TOLERANCE_TYPE_INT", "Campos de tipo entero no aceptan la tolerancia en días", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_TOLERANCE_TYPE_TEXT", "Campos do tipo texto não aceitam tolerância", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_TOLERANCE_TYPE_TEXT", "Text fields do not accept tolerance", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_TOLERANCE_TYPE_TEXT", "Campos de tipo texto no aceptan la tolerancia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE", "Campos do tipo data só aceitam tolerância em dias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE", "Date fields only accept tolerance in days", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_TOLERANCE_TYPE_DATE", "Los campos de fecha sólo aceptan la tolerancia en días", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_TOLERANCE_TYPE_DOUBLE", "Campos de valores não aceitam tolerância em dias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_TOLERANCE_TYPE_DOUBLE", "Amount fields do not accept tolerance in days", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_TOLERANCE_TYPE_DOUBLE", "Campos de tipo doble no aceptan la tolerancia en días", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_TOLERANCE_TYPE_MANDATORY", "Se cadastrar uma tolerância, o tipo é obrigatório", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_TOLERANCE_TYPE_MANDATORY", "Tolerance type is mandatory when the tolerance is defined", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_TOLERANCE_TYPE_MANDATORY", "Tipo de tolerancia es necesario cuando se introduce un valor de tolerancia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MISSING_RECONCILE", "Conciliação não encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MISSING_RECONCILE", "Reconciliation not found", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MISSING_RECONCILE", "Reconciliación que no se encuentra", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MISSING_RECONCILE_STEP", "Conciliação sem nenhum passo cadastrado ou habilitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MISSING_RECONCILE_STEP", "Reconciliation without any step registered or enabled", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MISSING_RECONCILE_STEP", "La reconciliación sin ninguna etapa de domicilio ou ligada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MISSING_RECONCILE_STEP_RULE", "Passo da conciliação sem nenhuma regra cadastrada ou habilitada", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MISSING_RECONCILE_STEP_RULE", "The reconciliation step without any registered or enabled rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MISSING_RECONCILE_STEP_RULE", "El paso de la reconciliación sin ninguna regla registrada ou habilitada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MISSING_RECONCILE_STEP_RULE_FIELD", "Regras de conciliação não definidas ou desabilitadas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MISSING_RECONCILE_STEP_RULE_FIELD", "Reconciliation rules not defined or disabled", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MISSING_RECONCILE_STEP_RULE_FIELD", "Reglas de la conciliación no especificado ou habilitada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_NO_INFORMATION_TO_RECONCILE", "Registros para conciliação não encontrados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_NO_INFORMATION_TO_RECONCILE", "Reconciliation data not found", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_NO_INFORMATION_TO_RECONCILE", "No se han encontrado datos de conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_NOT_FOUND", "Leiaut não encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_NOT_FOUND", "Layout not found", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_NOT_FOUND", "Disposición que no se encuentra", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_SESSION_NOT_FOUND", "Nenhuma sessão cadastrada para o layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_SESSION_NOT_FOUND", "Session not found for current layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_SESSION_NOT_FOUND", "La sesión no se encontró para la disposición actual", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_SESSION_DEFINITION_NOT_FOUND", "Layout com sessão não especificada", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_SESSION_DEFINITION_NOT_FOUND", "Missing session especification for current layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_SESSION_DEFINITION_NOT_FOUND", "Missing especificación de sesión para la presentación actual", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_LAYOUT_TYPE", "Tipo de layout inválido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_LAYOUT_TYPE", "Invalid layout type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_LAYOUT_TYPE", "Tipo de disposición no válido", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_SESSION_NOT_IDENTIFIED", "Layouts com header/trailer devem ter o identificador de sessão especificados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_SESSION_NOT_IDENTIFIED", "Layouts with header/trailer must have the session identifier specified", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_SESSION_NOT_IDENTIFIED", "Con disposición de cabecera / remolque debe tener el identificador de sesión especificado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "Mapeamento incorreto, quantidade de informações no layout é diferente do arquivo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "Invalid mapping, amount of information defined in the layout is different from the file", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_DEFFERENCE_FILE", "Mapeo no válida, la cantidad de información definida en la disposición es diferente del archivo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PARSE_DATE", "Mapeamento incorreto, mascara de data diferente do formato encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PARSE_DATE", "Invalid mapping, date mask is different", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PARSE_DATE", "Mapeo no válida , la máscara de la fecha es diferente", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Mapeamento incorreto, mascara de valor diferente do formato encontrado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Invalid mapping, double mask is different", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FAIL_TO_PARSE_DOUBLE", "Mapeo no válida , la máscara de la valor es diferente", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_OPERATOR_TEXT_FIELD", "Operador inválido para campos do tipo texto", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_OPERATOR_TEXT_FIELD", "Invalid operator for text fields", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_OPERATOR_TEXT_FIELD", "Operador no válido para los campos de texto", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_INVALID_OPERATOR_DATE_FIELD", "Operador inválido para campos do tipo data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_INVALID_OPERATOR_DATE_FIELD", "Invalid operator for date fields", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_INVALID_OPERATOR_DATE_FIELD", "Operador no válido para los campos de fecha", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_OPERATOR_TOLERANCE_EQUALS_ONLY", "Somente operador de igualdade permite trabalhar com tolerâncias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_OPERATOR_TOLERANCE_EQUALS_ONLY", "Only equality operator lets you work with tolerances", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_OPERATOR_TOLERANCE_EQUALS_ONLY", "Sólo operador de igualdad le permite trabajar con tolerancias", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_NO_INFORMATION_TO_EXPORT", "Não existem registros a serem exportados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_NO_INFORMATION_TO_EXPORT", "No data to export", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_NO_INFORMATION_TO_EXPORT", "Não há dados para exportar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", "Já existe um registro com esse nome ou descrição. Campo não aceita valores duplicados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", "Record already exists. Cannot input duplicated values for this field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", "Registro ya existe. No puede ingresar un valor duplicado para este campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MAPPED_FIELD_NOT_FOUND_AT_RECONCILIATION_AREA", "Campo mapeado não existe na área de conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MAPPED_FIELD_NOT_FOUND_AT_RECONCILIATION_AREA", "Mapped field does not exists at reconciliation area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MAPPED_FIELD_NOT_FOUND_AT_RECONCILIATION_AREA", "campo mapeado não existe na área de reconciliação", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FIELD_OBJECT_ALREAD_MAPPED", "Campo objeto já foi mapeado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FIELD_OBJECT_ALREAD_MAPPED", "Field object already mapped", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FIELD_OBJECT_ALREAD_MAPPED", "El campo objeto ya mapeada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FIELD_NAME_ALREAD_MAPPED", "Campo nome já foi mapeado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FIELD_NAME_ALREAD_MAPPED", "Field name already mapped", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FIELD_NAME_ALREAD_MAPPED", "El campo nombre ya mapeada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FIELD_LABEL_ALREAD_MAPPED", "Campo label já foi mapeado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FIELD_LABEL_ALREAD_MAPPED", "Field label already mapped", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FIELD_LABEL_ALREAD_MAPPED", "El campo label ya mapeada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_MISSING_MATCH_KEY", "Chave de batimento não definida", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_MISSING_MATCH_KEY", "Match key not defined", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_MISSING_MATCH_KEY", "Partido clave no está definido", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_CANNOT_DELETE_COMPANY", "Empresa base não pode ser excluída", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_CANNOT_DELETE_COMPANY", "Base company cannot be deleted", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_CANNOT_DELETE_COMPANY", "Empresa de base no se puede eliminar", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_CANNOT_SELECT_FIELD_DIFF_TRANSACTION", "Não é permitido selecionar campos de transações diferentes", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_CANNOT_SELECT_FIELD_DIFF_TRANSACTION", "Cannot select fields from different transactions", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_CANNOT_SELECT_FIELD_DIFF_TRANSACTION", "No se puede seleccionar campos de diferentes transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_CANNOT_DELETE_SYSTEM_FIELD", "Campos do sistema não podem ser excluídos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_CANNOT_DELETE_SYSTEM_FIELD", "Cannot delete system fields", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_CANNOT_DELETE_SYSTEM_FIELD", "No se puede eliminar campos del sistema", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_LAYOUT_LINE_LIMIT", "Arquivo possui mais linhas do que o permitido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_LAYOUT_LINE_LIMIT", "Number of lines for this layout is exceeded", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_LAYOUT_LINE_LIMIT", "Se supera el número de líneas de este disposición", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_USER_NOT_FOUND", "Usuário não cadastrado no sistema ou senha inválida", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_USER_NOT_FOUND", "User not registered or invalid password", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_USER_NOT_FOUND", "El usuario no registrado o contraseña inválida", ""));        
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FILE_TO_UPLOAD_NOT_SELECTED", "Nenhum arquivo selecionado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FILE_TO_UPLOAD_NOT_SELECTED", "No file selected", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FILE_TO_UPLOAD_NOT_SELECTED", "Ningún archivo seleccionado", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_SITE", "Houve um erro de processamento no site, sua sessão foi encerrada. Tente novamente ou entre em contato com administrador do sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_SITE", "There was a processing error on the site, your session has ended. Please try again or contact your system administrator", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_SITE", "Hubo un error de procesamiento en el sitio, su sesión ha finalizado. Inténtelo de nuevo o póngase en contacto con el administrador del sistema", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FILE_ALREADY_EXISTS", "Arquivo já existe, apague e tente novamente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FILE_ALREADY_EXISTS", "File already exists, delete the file and try again", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FILE_ALREADY_EXISTS", "El archivo ya existe, elimine el archivo y vuelva a intentarlo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_COMPANY_NOT_FOUND", "Empresa não cadastrada ou código inválido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_COMPANY_NOT_FOUND", "Company not registered or invalid code", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_COMPANY_NOT_FOUND", "Empresa no registrada o código no válido", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FK_FIELD_MUST_BE_INT", "Campos de chave estrangeira devem ser inteiros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FK_FIELD_MUST_BE_INT", "Foreign Key fields must be integer", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FK_FIELD_MUST_BE_INT", "Los campos clave extranjeros deben ser enteros", ""));
        

        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_FK_DEPENDENCIES", "Não é possível excluir o registro, excluir informações relacionadas em ", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_FK_DEPENDENCIES", "Cannot delete record, please remove related record in ", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_FK_DEPENDENCIES", "No se puede eliminar el registro, elimine el registro relacionado en ", ""));                
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EXCEPTION_CANNOT_DELETE_MENU_TYPE_SYSTEM", "Menus do tipo sistema não podem ser excluídos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EXCEPTION_CANNOT_DELETE_MENU_TYPE_SYSTEM", "Cannot delete menu type system", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EXCEPTION_CANNOT_DELETE_MENU_TYPE_SYSTEM", "No se puede eliminar el menú de tipo sistema", ""));                
        
        
        
        /*
         * Dictionary domain
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "BOOLEAN_YES", "Sim", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "BOOLEAN_YES", "Yes", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "BOOLEAN_YES", "Si", ""));                 
        ListCENTData1.add(GetDictionary(++ intId, 1, "BOOLEAN_NO", "Não", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "BOOLEAN_NO", "No", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "BOOLEAN_NO", "No", ""));                 
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "DATA_TYPE_INT", "Inteiro", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "DATA_TYPE_INT", "Integer", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "DATA_TYPE_INT", "Inteiro", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "DATA_TYPE_TEXT", "Texto", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "DATA_TYPE_TEXT", "Text", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "DATA_TYPE_TEXT", "Texto", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "DATA_TYPE_DATE", "Data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "DATA_TYPE_DATE", "Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "DATA_TYPE_DATE", "Fecha", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "DATA_TYPE_DOUBLE", "Double", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "DATA_TYPE_DOUBLE", "Double", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "DATA_TYPE_DOUBLE", "Double", ""));                
        ListCENTData1.add(GetDictionary(++ intId, 1, "DATA_TYPE_BOOLEAN", "Booleano", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "DATA_TYPE_BOOLEAN", "Boolean", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "DATA_TYPE_BOOLEAN", "Buleano", ""));                
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_EQUAL", "Igual", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_EQUAL", "Equals", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_EQUAL", "Igual", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_NOT_EQUAL", "Diferente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_NOT_EQUAL", "Not equal", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_NOT_EQUAL", "No es igual", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_GREATER", "Maior que", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_GREATER", "Greater than", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_GREATER", "Mas grande que", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_GREATER_EQUAL", "Maior ou igual", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_GREATER_EQUAL", "Greater or equal than", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_GREATER_EQUAL", "Igual ou mas grande", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_SMALL", "Menor que", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_SMALL", "Smaller than", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_SMALL", "Menor que", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_SMALL_EQUAL", "Menor ou igual", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_SMALL_EQUAL", "Smaller or equal than", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_SMALL_EQUAL", "Menor o igual", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_IN", "Contém", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_IN", "In", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_IN", "Contem", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_NOT_IN", "Não contém", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_NOT_IN", "Not In", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_NOT_IN", "Não contem", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_LIKE", "Similar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_LIKE", "Like", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_LIKE", "Similar", ""));       
        ListCENTData1.add(GetDictionary(++ intId, 1, "OPERATOR_NOT_LIKE", "Não similar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "OPERATOR_NOT_LIKE", "Not Like", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "OPERATOR_NOT_LIKE", "Não similar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FILE_TYPE_UPLOAD", "Arquivo enviado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FILE_TYPE_UPLOAD", "Uploaded file", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FILE_TYPE_UPLOAD", "Arquivo enviado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "FILE_TYPE_CREATED", "Criado pelo sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FILE_TYPE_CREATED", "System created", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FILE_TYPE_CREATED", "Sistema creado", ""));           
        ListCENTData1.add(GetDictionary(++ intId, 1, "FILE_ACTION_NONE", "Manter", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FILE_ACTION_NONE", "No action", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FILE_ACTION_NONE", "Manter", ""));           
        ListCENTData1.add(GetDictionary(++ intId, 1, "FILE_ACTION_RENAME", "Renomear", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FILE_ACTION_RENAME", "Rename", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FILE_ACTION_RENAME", "Renomear", ""));   
        ListCENTData1.add(GetDictionary(++ intId, 1, "FILE_ACTION_DELETE", "Apagar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FILE_ACTION_DELETE", "Delete", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FILE_ACTION_DELETE", "Apagar", ""));           
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT", "Selecionar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT", "Select", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT", "seleccionar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_FIELD", "Selecionar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_FIELD", "Select", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_FIELD", "seleccionar", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_COUNT", "Contar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_COUNT", "Count", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_COUNT", "Contar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_SUM", "Somatória", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_SUM", "Sum", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_SUM", "Suma", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_MAX", "Maximo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_MAX", "Max", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_MAX", "Máximo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_MIN", "Minimo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_MIN", "Min", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_MIN", "Minimo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_AVG", "Média", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_AVG", "Avarage", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_AVG", "Promedio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_SELECT_GROUP_BY", "Agrupar Por", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_SELECT_GROUP_BY", "Group By", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_SELECT_GROUP_BY", "Agrupar Por", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_ORDER_BY_ASC", "Ordenar Asc", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_ORDER_BY_ASC", "Order By Asc", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_ORDER_BY_ASC", "Ordenar Asc", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_ORDER_BY_DESC", "Ordenar Des", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_ORDER_BY_DESC", "Order By Desc", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_ORDER_BY_DESC", "Ordenar Desc", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_CONDITION_AND", "Condição E", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_CONDITION_AND", "Condition And", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_CONDITION_AND", "Condição E", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SQL_COMMAND_CONDITION_OR", "Condição Ou", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SQL_COMMAND_CONDITION_OR", "Condition Or", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SQL_COMMAND_CONDITION_OR", "Condição Ou", ""));                                                

        ListCENTData1.add(GetDictionary(++ intId, 1, "LAYOUT_TYPE_FIXED_POSITION", "Posição Fixa", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LAYOUT_TYPE_FIXED_POSITION", "Fixed Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LAYOUT_TYPE_FIXED_POSITION", "Posición Fija", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "LAYOUT_TYPE_DELIMITED", "Delimitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LAYOUT_TYPE_DELIMITED", "Delimited", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LAYOUT_TYPE_DELIMITED", "Delimitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "LAYOUT_TYPE_SEQUENTIAL", "Sequencial", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LAYOUT_TYPE_SEQUENTIAL", "Sequential", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LAYOUT_TYPE_SEQUENTIAL", "Secuencial", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "SESSION_TYPE_RECORDS", "Registros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SESSION_TYPE_RECORDS", "Records", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SESSION_TYPE_RECORDS", "Archivos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SESSION_TYPE_HEADER", "Cabeçalho", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SESSION_TYPE_HEADER", "Header", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SESSION_TYPE_HEADER", "Encabezamiento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SESSION_TYPE_TRAILER", "Rodapé", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SESSION_TYPE_TRAILER", "Trailer", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SESSION_TYPE_TRAILER", "Remolque", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_RESULT_ALL", "Todos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_RESULT_ALL", "All", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_RESULT_ALL", "Todos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_RESULT_DIFF", "Diferenças", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_RESULT_DIFF", "Differences", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_RESULT_DIFF", "Diferencias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_RESULT_MATCH", "Batidos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_RESULT_MATCH", "Matched", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_RESULT_MATCH", "Apareado", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_STATUS_ORFAN", "Orfão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_STATUS_ORFAN", "Orphan", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_STATUS_ORFAN", "Huérfano", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_STATUS_DIVERGENT", "Divergente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_STATUS_DIVERGENT", "Divergent", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_STATUS_DIVERGENT", "Divergente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_STATUS_MATCH", "Batido", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_STATUS_MATCH", "Matched", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_STATUS_MATCH", "Apareado", "")); 
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_SIDE_1", "Lado 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_SIDE_1", "Side 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_SIDE_1", "Lado 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "MATCH_SIDE_2", "Lado 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MATCH_SIDE_2", "Side 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MATCH_SIDE_2", "Lado 2", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EVENT_ACTION_NEW", "Novo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EVENT_ACTION_NEW", "New", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EVENT_ACTION_NEW", "Nuevo", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EVENT_ACTION_EDIT", "Editar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EVENT_ACTION_EDIT", "Edit", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EVENT_ACTION_EDIT", "Editar", ""));                        
        ListCENTData1.add(GetDictionary(++ intId, 1, "EVENT_ACTION_QUERY", "Consulta", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "EVENT_ACTION_QUERY", "Query", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "EVENT_ACTION_QUERY", "Consulta", ""));                        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RULE_DEFINITION_TYPE_KEY_SEARCH", "Chave de Pesquisa", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RULE_DEFINITION_TYPE_KEY_SEARCH", "Key Search", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RULE_DEFINITION_TYPE_KEY_SEARCH", "Búsqueda por clave", ""));                        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RULE_DEFINITION_TYPE_COMPARE_CRITERIA", "Critério de Comparação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RULE_DEFINITION_TYPE_COMPARE_CRITERIA", "Comparison Criteria", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RULE_DEFINITION_TYPE_COMPARE_CRITERIA", "Criterios de Comparación", ""));       
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RULE_DEFINITION_ABSOLUT", "Absoluto", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RULE_DEFINITION_ABSOLUT", "Absolute", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RULE_DEFINITION_ABSOLUT", "Absoluto", ""));               
        ListCENTData1.add(GetDictionary(++ intId, 1, "RULE_DEFINITION_PERCENTUAL", "Percentual", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RULE_DEFINITION_PERCENTUAL", "Percentage", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RULE_DEFINITION_PERCENTUAL", "Percentual", ""));                       
        ListCENTData1.add(GetDictionary(++ intId, 1, "RULE_DEFINITION_DAYS", "Dias", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RULE_DEFINITION_DAYS", "Days", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RULE_DEFINITION_DAYS", "Dias", ""));                               
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LAYOUT_SOURCE_TEXT_FILE", "Arquivo Texto", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LAYOUT_SOURCE_TEXT_FILE", "Text File", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LAYOUT_SOURCE_TEXT_FILE", "Archivo de texto", ""));                       
        ListCENTData1.add(GetDictionary(++ intId, 1, "LAYOUT_SOURCE_DB_CONNECTION", "Conexão DB", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LAYOUT_SOURCE_DB_CONNECTION", "DB Connection", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LAYOUT_SOURCE_DB_CONNECTION", "Conexión de DB", ""));                                       
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_SUNDAY", "Domingo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_SUNDAY", "Sunday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_SUNDAY", "Domingo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_MONDAY", "Segunda-Feira", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_MONDAY", "Monday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_MONDAY", "Lunes", ""));        
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_TUESDAY", "Terça-Feira", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_TUESDAY", "Tuesday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_TUESDAY", "Martes", ""));                
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_WEDNESDAY", "Quarta-Feira", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_WEDNESDAY", "Wednesday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_WEDNESDAY", "Miércoles", ""));                        
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_THURSDAY", "Quinta-Feira", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_THURSDAY", "Thursday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_THURSDAY", "Jueves", ""));                                
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_FRIDAY", "Sexta-Feira", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_FRIDAY", "Friday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_FRIDAY", "Viernes", ""));                                        
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_SATURDAY", "Sábado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_SATURDAY", "Saturday", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_SATURDAY", "Sábado", ""));                                                
        ListCENTData1.add(GetDictionary(++ intId, 1, "WEEKDAY_DAILY", "Diário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "WEEKDAY_DAILY", "Daily", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "WEEKDAY_DAILY", "Diario", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SCHEDULE_JOB_IMPORT_LAYOUT", "Importar Layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SCHEDULE_JOB_IMPORT_LAYOUT", "Import Layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SCHEDULE_JOB_IMPORT_LAYOUT", "Importar Layout", ""));                                                
        ListCENTData1.add(GetDictionary(++ intId, 1, "SCHEDULE_JOB_EXECUTE_RECON", "Executar Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SCHEDULE_JOB_EXECUTE_RECON", "Execute Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SCHEDULE_JOB_EXECUTE_RECON", "La reconciliación de ejecución", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "JOB_STATUS_PENDING", "Pendente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "JOB_STATUS_PENDING", "Pending", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "JOB_STATUS_PENDING", "Pendiente", ""));                
        ListCENTData1.add(GetDictionary(++ intId, 1, "JOB_STATUS_RUNNING", "Executando", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "JOB_STATUS_RUNNING", "Running", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "JOB_STATUS_RUNNING", "Ejecución", ""));                        
        ListCENTData1.add(GetDictionary(++ intId, 1, "JOB_STATUS_SUCCESS", "Sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "JOB_STATUS_SUCCESS", "Success", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "JOB_STATUS_SUCCESS", "Éxito", ""));                                
        ListCENTData1.add(GetDictionary(++ intId, 1, "JOB_STATUS_FAIL", "Falha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "JOB_STATUS_FAIL", "Fail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "JOB_STATUS_FAIL", "Fallar", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "TRANSACTION_SYSTEM", "Sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRANSACTION_SYSTEM", "System", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRANSACTION_SYSTEM", "Sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRANSACTION_USER", "Usuário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRANSACTION_USER", "User", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRANSACTION_USER", "Usuario", ""));   
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRANSACTION_RECON_AREA", "Área de conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRANSACTION_RECON_AREA", "Reconciliation área", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRANSACTION_RECON_AREA", "Área de conciliacion", ""));           
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SCHEDULE_JOB_IMPORT_DATA", "Importar Dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SCHEDULE_JOB_IMPORT_DATA", "Import Data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SCHEDULE_JOB_IMPORT_DATA", "Importar Dados", ""));           
        ListCENTData1.add(GetDictionary(++ intId, 1, "SCHEDULE_JOB_EXECUTE_RECON", "Executar Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SCHEDULE_JOB_EXECUTE_RECON", "Execute Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SCHEDULE_JOB_EXECUTE_RECON", "Executar Conciliação", ""));           
        ListCENTData1.add(GetDictionary(++ intId, 1, "SCHEDULE_JOB_EXECUTE_SCHEDULE", "Executar Agendamentos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SCHEDULE_JOB_EXECUTE_SCHEDULE", "Execute Schedule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SCHEDULE_JOB_EXECUTE_SCHEDULE", "Executar Programação", ""));           
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "SOLUTION_TYPE_ENTRY_MANAGER", "Gerenciador de Cadastros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SOLUTION_TYPE_ENTRY_MANAGER", "Entry Manager", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SOLUTION_TYPE_ENTRY_MANAGER", "Gerenciador de Cadastros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 1, "SOLUTION_TYPE_RECONCILIATION", "Conciliador", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "SOLUTION_TYPE_RECONCILIATION", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "SOLUTION_TYPE_RECONCILIATION", "Conciliador", ""));
        
        /*
         * General messages
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_SELECT_AN_ITEM", "Nenhuma linha selecionada", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_SELECT_AN_ITEM", "Please select an item", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_SELECT_AN_ITEM", "Por favor, seleccione una línea", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_SAVE_RECORD", "Tem certeza que deseja salvar o registro?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_SAVE_RECORD", "Are you sure you want to save the record?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_SAVE_RECORD", "¿Seguro que deseas guardar el registro?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_DELETE_RECORD", "Tem certeza que deseja apagar o registro?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_DELETE_RECORD", "Are you sure you want to delete the record?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_DELETE_RECORD", "¿Seguro que deseas apagar el registro?", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_DELETE_FILE", "Tem certeza que deseja apagar o arquivo?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_DELETE_FILE", "Are you sure you want to delete the file?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_DELETE_FILE", "¿Seguro que deseas apagar el arquivo?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_IMPORT_RECORD", "Tem certeza que deseja importar o registro?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_IMPORT_RECORD", "Are you sure you want to import the record?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_IMPORT_RECORD", "¿Seguro que deseas importar el registro?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_EXPORT_RECORD", "Tem certeza que deseja exportar o registro?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_EXPORT_RECORD", "Are you sure you want to export the record?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_EXPORT_RECORD", "¿Seguro que deseas exportar el registro?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_RECONCILE_RECORD", "Tem certeza que deseja executar as conciliações selecionadas?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_RECONCILE_RECORD", "Are you sure you want to execute the selected reconciliations?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_RECONCILE_RECORD", "¿Seguro que deseas ejecutar las conciliaciones seleccionadas?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_FILE_IMPORT_SCHEDULED_SUCCESS", "Processo de importação enviado para fila de processamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_FILE_IMPORT_SCHEDULED_SUCCESS", "Importing process successfully sent to processing queue", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_FILE_IMPORT_SCHEDULED_SUCCESS", "Proceso de importación se ha enviado satisfactoriamente a la cola de procesamiento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_FILE_EXPORT_SCHEDULED_SUCCESS", "Processo de exportação enviado para fila de processamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_FILE_EXPORT_SCHEDULED_SUCCESS", "Exporting process successfully sent to processing queue", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_FILE_EXPORT_SCHEDULED_SUCCESS", "Proceso de exportación se ha enviado satisfactoriamente a la cola de procesamiento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_RECONCILE_SCHEDULED_SUCCESS", "Processo de conciliação enviado para fila de processamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_RECONCILE_SCHEDULED_SUCCESS", "Reconciliation process successfully sent to processing queue", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_RECONCILE_SCHEDULED_SUCCESS", "Proceso de conciliación se ha enviado satisfactoriamente a la cola de procesamiento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_RECORD_INSERT_SUCCESS", "Registro incluído com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_RECORD_INSERT_SUCCESS", "Successfully included record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_RECORD_INSERT_SUCCESS", "Con éxito se incluye registro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_RECORD_UPDATE_SUCCESS", "Registro alterado com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_RECORD_UPDATE_SUCCESS", "Successfully updated record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_RECORD_UPDATE_SUCCESS", "Registro actualizado con éxito", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_RECORD_DELETE_SUCCESS", "Registro excluído com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_RECORD_DELETE_SUCCESS", "Successfully deleted record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_RECORD_DELETE_SUCCESS", "Registro excluido con éxito", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_SETUP", "O sistema irá cadastrar o mapeamento e conciliação baseado nos campos dessa visão. Confirma?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_SETUP", "The system will register the mapping and reconciliation based on fields that vision. Confirm?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_SETUP", "¿El sistema registrará el mapeo y la reconciliación basada en campos que la visión. Confirmar?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_DUPLICATE", "Tem certeza que deseja duplicar o registro selecionado?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_DUPLICATE", "Are you sure you want to duplicate selected records", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_DUPLICATE", "¿Está seguro de que desea duplicar la transacción seleccionada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_CONFIRM_REPROCESS", "Tem certeza que deseja reprocessar o registro selecionada?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_CONFIRM_REPROCESS", "Are you sure you want to reprocess selected record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_CONFIRM_REPROCESS", "¿Está seguro de que desea volver a procesar registro seleccionado?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_SETUP_SUCCESS", "Leiaute e conciliação configuradas com sucesso para as visões selecionadas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_SETUP_SUCCESS", "Layout and reconciliation successfully configured reconciliation for selected views", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_SETUP_SUCCESS", "El diseño y la reconciliación configurado correctamente para las vistas seleccionadas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_DUPLICATE_SUCCESS", "Registro duplicados com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_DUPLICATE_SUCCESS", "Records successfully duplicated", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_DUPLICATE_SUCCESS", "Registros duplicados con éxito", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_REPROCESS_SUCCESS", "Registro enviado para processamento com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_REPROCESS_SUCCESS", "Records successfully sent to be reprocessed", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_REPROCESS_SUCCESS", "Los registros enviados con éxito para ser reprocesados", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_NO_INFORMATION_TO_DISPLAY", "Não existem dados para formar o gráfico", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_NO_INFORMATION_TO_DISPLAY", "No information to plot the chart", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_NO_INFORMATION_TO_DISPLAY", "No hay información para trazar el gráfico", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_UPLOAD_SUCCESS", "Arquivos enviados com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_UPLOAD_SUCCESS", "Successfully uploaded files", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_UPLOAD_SUCCESS", "Archivos subidos con éxito", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_UPLOAD_FAIL", "Não foi possível enviar os arquivos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_UPLOAD_FAIL", "Could not send the files", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_UPLOAD_FAIL", "No se pudo enviar los archivos", ""));   
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_SUCCESS", "Importação dos dados - Sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_SUCCESS", "Data Import - Success", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_SUCCESS", "Importación de datos - Éxito", ""));           

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_SUCCESS", "Layout importado com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_SUCCESS", "Could not send the files", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_SUCCESS", "Layout successfully imported", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_FAIL", "Importação de dados - Falha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_FAIL", "Data import - fail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_FAIL", "Importación de datos - fallar", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_FAIL", "Não foi possível importar os dados conforme layout associado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_FAIL", "Could not import the data as associated layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_IMPORT_LAYOUT_FAIL", "No se pudo importar los datos mientras que el diseño asociado", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_RECON_SUCCESS", "Execução de conciliação - Sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_RECON_SUCCESS", "Reconciliation execution - Success", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_RECON_SUCCESS", "La ejecución de la reconciliación - Éxito", ""));           

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_RECON_SUCCESS", "Conciliação executada com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_RECON_SUCCESS", "Conciliation successfully executed", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_RECON_SUCCESS", "Conciliación ejecutado con éxito", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_RECON_FAIL", "Execução de conciliação - Falha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_RECON_FAIL", "Reconciliation execution - Fail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_RECON_FAIL", "La ejecución de la reconciliación - Fallar", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_RECON_FAIL", "Não foi possível executar a conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_RECON_FAIL", "Could not execute the reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_RECON_FAIL", "No se pudo ejecutar la conciliación", ""));
                        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_SCHEDULE_SUCCESS", "Execução de agendamento - Sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_SCHEDULE_SUCCESS", "Schedule execution - Success", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_SCHEDULE_SUCCESS", "Ejecución de programación - El éxito", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_SCHEDULE_SUCCESS", "Agendamento executado com sucesso", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_SCHEDULE_SUCCESS", "Schedule successfully executed", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_SCHEDULE_SUCCESS", "Programación ejecutado con éxito", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_SUBJECT_SCHEDULE_FAIL", "Execução de agendamento - Falha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_SUBJECT_SCHEDULE_FAIL", "Schedule execution - Fail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_SUBJECT_SCHEDULE_FAIL", "Ejecución de programación - Fallar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MESSAGE_MAIL_BODY_SCHEDULE_FAIL", "Não foi possível executar o agendamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MESSAGE_MAIL_BODY_SCHEDULE_FAIL", "Could not execute the schedule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MESSAGE_MAIL_BODY_SCHEDULE_FAIL", "No se pudo ejecutar la Programación", ""));        
        
        /*
         * System functions
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_NEW", "Novo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_NEW", "New", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_NEW", "Nuevo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_EDIT", "Alterar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_EDIT", "Update", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_EDIT", "Actualizar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_DELETE", "Excluir", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_DELETE", "Delete", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_DELETE", "Borrar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_SAVE", "Salvar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_SAVE", "Save", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_SAVE", "Guardar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_FILTER", "Filtrar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_FILTER", "Filter", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_FILTER", "Filtro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_IMPORT", "Importar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_IMPORT", "Import", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_IMPORT", "Importar", ""));
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_EXPORT", "Exportar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_EXPORT", "Export", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_EXPORT", "Exportar", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_RECONCILE", "Conciliar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_RECONCILE", "Reconcile", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_RECONCILE", "Conciliar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_SETUP", "Configurar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_SETUP", "Setup", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_SETUP", "Preparar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_DUPLICATE", "Duplicar registro", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_DUPLICATE", "Duplicate record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_DUPLICATE", "Duplicar registro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FUNCTION_REPROCESS", "Reprocessar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FUNCTION_REPROCESS", "Reprocess", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FUNCTION_REPROCESS", "Reprocesar", ""));
        
        /*
         * Labels
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Username", "Usuário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Username", "Username", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Username", "Usuario", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Password", "Senha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Password", "Password", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Password", "Clave", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Company", "Empresa", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Company", "Company", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Company", "Compañía", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Language", "Idioma", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Language", "Language", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Language", "Idioma", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Login", "Entrar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Login", "Login", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Login", "Entrar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Login.Date", "Data do Sistema", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Login.Date", "System Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Login.Date", "Fecha del sistema", ""));

        /*
         * Home page
         */                              
        ListCENTData1.add(GetDictionary(++ intId, 1, "Homepage.Text1", "Bem vindo ao app1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Homepage.Text1", "Welcome to app1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Homepage.Text1", "Bienvenido a app1", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "Homepage.Text2", "Soluções para automatização de backoffice", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Homepage.Text2", "Solutions for back office automation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Homepage.Text2", "Soluciones para la automatización de backoffice", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "Homepage.Text3", "Gerencie cadastros e relacionamento das informações, crie consultas personalizadas e apresente gráficos em diversos formatos.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Homepage.Text3", "Manage information records and relationships, create personalized queries and display charts in a variety of formats.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Homepage.Text3", "Administrar registros y relacionar la información, crear consultas personalizadas y mostrar gráficos en diversos formatos.", ""));                        

        ListCENTData1.add(GetDictionary(++ intId, 1, "Homepage.Text4", "Configure processos de ETL e tenha uma visão consolidada das posições das suas áreas de negócio.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Homepage.Text4", "Set up ETL processes and have a consolidated view of the positions of your business areas.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Homepage.Text4", "Configure procesos de ETL y tenga una visión consolidada de las posiciones de sus áreas de negocio.", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "Homepage.Text5", "Agende a execução de conciliações definindo frequência e horário conforme sua necessidade, se preferir, execute manualmente.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Homepage.Text5", "Schedule the execution of reconciliations setting frequency and time as you need, if you prefer, perform manually.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Homepage.Text5", "Ejecución de programación de las conciliaciones ajuste de la frecuencia y el tiempo que necesite, si lo prefiere, lleve a cabo de forma manual.", ""));        

        
        /*
         * Buttons
         */              
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.New", "Novo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.New", "New", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.New", "Nuevo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Edit", "Editar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Edit", "Edit", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Edit", "Editar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Save", "Salvar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Save", "Save", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Save", "Guardar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Delete", "Apagar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Delete", "Delete", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Delete", "Borrar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Filter", "Filtrar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Filter", "Filter", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Filter", "Filtro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Cancel", "Cancelar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Cancel", "Cancel", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Cancel", "Cancelar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Back", "Voltar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Back", "Back", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Back", "Volver", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Import", "Importar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Import", "Import", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Import", "Importación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Export", "Exportar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Export", "Export", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Export", "Exportación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Reconciliation", "Conciliar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Reconciliation", "Conciliar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Setup", "Configurar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Setup", "Setup", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Setup", "Preparar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Duplicate", "Duplicar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Duplicate", "Duplicate", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Duplicate", "Duplicar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Reprocess", "Reprocessar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Reprocess", "Reprocess", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Reprocess", "Reprocesar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.ChangePassword", "Alterar Senha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.ChangePassword", "Change Password", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.ChangePassword", "Cambiar Clave", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "Button.Download", "Baixar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Button.Download", "Download", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Button.Download", "Descargar", ""));

        /*
         * Menus
         */              
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_SETUP", "Configurações", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_SETUP", "Setup", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_SETUP", "Preparar", ""));
                
            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ACCESS_CONTROL", "Controle de acesso", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ACCESS_CONTROL", "Access control", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ACCESS_CONTROL", "Controle de acceso", ""));
        
            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_TRANSACTION", "Configurar transações", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_TRANSACTION", "Transactions setup", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_TRANSACTION", "Preparar transacciones", ""));

            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_DICTIONARY", "Configurar dicionário", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_DICTIONARY", "Dictionary setup", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_DICTIONARY", "Preparar diccionario", ""));

            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_SCHEDULE", "Configurar agendamento", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_SCHEDULE", "Scheduler setup", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_SCHEDULE", "Preparar el programador", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ETL", "ETL", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ETL", "ETL", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ETL", "ETL", ""));
        
            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ETL_LAYOUT", "Configurar leiautes", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ETL_LAYOUT", "Layout setup", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ETL_LAYOUT", "Configurar disposición", ""));
            
            ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ETL_TRANSLATION", "Traduções", ""));
            ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ETL_TRANSLATION", "Translations", ""));
            ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ETL_TRANSLATION", "Traducciones", ""));            
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_RECONCILIATION", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_RECONCILIATION", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_RECONCILIATION", "Conciliación", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_RECONCILIATION_SETUP", "Configurar conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_RECONCILIATION_SETUP", "Reconciliation setup", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_RECONCILIATION_SETUP", "Configurar conciliación", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_RECONCILIATION_MATCH", "Batimentos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_RECONCILIATION_MATCH", "Match", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_RECONCILIATION_MATCH", "Batimentos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_RA", "Área de conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_RA", "Reconciliation area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_RA", "Área de conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ENTRY", "Cadastros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ENTRY", "Entry", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ENTRY", "Cadastro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_ENTITY", "Entidades", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_ENTITY", "Entity", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_ENTITY", "Entidad", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "MENU_SESSION", "Sessão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MENU_SESSION", "Session", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MENU_SESSION", "Sesión", ""));        
        
        /*
         * Transactions
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_TABLE", "Tabela", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_TABLE", "Table", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_TABLE", "Datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_MENU", "Menus", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_MENU", "Menus", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_MENU", "Menús", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_TRANSACTION", "Transações", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_TRANSACTION", "Transactions", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_TRANSACTION", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_CATALOG", "Catalogo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_CATALOG", "Catalog", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_CATALOG", "Catálogo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_FIELD_EVENT", "Eventos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_FIELD_EVENT", "Events", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_FIELD_EVENT", "Eventos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_VIEW", "Visões", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_VIEW", "Views", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_VIEW", "Visión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_VIEW_DEF", "Definição da visão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_VIEW_DEF", "View definition", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_VIEW_DEF", "Definición de visión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_DASHBOARD", "Painel", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_DASHBOARD", "Dashboard", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_DASHBOARD", "Tablero", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_DASHBOARD_DEF", "Definição do painel", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_DASHBOARD_DEF", "Dashboard definition", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_DASHBOARD_DEF", "Definición de tablero", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_DOMAIN", "Domínio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_DOMAIN", "Domain", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_DOMAIN", "Dominio", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_COUNTRY", "País", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_COUNTRY", "Country", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_COUNTRY", "Pais", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LANGUAGE", "Idiomas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LANGUAGE", "Language", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LANGUAGE", "Idiomas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_DICTIONARY", "Dicionário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_DICTIONARY", "Dictionary", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_DICTIONARY", "Diccionario", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_COMPANY", "Empresas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_COMPANY", "Companies", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_COMPANY", "Compañías", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_AREA", "Áreas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_AREA", "Areas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_AREA", "Areas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_PROFILE", "Perfis", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_PROFILE", "Profiles", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_PROFILE", "Perfiles", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_PROFILE_TRANSACTION", "Perfil x Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_PROFILE_TRANSACTION", "Profile x Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_PROFILE_TRANSACTION", "Perfil x Transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_TRANSACTION_FUNCTION", "Transações x Funções", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_TRANSACTION_FUNCTION", "Transactions x Functions", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_TRANSACTION_FUNCTION", "Transacciones x Função", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_USER", "Usuário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_USER", "User", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_USER", "Usuario", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_FUNCTION", "Funções", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_FUNCTION", "Functions", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_FUNCTION", "Funciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT", "Leiautes", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT", "Layouts", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT", "Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_SESSION", "Sessões", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_SESSION", "Session", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_SESSION", "Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_SESSION_DEFINITION", "Definir sessões", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_SESSION_DEFINITION", "Define Session", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_SESSION_DEFINITION", "Definir Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_LAYOUT_LOOKUP", "De/para x Leiaute", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_LAYOUT_LOOKUP", "Lookup table x Layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_LAYOUT_LOOKUP", "Tabla de consulta x Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_FUNCTION", "Funções x Leiaute", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_FUNCTION", "Functions x Layouts", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_FUNCTION", "Funciones x Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_LOOKUP", "Tabelas de De/Para", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_LOOKUP", "LookUp Tables", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_LOOKUP", "Tabla de Búsqueda", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LAYOUT_LOOKUP_ITEM", "Definição da tabela de de/para", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LAYOUT_LOOKUP_ITEM", "LookUp table definition", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LAYOUT_LOOKUP_ITEM", "Definición tabla de consulta", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_RECONCILIATION", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_RECONCILIATION", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_RECONCILIATION", "Conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_RECONCILIATION_STEP", "Passos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_RECONCILIATION_STEP", "Steps", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_RECONCILIATION_STEP", "Pasos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_RECONCILIATION_STEP_RULE", "Regras", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_RECONCILIATION_STEP_RULE", "Rules", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_RECONCILIATION_STEP_RULE", "Reglas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_RECONCILIATION_STEP_RULE_FIELD", "Definição das regras", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_RECONCILIATION_STEP_RULE_FIELD", "Rule definition", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_RECONCILIATION_STEP_RULE_FIELD", "Definición de regla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_MATCH", "Batimentos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_MATCH", "Matches", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_MATCH", "Conferencias", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_MATCH_ITEM", "Batimentos/Item", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_MATCH_ITEM", "Matches/Item", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_MATCH_ITEM", "Conferencias/Item", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_SCHEDULE", "Agendamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_SCHEDULE", "Schedule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_SCHEDULE", "Programação", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_SCHEDULE_JOB", "Serviços", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_SCHEDULE_JOB", "Jobs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_SCHEDULE_JOB", "Trabajo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_QUEUE", "Fila de Processamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_QUEUE", "Processing Queue", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_QUEUE", "Fila de Processamento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_FILE_MANAGER", "Gerenciador de Arquivos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_FILE_MANAGER", "File Manager", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_FILE_MANAGER", "Administrador de archivos", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TRN_LOGOUT", "Encerrar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TRN_LOGOUT", "End", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TRN_LOGOUT", "Encerrar", ""));                
        
        /*
         * TRN_TABLE
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "Table.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Table.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Table.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Table.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Table.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Table.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Table.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Table.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Table.Note", "Nota", ""));

        /*
         * TRN_MENU
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Type", "Tipo", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Parent", "Menu pai", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Parent", "Parent menu", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Parent", "Menu papa", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Position", "Posição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Position", "Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Position", "Posición", ""));                
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Menu.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Menu.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Menu.Note", "Nota", ""));
        
        /*
         * TRN_TRANSACTION
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Page", "Página", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Page", "Page", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Page", "Página", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Menu", "Menu", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Menu", "Menu", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Menu", "Menu", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Table", "Tabela", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Table", "Table", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Table", "Tabela", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Transaction.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Transaction.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Transaction.Note", "Nota", ""));
        
        /*
         * TRN_CATALOG
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Transaction", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Transaction", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Transaction", "Transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Label", "Label", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Label", "Label", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Label", "Label", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Type", "Data Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Object", "Objeto", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Object", "Object", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Object", "Objeto", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Size", "Tamanho", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Size", "Size", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Size", "Tamaño", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Nullable", "Nulo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Nullable", "Nullable", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Nullable", "Nulos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Unique", "Único", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Unique", "Unique", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Unique", "Único", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Pk", "PK", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Pk", "PK", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Pk", "PK", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Fk", "FK", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Fk", "FK", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Fk", "FK", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Domain", "Domínio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Domain", "Domain", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Domain", "Dominio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Position", "Posição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Position", "Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Position", "Posición", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Catalog.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Catalog.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Catalog.Note", "Nota", ""));
        
        /*
         * TRN_FIELD_EVENT
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Trn", "Transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Field", "El Campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Action", "Ação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Action", "Action", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Action", "Acción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Event", "Evento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Event", "Event", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Event", "Evento", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Param1", "P1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Param1", "P1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Param1", "P1", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Param2", "P2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Param2", "P2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Param2", "P2", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Param3", "P3", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Param3", "P3", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Param3", "P3", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Param4", "P4", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Param4", "P4", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Param4", "P4", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Param5", "P5", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Param5", "P5", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Param5", "P5", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FieldEvent.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FieldEvent.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FieldEvent.Note", "Nota", ""));
        
        /*
         * TRN_VIEW
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Trn", "Transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Procedure", "Consulta Customizada", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Procedure", "Custom Query", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Procedure", "Consulta Personalizada", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Display", "Visualizar como", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Display", "View as", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Display", "Ver como", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "View.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "View.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "View.Note", "Nota", ""));
        
        
        /*
         * TRN_VIEW_DEF
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.View", "Visão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.View", "View", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.View", "Vision", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Command", "Commando SQL", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Command", "SQL Command", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Command", "Commando SQL", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Transaction", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Transaction", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Transaction", "Transacción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Field", "Campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Operator", "Operador", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Operator", "Operator", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Operator", "Operador", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Value", "Valor", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Value", "Value", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Value", "Valor", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Position", "Posição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Position", "Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Position", "Posición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ViewDef.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ViewDef.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ViewDef.Note", "Nota", ""));

        /*
         * TRN_COUNTRY
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "Country.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Country.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Country.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Country.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Country.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Country.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Country.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Country.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Country.Note", "Nota", ""));
        
        /*
         * TRN_LANGUAGE
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "Language.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Language.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Language.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Language.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Language.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Language.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Language.Country", "Código do País", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Language.Country", "Country Code", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Language.Country", "Código de País", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Language.Language", "Código do Idioma", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Language.Language", "Language Code", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Language.Language", "Código de Idioma", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Language.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Language.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Language.Note", "Nota", ""));
                
        /*
         * TRN_DICTIONARY
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Dictionary.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Dictionary.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Dictionary.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Dictionary.Language", "Idioma", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Dictionary.Language", "Language", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Dictionary.Language", "Idioma", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Dictionary.Code", "Código", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Dictionary.Code", "Code", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Dictionary.Code", "Código", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Dictionary.Description", "Descrição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Dictionary.Description", "Description", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Dictionary.Description", "Descripción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Dictionary.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Dictionary.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Dictionary.Note", "Nota", ""));
        
        /*
         * TRN_DOMAIN
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.IdDomain", "Id Dominio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.IdDomain", "Id Domain", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.IdDomain", "Id Dominio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.Description", "Descrição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.Description", "Description", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.Description", "Descripción", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Domain.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Domain.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Domain.Note", "Nota", ""));

        /*
         * TRN_COMPANY
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Id", "Id", ""));    
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Name", "Nombre", ""));  
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Document", "CPF/CNPJ", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Document", "Docto/Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Document", "Docto/Id", ""));      

        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Country", "País", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Country", "Country", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Country", "Pais", ""));      

        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Language", "Idioma", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Language", "Language", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Language", "Idioma", ""));      
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Email", "Correo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.ExpireDate", "Expira em", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.ExpireDate", "Expire Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.ExpireDate", "Fecha de caducidad", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.NumberOfReconcile", "Processos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.NumberOfReconcile", "Process", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.NumberOfReconcile", "Procesos", ""));                
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.DaysToKeepHistory", "Histórico (dias)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.DaysToKeepHistory", "History (Days)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.DaysToKeepHistory", "Historia (Dias)", ""));                

        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.NumberOfLine", "Limite de linhas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.NumberOfLine", "Line limit", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.NumberOfLine", "Limite de linhas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Type", "Tipo", ""));                
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Company.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Company.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Company.Note", "Nota", ""));
        
        
        /*
         * TRN_DICTIONARY
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Area.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Area.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Area.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Area.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Area.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Area.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Area.Company", "Empresa", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Area.Company", "Company", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Area.Company", "compañía", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Area.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Area.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Area.Email", "Correo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Area.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Area.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Area.Note", "Nota", ""));
        
        /*
         * TRN_PROFILE
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Profile.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Profile.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Profile.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Profile.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Profile.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Profile.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Profile.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Profile.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Profile.Email", "Correo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Profile.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Profile.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Profile.Note", "Nota", ""));

        /*
         * TRN_PROFILE_TRANSACTION
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "ProfileTransaction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ProfileTransaction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ProfileTransaction.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ProfileTransaction.Profile", "Perfil", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ProfileTransaction.Profile", "Profile", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ProfileTransaction.Profile", "Perfile", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ProfileTransaction.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ProfileTransaction.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ProfileTransaction.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ProfileTransaction.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ProfileTransaction.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ProfileTransaction.Note", "Nota", ""));
        
        /*
         * TRN_TRANSACTION_FUNCTION
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "TransactionFunction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TransactionFunction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TransactionFunction.Id", "Id", ""));
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "TransactionFunction.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TransactionFunction.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TransactionFunction.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TransactionFunction.Function", "Função", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TransactionFunction.Function", "Function", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TransactionFunction.Function", "Función", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TransactionFunction.Profile", "Perfil", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TransactionFunction.Profile", "Profile", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TransactionFunction.Profile", "Perfile", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "TransactionFunction.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "TransactionFunction.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "TransactionFunction.Note", "Nota", ""));
        
        /*
         * TRN_USER
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Username", "Usuário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Username", "Username", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Username", "Usuario", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Password", "Senha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Password", "Password", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Password", "Clave", ""));
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Profile", "Perfil", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Profile", "Profile", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Profile", "Perfile", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Area", "Área", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Area", "Area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Area", "Area", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Email", "E-mail", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Email", "Correo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "User.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "User.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "User.Note", "Nota", ""));        
        
        /*
         * TRN_FUNCTION
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "Function.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Function.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Function.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Function.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Function.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Function.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Function.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Function.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Function.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Function.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Function.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Function.Note", "Nota", ""));
        
        /*
         * TRN_LAYOUT
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Area", "Área", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Area", "Area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Area", "Area", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Type", "Leyout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Transaction", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Transaction", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Transaction", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Input", "Fonte de dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Input", "Data source", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Input", "Entrada", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Output", "Arquivo de Saída", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Output", "Output file", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Output", "Arquivo de Salída", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.LineStart", "Ler a partir da linha", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.LineStart", "Start line", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.LineStart", "Comience en línea", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Position", "Posição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Position", "Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Position", "Posição", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Size", "Tamanho", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Size", "Size", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Size", "Tamanho", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.FileAction", "Arquivo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.FileAction", "File", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.FileAction", "Arquivo", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Layout.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Layout.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Layout.Note", "Nota", ""));
        
        /*
         * TRN_LAYOUT_SESSION
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.Layout", "Leiaute", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.Layout", "Layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.Layout", "Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.Name", "Nome da sessão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.Name", "Session name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.Name", "Nombre de Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.SessionType", "Tipo da Sessão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.SessionType", "Session Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.SessionType", "Tipo de Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.SessionIdentifier", "Identificador da Sessão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.SessionIdentifier", "Session Identifier", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.SessionIdentifier", "Identificador de Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.DataFormat", "Formato dos Dados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.DataFormat", "Data Format", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.DataFormat", "Formato de Datos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.Delimiter", "Delimitador", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.Delimiter", "Delimiter", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.Delimiter", "Delimitador", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutSession.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutSession.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutSession.Note", "Nota", ""));
                
        /*
         * TRN_LAYOUT_DEF
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Layout", "Leiaute", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Layout", "Layout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Layout", "Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Session", "Sessão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Session", "Session", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Session", "Sesión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.StartPosition", "Posição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.StartPosition", "Position", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.StartPosition", "Posición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Size", "Tamanho", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Size", "Size", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Size", "Tamaño", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Field", "El campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Mask", "Máscara", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Mask", "Mask", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Mask", "Máscara", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Default", "Valor Padrão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Default", "Default Value", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Default", "Valor Defecto", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.FillSide", "Preencher", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.FillSide", "Fill Side", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.FillSide", "Relleno", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.FillChar", "Caractér", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.FillChar", "Character", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.FillChar", "Character", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutDef.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutDef.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutDef.Note", "Nota", ""));
        
        
        /*
         * TRN_LAYOUT_LOOKUP
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Note", "Nota", ""));
                
        /*
         * TRN_LAYOUT_LOOKUP_ITEM
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUpItem.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUpItem.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUpItem.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUpItem.LookUp", "Nome da tabelas de De/Para", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUpItem.LookUp", "LookUp Table Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUpItem.LookUp", "Nombre Tabla de Búsqueda", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUpItem.Code", "Código", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUpItem.Code", "Code", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUpItem.Code", "Código", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUpItem.Description", "Descrição", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUpItem.Description", "Description", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUpItem.Description", "Descripción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUpItem.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUpItem.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUpItem.Note", "Nota", ""));
        
        
        /*
         * TRN_LOOKUP
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Layout", "Nome do Leiaout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Layout", "Layout Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Layout", "Nombre de la Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Field", "El campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.LookUp", "Tradução", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.LookUp", "LookUp", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.LookUp", "Traducción", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Refuse", "Ignorar linha caso não encontre?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Refuse", "Discard line if not found?", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Refuse", "Desechar la línea si no lo encuentra ?", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutLookUp.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutLookUp.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutLookUp.Note", "Nota", ""));
        
        
        /*
         * TRN_LAYOUT_FUNCTION
         */                
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Layout", "Nome do Leiaout", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Layout", "Layout Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Layout", "Nombre de la Disposición", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Name", "Nome", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Name", "Name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Name", "Nombre", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Field", "El campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Function", "Função", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Function", "Function", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Function", "Função", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Parameter1", "P1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Parameter1", "P1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Parameter1", "P1", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Parameter2", "P2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Parameter2", "P2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Parameter2", "P2", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "LayoutFunction.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "LayoutFunction.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "LayoutFunction.Note", "Nota", ""));
        
        /*
         * TRN_RECONCILIATION
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Name", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Name", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Name", "conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Area", "Área", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Area", "Area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Area", "Area", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Trn", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Trn", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Trn", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Type", "Tipo", ""));
                
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.View1", "Visão 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.View1", "View 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.View1", "Visión 1", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.View2", "Visão 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.View2", "View 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.View2", "Visión 2", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Results", "Resultados", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Results", "Results", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Results", "Resultados", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Reconcile.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Reconcile.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Reconcile.Note", "Nota", ""));        
        
        /*
         * TRN_RECONCILIATION_STEP
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Reconciliation", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Reconciliation", "Conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Name", "Passo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Name", "Step", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Name", "Paso", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Code1", "Chave 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Code1", "Key 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Code1", "Llave 1", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Code2", "Chave 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Code2", "Key 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Code2", "Llave 2", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Enabled", "Habilitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Enabled", "Enabled", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Enabled", "Habilitado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStep.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStep.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStep.Note", "Nota", ""));
        
        /*
         * TRN_RECONCILIATION_STEP_RULE
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Reconciliation", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Reconciliation", "Conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Step", "Passo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Step", "Step", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Step", "Paso", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Name", "Regra", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Name", "Rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Name", "Regla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Enabled", "Habilitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Enabled", "Enabled", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Enabled", "Habilitado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRule.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRule.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRule.Note", "Nota", ""));
        
        /*
         * TRN_RECONCILIATION_STEP_RULE_FIELD
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Reconciliation", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Reconciliation", "Conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Step", "Passos", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Step", "Steps", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Step", "Pasos", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Rule", "Regra", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Rule", "Rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Rule", "Regla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Transaction", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Transaction", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Transaction", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Field", "Campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Operator", "Operador", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Operator", "Operator", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Operator", "Operador", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Tol", "Tolerância", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Tol", "Tolerance", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Tol", "Tolerancia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.TolType", "Tipo de Tol.", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.TolType", "Tol. Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.TolType", "Tipo de Tol.", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Enabled", "Habilitado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Enabled", "Enabled", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Enabled", "Habilitado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ReconcileStepRuleField.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ReconcileStepRuleField.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ReconcileStepRuleField.Note", "Nota", ""));
        
        /*
         * TRN_MATCH
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.IdProcess", "Id Processo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.IdProcess", "Id Process", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.IdProcess", "Id Processo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Date", "Data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Date", "Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Date", "Fecha", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Reconciliation", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Reconciliation", "Conciliación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Step", "Passo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Step", "Step", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Step", "Paso", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Rule", "Regra", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Rule", "Rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Rule", "Regla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Side", "Lado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Side", "Side", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Side", "Lado", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Status", "Status", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Transaction", "Transação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Transaction", "Transaction", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Transaction", "Transacciones", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Field", "Campo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Diff", "Diferença", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Diff", "Difference", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Diff", "Diferencia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.IdMatch", "Batimento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.IdMatch", "Match", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.IdMatch", "Conferencia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Match.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Match.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Match.Note", "Nota", ""));
        
        /*
         * TRN_MATCH_ITEM
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.IdProcess", "Id Processo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.IdProcess", "Id Process", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.IdProcess", "IdProcesso", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.IdMatch", "Id Batimento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.IdMatch", "Id Match", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.IdMatch", "Id Conferencia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.IdRecord", "Id Registro", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.IdRecord", "Id Record", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.IdRecord", "Id Registro", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.IdRule", "Id Regra", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.IdRule", "Id Rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.IdRule", "Id Regla", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "MatchItem.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "MatchItem.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "MatchItem.Note", "Nota", ""));
        
        /*
         * TRN_SCHEDULE
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Name", "Agendamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Name", "Schedule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Name", "Programación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Area", "Área", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Area", "Area", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Area", "Area", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Recurrence", "Recorrência", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Recurrence", "Recurrence", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Recurrence", "recurrencia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Time", "Hora", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Time", "Time", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Time", "Hora", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Schedule.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Schedule.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Schedule.Note", "Nota", ""));
                
        /*
         * TRN_SCHEDULE_STEP
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Name", "Passo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Name", "Step", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Name", "Paso", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Schedule", "Agendamento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Schedule", "Scheduler", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Schedule", "Programación", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Type", "Tipo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Service", "Id do Serviço", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Service", "Service Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Service", "Id do Servicio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "ScheduleStep.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "ScheduleStep.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "ScheduleStep.Note", "Nota", ""));
        
        /*
         * TRN_QUEUE
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Date", "Data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Date", "Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Date", "Fecha", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Type", "Tipo de Fila", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Type", "Queue Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Type", "Tipo de Fila", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Service", "ID do Serviço", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Service", "Service Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Service", "ID do Servicio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Name", "Nome do Serviço", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Name", "Service name", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Name", "Nombre de Servicio", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.StartDate", "Inicio da Execução", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.StartDate", "Start Time", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.StartDate", "Inicio de la ejecución", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.EndDate", "Fim da Execução", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.EndDate", "End Time", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.EndDate", "Final de la ejecución", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Description", "Mensagem", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Description", "Message", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Description", "Mensagem", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Status", "Estatus", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.User", "Usuário", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.User", "User", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.User", "Usuário", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Queue.Note", "Obs", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Queue.Note", "Note", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Queue.Note", "Nota", ""));
                
        /*
         * TRN_FILE_MANAGER
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "FileManager.File", "Arquivos disponíveis", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FileManager.File", "Available files", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FileManager.File", "archivos disponibles", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "FileManager.Action", "Ação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "FileManager.Action", "Action", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "FileManager.Action", "Acción", ""));       
        
        /*
         * Functions used to translate data
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Replace()", "Substituir(P1, P2)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Replace()", "Replace(P1, P2)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Replace()", "Reemplazar(P1, P2)", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Mid()", "Meio(Posição Inicial, Posição Final)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Mid()", "Meio(Start Position, End Position)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Mid()", "Meio(Texto, Tamanho)", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Right()", "Direita(Tamanho)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Right()", "Right(Size)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Right()", "Derecho(Tamanho)", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Left()", "Esquerda(Tamanho)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Left()", "Left(Size)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Left()", "Izquierda(Tamanho)", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Join()", "Unir(Campo, Valor, Campo, Valor)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Join()", "Join(Field, Value, Field, Value)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Join()", "Unir(Campo, Valor, Campo, Valor)", ""));
        
        /*
         * Labels
         */        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.Select", "Selecionar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.Select", "Select", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.Select", "Selecionar", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.Day", "Dia(s)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.Day", "Day(s)", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.Day", "Dia(s)", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.View", "Visão", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.View", "View", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.View", "Visión", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.RecordCount", "Total de registros", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.RecordCount", "Record count", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.RecordCount", "Número de registros", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.Pages", "Total de páginas", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.Pages", "Page count", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.Pages", "Número de páginas", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.LinePage", "Linhas por página", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.LinePage", "Lines per page", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.LinePage", "Líneas por página", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.GoPage", "Ver página", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.GoPage", "Go page", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.GoPage", "Ver página", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.select_count", "Contar", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.select_count", "Count", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.select_count", "Contar", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.select_sum", "Somatória", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.select_sum", "Sum", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.select_sum", "Suma", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.select_max", "Maior", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.select_max", "Maximum", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.select_max", "Máximo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.select_min", "Menor", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.select_min", "Minimum", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.select_min", "Mínimo", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "Label.select_avg", "Média", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "Label.select_avg", "Avarage", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "Label.select_avg", "Média", ""));

        /*
         * Reconciliation Areas
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Id", "Id", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Id", "Id", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.MatchId", "Id Batimento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.MatchId", "Id Match", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.MatchId", "Id Conferencia", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.ProcessId", "Id Processo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.ProcessId", "Id Process", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.ProcessId", "Id Proceso", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.MatchDate", "Data do Batimento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.MatchDate", "Match Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.MatchDate", "Data da Conferencia", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Reconciliation", "Conciliação", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Reconciliation", "Reconciliation", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Reconciliation", "Conferencia", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Step", "Passo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Step", "Step", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Step", "Paso", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Rule", "Regra", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Rule", "Rule", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Rule", "Regla", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Status", "Status", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Status", "Status", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Field", "Campo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Field", "Field", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Field", "Campo", ""));                        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Difference", "Diferenças", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Difference", "Difference", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Difference", "Diferencia", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Side", "Lado", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Side", "Side", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Side", "Lado", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.DataSource", "Origem", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.DataSource", "Source", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.DataSource", "Fuente", ""));
        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Ticket", "Boleta", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Ticket", "Ticket", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Ticket", "Boleta", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Date", "Data", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Date", "Date", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Date", "Fecha", ""));                
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Type", "Tipo", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Type", "Type", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Type", "Tipo", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Currency", "Moeda", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Currency", "Currency", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Currency", "Moneda", ""));       
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Amount", "Quantidade", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Amount", "Amount", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Amount", "Quantidade", ""));

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Price", "Preço", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Price", "Price", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Price", "Preço", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Settlement", "Vencimento", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Settlement", "Settlement", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Settlement", "Vencimento", ""));        
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Fixing", "Fixing", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Fixing", "Fixing", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Fixing", "Fixing", ""));                        

        ListCENTData1.add(GetDictionary(++ intId, 1, "RA.Counterparty", "Contraparte", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "RA.Counterparty", "Counterparty", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "RA.Counterparty", "Contraparte", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "COMPANY_1", "Empresa 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "COMPANY_1", "Company 1", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "COMPANY_1", "Empresa 1", ""));        

        ListCENTData1.add(GetDictionary(++ intId, 1, "COMPANY_2", "Empresa 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "COMPANY_2", "Company 2", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "COMPANY_2", "Empresa 2", ""));        

        /*
         * Default areas
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "AREA_FX", "Cambio", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "AREA_FX", "FX", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "AREA_FX", "Cambio", ""));

        /*
         * Default areas
         */
        ListCENTData1.add(GetDictionary(++ intId, 1, "PROFILE_ADMIN", "Administrador", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "PROFILE_ADMIN", "Administrator", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "PROFILE_ADMIN", "Administrador", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "PROFILE_MANAGER", "Gerente", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "PROFILE_MANAGER", "Manager", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "PROFILE_MANAGER", "Gerente", ""));
        
        ListCENTData1.add(GetDictionary(++ intId, 1, "PROFILE_ANALYST", "Analista", ""));
        ListCENTData1.add(GetDictionary(++ intId, 2, "PROFILE_ANALYST", "Analyst", ""));
        ListCENTData1.add(GetDictionary(++ intId, 3, "PROFILE_ANALYST", "Analista", ""));        
        
        
        return ListCENTData1;
    }    

    public List<CENTData> GetListOfFieldEvents() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_VIEW_DEF, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_VIEW_DEF, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_VIEW_DEF, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_VIEW_DEF, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_layout", EVENT_NEW, 1, 1, "id_session", "this.options[this.selectedIndex].value", String.valueOf(TRN_LAYOUT_SESSION), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_layout", EVENT_EDIT, 1, 1, "id_session", "this.options[this.selectedIndex].value", String.valueOf(TRN_LAYOUT_SESSION), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_layout", EVENT_FILTER, 1, 1, "id_session_24", "this.options[this.selectedIndex].value", String.valueOf(TRN_LAYOUT_SESSION), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_LAYOUT_SESSION_DEFINITION, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));       
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_LAYOUT_LAYOUT_LOOKUP, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_FUNCTION, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_FUNCTION, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_LAYOUT_FUNCTION, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_LAYOUT_FUNCTION, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_reconcile", EVENT_NEW, 1, 1, "id_step", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_reconcile", EVENT_EDIT, 1, 1, "id_step", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_reconcile", EVENT_FILTER, 1, 1, "id_step_" + TRN_RECONCILIATION_STEP_RULE_FIELD, "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_step", EVENT_NEW, 1, 1, "id_rule", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_step", EVENT_EDIT, 1, 1, "id_rule", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_step", EVENT_FILTER, 1, 1, "id_rule_" + TRN_RECONCILIATION_STEP_RULE_FIELD, "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_rule", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE_FIELD), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_rule", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE_FIELD), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_rule", EVENT_FILTER, 1, 1, "id_field_" + TRN_RECONCILIATION_STEP_RULE_FIELD, "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE_FIELD), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_RECONCILIATION_STEP_RULE_FIELD, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));        
        
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_reconcile", EVENT_NEW, 1, 1, "id_step", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_reconcile", EVENT_EDIT, 1, 1, "id_step", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_reconcile", EVENT_FILTER, 1, 1, "id_step_" + TRN_MATCH, "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_step", EVENT_NEW, 1, 1, "id_rule", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_step", EVENT_EDIT, 1, 1, "id_rule", "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_step", EVENT_FILTER, 1, 1, "id_rule_" + TRN_MATCH, "this.options[this.selectedIndex].value", String.valueOf(TRN_RECONCILIATION_STEP_RULE), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_transaction", EVENT_NEW, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_transaction", EVENT_EDIT, 1, 1, "id_field", "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
        ListCENTData1.add(GetFieldEvent(++ intId, TRN_MATCH, "id_transaction", EVENT_FILTER, 1, 1, "id_field_" + TRN_MATCH, "this.options[this.selectedIndex].value", String.valueOf(TRN_CATALOG), "", "",""));
     
        return ListCENTData1;
    }    

    public List<CENTData> GetListOfDomains() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        ListCENTData1.add(GetDomain(++ intId, "domain_1", 1, "DATA_TYPE_INT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_1", 2, "DATA_TYPE_TEXT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_1", 3, "DATA_TYPE_DATE", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_1", 4, "DATA_TYPE_DOUBLE", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_1", 5, "DATA_TYPE_BOOLEAN", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_2", 1, "BOOLEAN_YES", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_2", 2, "BOOLEAN_NO", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 1, "SQL_COMMAND_SELECT_FIELD", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 2, "SQL_COMMAND_SELECT_COUNT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 3, "SQL_COMMAND_SELECT_SUM", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 4, "SQL_COMMAND_SELECT_MAX", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 5, "SQL_COMMAND_SELECT_MIN", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 6, "SQL_COMMAND_SELECT_AVG", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 7, "SQL_COMMAND_CONDITION_AND", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 8, "SQL_COMMAND_CONDITION_OR", DOMAIN_SYSTEM, ""));             
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 9, "SQL_COMMAND_SELECT_GROUP_BY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 10, "SQL_COMMAND_ORDER_BY_ASC", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_3", 11, "SQL_COMMAND_ORDER_BY_DESC", DOMAIN_SYSTEM, ""));  
                
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 1, "OPERATOR_EQUAL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 2, "OPERATOR_NOT_EQUAL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 3, "OPERATOR_GREATER", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 4, "OPERATOR_GREATER_EQUAL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 5, "OPERATOR_SMALL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 6, "OPERATOR_SMALL_EQUAL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 7, "OPERATOR_IN", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 8, "OPERATOR_NOT_IN", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 9, "OPERATOR_LIKE", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_18", 10, "OPERATOR_NOT_LIKE", DOMAIN_SYSTEM, ""));        
        
        ListCENTData1.add(GetDomain(++ intId, "domain_4", 1, "", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_4", 2, "", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_4", 3, "", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 1, "Grid", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 2, "column", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 3, "line", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 4, "bar", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 5, "pie", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 6, "doughnut", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 7, "spline", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_5", 8, "splineArea", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_6", 1, "LAYOUT_TYPE_FIXED_POSITION", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_6", 2, "LAYOUT_TYPE_DELIMITED", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_6", 3, "LAYOUT_TYPE_SEQUENTIAL", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_7", 1, "SESSION_TYPE_RECORDS", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_7", 2, "SESSION_TYPE_HEADER", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_7", 3, "SESSION_TYPE_TRAILER", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_8", 1, "1:1", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_8", 2, "1:M", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_8", 3, "M:M", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_9", 1, "MATCH_RESULT_ALL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_9", 2, "MATCH_RESULT_DIFF", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_9", 3, "MATCH_RESULT_MATCH", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_10", 1, "MATCH_STATUS_ORFAN", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_10", 2, "MATCH_STATUS_DIVERGENT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_10", 3, "MATCH_STATUS_MATCH", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_11", 1, "MATCH_SIDE_1", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_11", 2, "MATCH_SIDE_2", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_12", 3, "EVENT_ACTION_NEW", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_12", 4, "EVENT_ACTION_EDIT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_12", 8, "EVENT_ACTION_QUERY", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_13", 1, "onChange", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_13", 2, "onClick", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_13", 3, "onBlur", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_14", 1, "SetDropDown", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_15", 1, "RULE_DEFINITION_TYPE_KEY_SEARCH", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_15", 2, "RULE_DEFINITION_TYPE_COMPARE_CRITERIA", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_16", 1, "RULE_DEFINITION_ABSOLUT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_16", 2, "RULE_DEFINITION_PERCENTUAL", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_16", 3, "RULE_DEFINITION_DAYS", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_17", 1, "Replace()", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_17", 2, "Mid()", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_17", 3, "Right()", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_17", 4, "Left()", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_17", 5, "Join()", DOMAIN_SYSTEM, ""));

        ListCENTData1.add(GetDomain(++ intId, "domain_19", 1, "FILE_TYPE_UPLOAD", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_19", 2, "FILE_TYPE_CREATED", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_20", 1, "LAYOUT_SOURCE_TEXT_FILE", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_20", 2, "LAYOUT_SOURCE_DB_CONNECTION", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_21", 1, "", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_21", 2, "", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 1, "WEEKDAY_SUNDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 2, "WEEKDAY_MONDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 3, "WEEKDAY_TUESDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 4, "WEEKDAY_WEDNESDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 5, "WEEKDAY_THURSDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 6, "WEEKDAY_FRIDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 7, "WEEKDAY_SATURDAY", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_22", 8, "WEEKDAY_DAILY", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_23", 1, "SCHEDULE_JOB_IMPORT_LAYOUT", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_23", 2, "SCHEDULE_JOB_EXECUTE_RECON", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_24", 1, "JOB_STATUS_PENDING", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_24", 2, "JOB_STATUS_RUNNING", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_24", 3, "JOB_STATUS_SUCCESS", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_24", 4, "JOB_STATUS_FAIL", DOMAIN_SYSTEM, ""));

        ListCENTData1.add(GetDomain(++ intId, "domain_25", 1, "FILE_ACTION_NONE", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_25", 2, "FILE_ACTION_RENAME", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_25", 3, "FILE_ACTION_DELETE", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_26", 1, "TRANSACTION_SYSTEM", DOMAIN_SYSTEM, "Transações de sistema like Profile or Transaction, cannot be deleted"));
        ListCENTData1.add(GetDomain(++ intId, "domain_26", 2, "TRANSACTION_USER", DOMAIN_SYSTEM, "Users can create or delete as their needs. No restriction applied"));
        ListCENTData1.add(GetDomain(++ intId, "domain_26", 3, "TRANSACTION_RECON_AREA", DOMAIN_SYSTEM, "Append additional fields to the transaction, used to control etl and reconciliation process"));        
        
        ListCENTData1.add(GetDomain(++ intId, "domain_27", 1, "SCHEDULE_JOB_IMPORT_DATA", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_27", 2, "SCHEDULE_JOB_EXECUTE_RECON", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_27", 3, "SCHEDULE_JOB_EXECUTE_SCHEDULE", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_28", 1, "SOLUTION_TYPE_ENTRY_MANAGER", DOMAIN_SYSTEM, ""));
        ListCENTData1.add(GetDomain(++ intId, "domain_28", 2, "SOLUTION_TYPE_RECONCILIATION", DOMAIN_SYSTEM, ""));
        
        ListCENTData1.add(GetDomain(++ intId, "domain_29", 1, "TRANSACTION_SYSTEM", DOMAIN_SYSTEM, "Transações de sistema like Profile or Transaction, cannot be deleted"));
        ListCENTData1.add(GetDomain(++ intId, "domain_29", 2, "TRANSACTION_USER", DOMAIN_SYSTEM, "Users can create or delete as their needs. No restriction applied"));        
     
        return ListCENTData1;
    }        

    public List<CENTData> GetListOfAreas() throws Exception
    {
        int intIdCompany = 1;
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        ListCENTData1.add(GetArea(AREA_FX, intIdCompany, "Area FX", "act@reconapp.com.br", ""));
        ListCENTData1.add(GetArea(AREA_CC, intIdCompany, "Area CC", "act@reconapp.com.br", ""));
     
        return ListCENTData1;
    }        


    public List<CENTData> GetListOfProfiles() throws Exception
    {
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        ListCENTData1.add(GetProfile(PROFILE_ADMIN, "PROFILE_ADMIN", "administrator@reconapp.com.br", ""));
        ListCENTData1.add(GetProfile(PROFILE_MANAGER, "PROFILE_MANAGER", "manager@reconapp.com.br", ""));
        ListCENTData1.add(GetProfile(PROFILE_ANALYST, "PROFILE_ANALYST", "analyst@reconapp.com.br", ""));

        return ListCENTData1;
    }
    
    public List<CENTData> GetListOfUsers(int intIdArea) throws Exception
    {
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetUser(0, "admin", "admin", PROFILE_ADMIN, intIdArea, "admin@reconapp.com.br", ""));
        ListCENTData1.add(GetUser(0, "david", "david", PROFILE_MANAGER, intIdArea, "david@reconapp.com.br", ""));
        ListCENTData1.add(GetUser(0, "ana", "ana", PROFILE_ANALYST, intIdArea, "ana@reconapp.com.br", ""));
        ListCENTData1.add(GetUser(0, "maria", "maria", PROFILE_ANALYST, intIdArea, "maria@reconapp.com.br", ""));        
     
        return ListCENTData1;
    }    


    public List<CENTData> GetListOfFunctions() throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        ListCENTData1.add(GetFunction(FUNCTION_NEW, "FUNCTION_NEW", ""));
        ListCENTData1.add(GetFunction(FUNCTION_EDIT, "FUNCTION_EDIT", ""));
        ListCENTData1.add(GetFunction(FUNCTION_DELETE, "FUNCTION_DELETE", ""));
        ListCENTData1.add(GetFunction(FUNCTION_SAVE, "FUNCTION_SAVE", ""));
        ListCENTData1.add(GetFunction(FUNCTION_FILTER, "FUNCTION_FILTER", ""));
        ListCENTData1.add(GetFunction(FUNCTION_IMPORT, "FUNCTION_IMPORT", ""));
        ListCENTData1.add(GetFunction(FUNCTION_EXPORT, "FUNCTION_EXPORT", ""));
        ListCENTData1.add(GetFunction(FUNCTION_RECONCILE, "FUNCTION_RECONCILE", ""));
        ListCENTData1.add(GetFunction(FUNCTION_SETUP, "FUNCTION_SETUP", ""));
        ListCENTData1.add(GetFunction(FUNCTION_DUPLICATE, "FUNCTION_DUPLICATE", ""));
        ListCENTData1.add(GetFunction(FUNCTION_REPROCESS, "FUNCTION_REPROCESS", ""));

        return ListCENTData1;
    }

    public List<CENTData> GetListOfProfileTransaction(int intIdProfile) throws Exception
    {
        int i = 0;

        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        
        switch (intIdProfile)
        {
            case PROFILE_OWNER:
            case PROFILE_ADMIN:    
                
                for (i=1; i<=TRN_COUNT; i++)
                {
                    ListCENTData1.add(GetProfileTransaction(0, intIdProfile, i, ""));
                }            
                
                break;
                
            case PROFILE_MANAGER:    

                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_AREA, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_PROFILE, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_PROFILE_TRANSACTION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_TRANSACTION_FUNCTION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_USER, ""));
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_SESSION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_SESSION_DEFINITION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_LAYOUT_LOOKUP, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_FUNCTION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_LOOKUP, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LAYOUT_LOOKUP_ITEM, ""));
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_RECONCILIATION, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_RECONCILIATION_STEP, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_RECONCILIATION_STEP_RULE, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_RECONCILIATION_STEP_RULE_FIELD, ""));
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_MATCH, ""));
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_SCHEDULE, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_SCHEDULE_JOB, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_QUEUE, ""));
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_LOGOUT, ""));                
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_MANAGER, TRN_FILE_MANAGER, ""));            
                
                break;
                
            case PROFILE_ANALYST:
                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_SCHEDULE, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_SCHEDULE_JOB, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_QUEUE, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_MATCH, ""));                
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_LOGOUT, ""));
                ListCENTData1.add(GetProfileTransaction(0, PROFILE_ANALYST, TRN_FILE_MANAGER, ""));            
                                
                break;
        }
        
        return ListCENTData1;
    }

    public List<CENTData> GetListOfTransactionFunction(int intIdProfile) throws Exception
    {
        int i = 0;
        int j = 0;
        int intId = 0;            

        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTProfileTransaction1 = null;
        List<CENTData> ListCENTCatalogProfileTransaction1 = null;
                
        ListCENTCatalogProfileTransaction1 = this.GetCatalog(TRN_PROFILE_TRANSACTION);
        
        
        switch (intIdProfile)
        {            
            case PROFILE_OWNER:
            case PROFILE_ADMIN:
                
                for (i=1; i<=TRN_COUNT; i++)
                {            
                    switch (i)
                    {
                        case TRN_LAYOUT:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_IMPORT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;

                        case TRN_RECONCILIATION:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_RECONCILE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;                    

                        case TRN_QUEUE:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_REPROCESS, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;

                        case TRN_VIEW:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SETUP, ""));

                            break;                    

                        case TRN_FILE_MANAGER:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;                                 

                        case TRN_MATCH:

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;                                                     

                        default:                    

                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, intIdProfile, i, FUNCTION_EXPORT, ""));

                            break;
                    }

                }
                                
                break;    
                        
            case PROFILE_MANAGER:
                
                ListCENTProfileTransaction1 = this.GetListOfProfileTransaction(PROFILE_MANAGER);
                
                for (CENTData CENTProfileTransaction1 : ListCENTProfileTransaction1)
                {
                    i = CENTProfileTransaction1.GetInt(this.GetFieldObject("id_transaction", ListCENTCatalogProfileTransaction1));
                    
                    switch (i)
                    {
                        case TRN_LAYOUT:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_IMPORT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;

                        case TRN_RECONCILIATION:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_RECONCILE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;                    

                        case TRN_QUEUE:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_REPROCESS, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;

                        case TRN_VIEW:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SETUP, ""));

                            break;                    

                        case TRN_FILE_MANAGER:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;                                 

                        case TRN_MATCH:

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;                     
                            
                        case TRN_SCHEDULE:
                            
                            ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, i, FUNCTION_SAVE, ""));                            

                        default:                    

                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_NEW, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EDIT, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_SAVE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DELETE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_FILTER, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_DUPLICATE, ""));
                            ListCENTData1.add(GetTransactionFunction(++ intId, PROFILE_MANAGER, i, FUNCTION_EXPORT, ""));

                            break;
                    }                
                }
                               
                break;
            
            case PROFILE_ANALYST:
                
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE, FUNCTION_FILTER, ""));
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE, FUNCTION_EXPORT, ""));

                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE_JOB, FUNCTION_FILTER, ""));
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE_JOB, FUNCTION_EXPORT, ""));

                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_QUEUE, FUNCTION_FILTER, ""));
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_QUEUE, FUNCTION_REPROCESS, ""));

                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_MATCH, FUNCTION_FILTER, ""));        
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_MATCH, FUNCTION_EXPORT, ""));                        
                
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE, FUNCTION_EDIT, ""));
                ListCENTData1.add(GetTransactionFunction(0, PROFILE_ANALYST, TRN_SCHEDULE, FUNCTION_SAVE, ""));
                
                break;                        
            
        }       
        
        return ListCENTData1;
    }    

    public List<CENTData> GetListOfView(List<CENTData> ListCENTCatalogTransaction1, List<CENTData> ListCENTTransaction1) throws Exception
    {
        int intId = 0;            
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        for (CENTData CENTTransaction1 : ListCENTTransaction1)
        {
            ListCENTData1.add(GetView(
                                        ++ intId, 
                                        CENTTransaction1.GetText(GetFieldObject("name", ListCENTCatalogTransaction1)), 
                                        CENTTransaction1.GetInt(GetFieldObject("id", ListCENTCatalogTransaction1)), 
                                        "", // Procedure
                                        DISPLAY_GRID, 
                                        "") // note
                                    );
        }                    
        
        return ListCENTData1;
    }    

    public List<CENTData> GetListOfViewDef(List<CENTData> ListCENTCatalogTransaction1, List<CENTData> ListCENTTransaction1, List<CENTData> ListCENTCatalog1) throws Exception
    {
        int intId = 0;          
        int intIdTransaction = 0;
        int intFieldPosition = 0;

        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        /*
         * Read all transactions and figure out the related fields
         */            
        for (CENTData CENTTransaction1 : ListCENTTransaction1)
        {
            intFieldPosition = 1;
            intIdTransaction = CENTTransaction1.GetInt(GetFieldObject("id", ListCENTCatalogTransaction1));

            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {                                
                if (CENTCatalog1.GetInt(FIELD_ID_TRN) == intIdTransaction)
                {
                    ListCENTData1.add(GetViewDef(++ intId, intIdTransaction, COMMAND_SELECT_FIELD, intIdTransaction, CENTCatalog1.GetInt(SYSTEM_FIELD_ID), 0, "", intFieldPosition, ""));
                    intFieldPosition ++;
                }    
            }
        }
        
        return ListCENTData1;
    }    
    
    public List<CENTData> GetListOfCatalog(int intIdTransaction) throws Exception
    {

        int intId = 0;
        int intPos = 0;
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTData2 = new ArrayList<CENTData>();
        
        /*
         * TRN_TABLE
         */
        intPos = 0;
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TABLE, "Table.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TABLE, "Table.Name", "name", TYPE_TEXT, "text_1", 500, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TABLE, "Table.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_MENU
         */
        intPos = 0;
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do Menu"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Parent", "id_parent", TYPE_INT, "int_2", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_MENU, "", (++intPos), "Menu pai"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Position", "position", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Posição do menu"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Type", "id_type", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_26", (++intPos), "Tipo de menu sistema/usuário"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MENU, "Menu.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações Gerais, texto livre"));

        /*
         * TRN_TRANSACTION
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do Transação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Page", "page", TYPE_TEXT, "text_2", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Página web relativa a transação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Menu", "id_menu", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_MENU, "", (++intPos), "Menu em que a transação vai aparecer"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Table", "id_table", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TABLE, "", (++intPos), "Repositório de dados (tabela)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Type", "id_type", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_26", (++intPos), "Uso interno e criadas pelos usuários (RAs)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION, "Transaction.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações Gerais, texto livre"));

        /*
         * TRN_CATALOG
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Transaction", "id_transaction", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Relacionamento com outros modulos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Label", "label", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Label na tela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Name", "name", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome dos objetos na tela, exemplo, textbox, dropdown, etc"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Type", "id_type", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_1", (++intPos), "Tipo de dado (Int, Text, Date, Double, Boolean)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Object", "object", TYPE_TEXT, "text_3", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome interno do objeto (int_1, text_2, etc)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Size", "size", TYPE_INT, "int_4", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Tamanho do campo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Nullable", "id_nullable", TYPE_BOOLEAN, "boolean_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Aceita nulo ou não"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Unique", "id_unique", TYPE_BOOLEAN, "boolean_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Identifica se o campo é unico (não repete)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Pk", "id_pk", TYPE_BOOLEAN, "boolean_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Identifica se o campo é ou não chave primária"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Fk", "id_fk", TYPE_INT, "int_5", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "alias 1", (++intPos), "Identifica chave estrangeira, transforma o campo em combo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Domain", "domain_name", TYPE_TEXT, "text_4", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Chave utilizada quando acessa a tabela de domínio"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Position", "position", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), ""));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_CATALOG, "Catalog.Note", "note", TYPE_TEXT, "text_5", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_FIELD_EVENT
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Trn", "id_transaction", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação onde o evento será aplicado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Field", "field_name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome do campo, para evento filtro, adicionar o evento por causa dos joins"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Action", "id_action", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_12", (++intPos), "Ação onde esse evento vai ocorrer (Inc/Alt/Exc)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Event", "id_event", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_13", (++intPos), "Evento que ocorre nessa ação (onBlur, onChange, etc)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Name", "id_function", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_14", (++intPos), "Nome da Função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Param1", "param_1", TYPE_TEXT, "text_2", 20, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Primeiro parametro da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Param2", "param_2", TYPE_TEXT, "text_3", 20, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Segundo parametro da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Param3", "param_3", TYPE_TEXT, "text_4", 20, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Terceiro parametro da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Param4", "param_4", TYPE_TEXT, "text_5", 20, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Quarto parametro da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Param5", "param_5", TYPE_TEXT, "text_6", 20, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Quinto parametro da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FIELD_EVENT, "FieldEvent.Note", "note", TYPE_TEXT, "text_7", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_VIEW
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Tradução do código por idioma"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Trn", "id_transaction", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Id do Transação que usará a view"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Procedure", "procedure", TYPE_TEXT, "text_2", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Stored Procedure que pode ser utilizada para sobrescrever o retorno da visão. Deve-se definir os campos normalmente ao indicar uma procedure"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Display", "id_display", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_5", (++intPos), "Define a apresentação dos dados, se em grid ou algum tipo de gráfico"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW, "View.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_VIEW_DEF
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.View", "id_view", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_VIEW, "", (++intPos), "Visão que está sendo definida"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Command", "id_command", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_3", (++intPos), "Comando sql"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Transaction", "id_transaction", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Tabela representada pela transação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Field", "id_field", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Campo da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Operator", "id_operator", TYPE_INT, "int_6", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_18", (++intPos), "Operador de filtro (igual, maior, etc)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Value", "value", TYPE_TEXT, "text_1", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Valor relacionado a condição"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Position", "position", TYPE_INT, "int_7", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Posição do campo na tela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_VIEW_DEF, "ViewDef.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_DOMAIN
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome do domínio, o que identifica como se fosse uma tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.IdDomain", "id_domain", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "id_domain é equivalente ao id (sequencial) nas outras tabelas"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.Description", "description", TYPE_TEXT, "text_2", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Valor/Descrição do dominio"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.Type", "id_typep", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_29", (++intPos), "id_domain é equivalente ao id (sequencial) nas outras tabelas"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DOMAIN, "Domain.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_COUNTRY
         */
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COUNTRY, "Country.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COUNTRY, "Country.Name", "name", TYPE_TEXT, "text_1", 500, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do país."));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COUNTRY, "Country.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_LANGUAGE
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LANGUAGE, "Language.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LANGUAGE, "Language.Name", "name", TYPE_TEXT, "text_1", 500, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do idioma (Ingles, Portugues ou esapnhol. Podemos cadastrar quantos idiomas quisermos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LANGUAGE, "Language.Language", "language_code", TYPE_TEXT, "text_2", 2, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Codigo do País, informacao técnica"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LANGUAGE, "Language.Country", "country_code", TYPE_TEXT, "text_3", 2, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Código do Idioma, informação técnica"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LANGUAGE, "Language.Note", "note", TYPE_TEXT, "text_4", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        
        
        /*
         * TRN_DICTIONARY
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DICTIONARY, "Dictionary.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DICTIONARY, "Dictionary.Language", "id_language", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LANGUAGE, "", (++intPos), "Id do idioma cadastrado no Transação de idiomas"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DICTIONARY, "Dictionary.Code", "code", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Código a ser traduzido"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DICTIONARY, "Dictionary.Description", "description", TYPE_TEXT, "text_2", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Tradução do código por idioma"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_DICTIONARY, "Dictionary.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_SCHEDULE
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do job"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Area", "id_area", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_AREA, "", (++intPos), "Departamento responsável pelo job"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Recurrence", "id_recurrence", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_22", (++intPos), "Recorrência da execução do job"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Time", "time", TYPE_TEXT, "text_2", 5, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Hora da execução"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE, "Schedule.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_SCHEDULE_JOB
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Schedule", "id_scheduler", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_SCHEDULE, "", (++intPos), "Agendamento pai do passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome do passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Type", "id_type", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_23", (++intPos), "Tipo de passo (conciliação ou layout"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Service", "id_service", TYPE_INT, "int_4", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id do Layout ou Conciliação a ser executado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_SCHEDULE_JOB, "ScheduleStep.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_QUEUE
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Date", "date", TYPE_DATE, "date_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Data da execução"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Type", "id_type", TYPE_INT, "int_2", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_27", (++intPos), "Tipo de agendamento (Importação/exportação de arquivos, execução de conciliações ou agendamentos)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Service", "id_service", TYPE_INT, "int_3", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id do processo a ser executado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Name", "name_service", TYPE_TEXT, "text_1", 100, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome do serviço em execução"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.StartDate", "start_date", TYPE_TEXT, "text_2", 15, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Hora que iniciou o processamento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.EndDate", "end_date", TYPE_TEXT, "text_3", 15, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Hora que finalizou o processamento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Description", "description", TYPE_TEXT, "text_4", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Descrição, geralmente mensagens de erro"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Status", "id_status", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_24", (++intPos), "Status do processamento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.User", "id_user", TYPE_INT, "int_5", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_USER, "", (++intPos), "Usuário que requisitou a execução"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_QUEUE, "Queue.Note", "note", TYPE_TEXT, "text_5", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        

        /*
         * TRN_COMPANY
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome da área"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Document", "document", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Email para notificar o grupo"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Email", "email", TYPE_TEXT, "text_3", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Email para notificar o grupo"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Country", "country", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_COUNTRY, "", (++intPos), "País de origem do cliente"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Language", "language", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LANGUAGE, "", (++intPos), "País de origem do cliente"));                
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.ExpireDate", "expire_date", TYPE_DATE, "date_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Data limite que a empresa pode usar o sistema"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.NumberOfReconcile", "number_reconcile", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Limite de conciliações que a empresa pode criar, zero ilimitado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.DaysToKeepHistory", "days_history", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Quantidade de dias que o sistema guarda o histórico das execuções. Zero infinito"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.NumberOfLine", "number_line", TYPE_INT, "int_5", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Limite de linhas por conciliação, zero ilimitado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Type", "id_type", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_28", (++intPos), "Status do processamento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_COMPANY, "Company.Note", "note", TYPE_TEXT, "text_4", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        

        /*
         * TRN_AREA
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_AREA, "Area.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_AREA, "Area.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome da área"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_AREA, "Area.Company", "id_company", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_COMPANY, "", (++intPos), "Nome da Empresa"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_AREA, "Area.Email", "email", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_YES, PK_YES, 0, "", (++intPos), "Email para notificar o grupo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_AREA, "Area.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_USER
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Username", "username", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do usuário"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Password", "password", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Senha do usuário"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Profile", "id_profile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_PROFILE, "", (++intPos), "Id do perfil"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Area", "id_area", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_AREA, "", (++intPos), "Id da area"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Email", "email", TYPE_TEXT, "text_3", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Email para notificar o grupo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_USER, "User.Note", "note", TYPE_TEXT, "text_4", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_PROFILE
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE, "Profile.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE, "Profile.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do perfil"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE, "Profile.Email", "email", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Email para notificar o grupo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE, "Profile.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_PROFILE_TRANSACTION
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE_TRANSACTION, "ProfileTransaction.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE_TRANSACTION, "ProfileTransaction.Profile", "id_profile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_PROFILE, "", (++intPos), "Id do perfil"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE_TRANSACTION, "ProfileTransaction.Trn", "id_transaction", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Id da transacao associada ao perfil"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_PROFILE_TRANSACTION, "ProfileTransaction.Note", "note", TYPE_TEXT, "text_1", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        

        /*
         * TRN_TRANSACTION_FUNCTION
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION_FUNCTION, "TransactionFunction.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION_FUNCTION, "TransactionFunction.Profile", "id_profile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_PROFILE, "", (++intPos), "Id do perfil"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION_FUNCTION, "TransactionFunction.Trn", "id_transaction", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Id da transação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION_FUNCTION, "TransactionFunction.Function", "id_function", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_FUNCTION, "", (++intPos), "Id da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_TRANSACTION_FUNCTION, "TransactionFunction.Note", "note", TYPE_TEXT, "text_1", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_FUNCTION
         */          
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FUNCTION, "Function.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FUNCTION, "Function.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FUNCTION, "Function.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        
        
        /*
         * TRN_LAYOUT
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome do layout"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Area", "id_area", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_AREA, "", (++intPos), "Área responsável pelo layout"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Type", "id_type", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_20", (++intPos), "Tipo de layout (Texto Simples, Texto com Sessão, Conexão Direta, XML)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Transaction", "id_transaction", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação que vai receber os dados importados"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Input", "input", TYPE_TEXT, "text_2", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Caminho e nome do arquivo ou nome da stored procedure que vai fornecedor os dados de entrada"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.LineStart", "line_start", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Linha que começa a ler as informações"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Position", "position", TYPE_INT, "int_6", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Quando mapear um layout com sessão, deve informar a posição inicial do identificador. ex: 1"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Size", "size", TYPE_INT, "int_7", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Quando mapear um layout com sessão, deve informar o tamanho do identificador da posição. ex: 2"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.FileAction", "file_action", TYPE_INT, "int_8", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_25", (++intPos), "O que fazer com arquivo após importar"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT, "Layout.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_LAYOUT_SESSION
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.Layout", "id_layout", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT, "", (++intPos), "Layout que vai receber as sessões"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome do sessão"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.SessionType", "id_type", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_7", (++intPos), "Header, Trailer ou registros"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.SessionIdentifier", "identifier", TYPE_TEXT, "text_2", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Identificador da sessão (00/01/02/03/99)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.DataFormat", "id_format", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_6", (++intPos), "Formato dos dados na fonte (delimitado, Posição Fixa ou Sequencial (procedures)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.Delimiter", "delimiter", TYPE_TEXT, "text_3", 1, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Delimitador (ponto/virgula/barra/etc)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION, "LayoutSession.Note", "note", TYPE_TEXT, "text_4", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_LAYOUT_SESSION_DEF
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Layout", "id_layout", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT, "", (++intPos), "Layout que vai receber as sessões"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Session", "id_session", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT_SESSION, "", (++intPos), "Controle para arquivos com multiplas sessoes (00, 01, 02, 99)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.StartPosition", "position", TYPE_INT, "int_4", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Posição onde se encontra a informação. Quando delimitado, serve para indicar a ordem sequencial do campo e não usa o tamaho"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Size", "size", TYPE_INT, "int_5", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Tamanho da Informação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Trn", "id_transaction", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Tipo de dado relativo ao campo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Field", "id_field", TYPE_INT, "int_7", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Junto com tipo identifica o campo que recebe a informação(int_1, text_1, text_2, etc"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Mask", "mask", TYPE_TEXT, "text_1", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Mascara que identifica os dados na origem (arquivo ou procedure). Utilizado para data (ex. yyyyMMdd, dd/MM/yyyy) ou valor (ex. 1.10,00)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Default", "default_value", TYPE_TEXT, "text_2", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Valor padrão para o campo, não mapear com a fonte de dados"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_SESSION_DEFINITION, "LayoutDef.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_LAYOUT_LAYOUT_LOOKUP
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Layout", "id_layout", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT, "", (++intPos), "Layout que receberá o de-para"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Trn", "id_transaction", TYPE_INT, "int_3", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação para selecionar campos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Field", "id_field", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Campo onde se aplica o de-para"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.LookUp", "id_lookup", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT_LOOKUP, "", (++intPos), "Tabela de de-para"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Refuse", "id_refuse", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Recursar linha caso não encontre o registro no de-para"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LAYOUT_LOOKUP, "LayoutLookUp.Note", "note", TYPE_TEXT, "text_1", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        
        
        /*
         * TRN_LAYOUT_FUNCTION
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Layout", "id_layout", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT, "", (++intPos), "Layout que receberá as definições"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Trn", "id_transaction", TYPE_INT, "int_3", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação para selecionar campos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Field", "id_field", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Campo onde se aplica a função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Function", "id_function", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_17", (++intPos), "Nome da função"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Parameter1", "parameter_1", TYPE_TEXT, "text_1", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Primeiro parametro"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Parameter2", "parameter_2", TYPE_TEXT, "text_2", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Segundo parametro"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_FUNCTION, "LayoutFunction.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        
        
        /*
         * TRN_LAYOUT_LOOKUP
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP, "LayoutLookUp.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP, "LayoutLookUp.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome da tabela de/para"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP, "LayoutLookUp.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));        

        /*
         * TRN_LAYOUT_LOOKUP_ITEM
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP_ITEM, "LayoutLookUpItem.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP_ITEM, "LayoutLookUpItem.LookUp", "id_lookup", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_LAYOUT_LOOKUP, "", (++intPos), "Layout que vai receber as sessões"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP_ITEM, "LayoutLookUpItem.Code", "code", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Código a ser traduzido"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP_ITEM, "LayoutLookUpItem.Description", "description", TYPE_TEXT, "text_2", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Descrição/Tradução"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_LAYOUT_LOOKUP_ITEM, "LayoutLookUpItem.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_RECONCILIATION
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, 0, "", (++intPos), "Nome da conciliação, usar nome intuitivo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Area", "id_area", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_AREA, "", (++intPos), "Área responsável pela conciliação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Trn", "id_transaction", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação (contexto) a ser conciliada"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Type", "id_type", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_8", (++intPos), "Tipo de conciliação (1:1 - 1:M - M:M"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.View1", "id_view_1", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_VIEW, "alias 1", (++intPos), "Visão que retorna os dados da parte a ser batida"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.View2", "id_view_2", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_VIEW, "alias 2", (++intPos), "Visão que retorna os dados da contraparte a ser batida"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Results", "id_result", TYPE_INT, "int_7", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_9", (++intPos), "Somente as diferenças ou batidos também"));        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION, "Reconcile.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_RECONCILIATION_STEP
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Reconciliation", "id_reconcile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION, "", (++intPos), "Nome da Recon"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome da passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Code1", "code_1", TYPE_TEXT, "text_2", 20, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Label que identifica o lado 1 a ser conciliado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Code2", "code_2", TYPE_TEXT, "text_3", 20, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Label que identifica o lado 2 a ser conciliado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Enabled", "id_enabled", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Habilitar ou desabilitar o passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP, "ReconcileStep.Note", "note", TYPE_TEXT, "text_4", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_RECONCILIATION_STEP_RULE
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Reconciliation", "id_reconcile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION, "", (++intPos), "Nome da Recon"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Step", "id_step", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION_STEP, "", (++intPos), "Nome do passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Name", "name", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Nome da regra"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Enabled", "id_enabled", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Habilitar ou desabilitar a regra"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE, "ReconcileStepRule.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_RECONCILIATION_STEP_RULE_FIELD
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Reconciliation", "id_reconcile", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION, "", (++intPos), "Nome da recon"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Step", "id_step", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION_STEP, "", (++intPos), "Nome do passo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Rule", "id_rule", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION_STEP_RULE, "", (++intPos), "Nome da regra"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Transaction", "id_transaction", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação utilizada para filtrar os campos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Field", "id_field", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Campo a ser utilizado como pesquisa ou comparação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Type", "id_type", TYPE_INT, "int_7", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_15", (++intPos), "Indica se é chave de pesquisa (matching) ou condição (recon)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Operator", "id_operator", TYPE_INT, "int_8", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_18", (++intPos), "Operador utilizado na comparação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Tol", "tolerance_value", TYPE_DOUBLE, "double_1", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Tolerancia a aplicar na condição dos campos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.TolType", "id_tolerance_type", TYPE_INT, "int_9", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_16", (++intPos), "Tipo de tolerancia (Absoluto/Percentual/Data)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Enabled", "id_enabled", TYPE_INT, "int_10", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_2", (++intPos), "Habilitar ou desabilitar o campo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_RECONCILIATION_STEP_RULE_FIELD, "ReconcileStepRuleField.Note", "note", TYPE_TEXT, "text_1", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_MATCH
         */
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.IdProcess", "id_process", TYPE_INT, "int_2", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id que identifica mais de uma importação por dia na mesma conciliação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Date", "date", TYPE_DATE, "date_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Data que ocorreu o batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Reconciliation", "id_reconcile", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION, "", (++intPos), "Nome da conciliação"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Step", "id_step", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION_STEP, "", (++intPos), "Passo que gerou o batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Rule", "id_rule", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_RECONCILIATION_STEP_RULE, "", (++intPos), "Regra que gerou o batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Status", "id_status", TYPE_INT, "int_6", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_10", (++intPos), "Status do batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Transaction", "id_transaction", TYPE_INT, "int_7", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_TRANSACTION, "", (++intPos), "Transação onde a conciliação é executada"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Field", "id_field", TYPE_INT, "int_8", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, TRN_CATALOG, "", (++intPos), "Campo que causou a divergencia"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Diff", "difference", TYPE_TEXT, "text_1", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Valor da divergencia"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Side", "side", TYPE_TEXT, "text_2", 0, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Identificador (código) do lado onde o registro é falho ou não existe"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH, "Match.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));

        /*
         * TRN_MATCH_ITEM
         */        
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.IdProcess", "id_process", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id do processo"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.IdMatch", "id_match", TYPE_INT, "int_3", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id do match"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.IdRecord", "id_record", TYPE_INT, "int_4", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Id do campo que originou o batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.IdRule", "id_rule", TYPE_INT, "int_5", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Status do batimento"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_MATCH_ITEM, "MatchItem.Note", "note", TYPE_TEXT, "text_3", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais, texto livre"));
        
        /*
         * TRN_FILE_MANAGER
         */            
        intPos = 0;        
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.Id",   "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, 0, "", (++intPos), "Chave primária da tabela"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.Date", "dt", TYPE_DATE, "date_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Data que o arquivo foi criado"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.Type", "id_type", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "domain_19", (++intPos), "Tipo de arquivo (uploaded, generated)"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.User", "id_user", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_USER, "", (++intPos), "Usuário que gerou os arquivos"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.File", "file", TYPE_TEXT, "text_1", 500, NULLABLE_NO, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Arquivo para donwload"));
        ListCENTData1.add(this.GetCatalog(++ intId, TRN_FILE_MANAGER, "FileManager.Note", "note", TYPE_TEXT, "text_2", 500, NULLABLE_YES, UNIQUE_NO, PK_NO, 0, "", (++intPos), "Observações gerais"));
        
        if (intIdTransaction > 0)
        {
            for (CENTData CENTCatalog1 : ListCENTData1)
            {
                if (CENTCatalog1.GetInt(2) == intIdTransaction)
                {
                    ListCENTData2.add(CENTCatalog1);
                }
            }
            
            return ListCENTData2;
        }
        else
        {        
            return ListCENTData1;
        }
    }        

    
    /*
     * Util methods used to setup reconciliations
     */        
    public void Setup(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdArea = 0;
        int intIdField = 0;
        int intIdFieldType = 0;
        int intIdView = 0;
        int intPosition = 0;
        int intSize = 0;
        int intIdLayout = 0; 
        int intIdLayoutSession = 0;
        int intIdTransaction = 0;
        int intIdCommand = 0;
        int intIdSchedule = 0;
        
        int intIdReconcile = 0;
        int intIdStep = 0;
        int intIdType = 0;
        int intIdRule = 0; 
        int intIdOperator = 0;        
        
        double dblTolerance = 0;
        int intIdToleranceType = 0;
        int intIdEnabled = 0;        
        
        String strFieldObject = "";
        String strDelimiter = "";
        String strMask = "";        
        String strDefaultValue = "";
        String strName = "";
        String strNote = "";

        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTCatalogView1 = null;
        List<CENTData> ListCENTCatalogViewDef1 = null;
        List<CENTData> ListCENTCatalog1 = null;
        
        List<CENTData> ListCENTData1 = null;
        CDALCore CDALCore1 = null;
        
        try
        {
            /*
             * Keep current profile
             */
            intIdArea = this.GetSession().GetInt(SESSION_AREA);
            
            /*
             * View to be mapped
             */            
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogView1 = GetCatalog(TRN_VIEW);
            ListCENTCatalogViewDef1 = GetCatalog(TRN_VIEW_DEF);
                
            /*
             * Mapp two layouts for current view
             */            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdView);
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            if (!ListCENTData1.isEmpty())
            {
                intIdTransaction = ListCENTData1.get(0).GetInt(GetFieldObject("id_transaction", ListCENTCatalogView1));
                ListCENTCatalog1 = this.GetCatalog(intIdTransaction);
                strName = Translate(ListCENTData1.get(0).GetText(GetFieldObject("name", ListCENTCatalogView1)));
            }

            CENTData1 = this.GetLayout(0, strName, intIdArea, LAYOUT_TYPE_TEXT_SIMPLE, intIdTransaction, this.GetPropertieValue("FILE_MANAGER") + this.GetSession().GetInt(SESSION_COMPANY) + "/" + strName.toLowerCase() + ".txt", 2, 0, 0, 1, "");
            CDALCore1.SetIdTransaction(TRN_LAYOUT);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdLayout = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create the layout session
             */
            strDelimiter = ";";
            CENTData1 = this.GetLayoutSession(0, intIdLayout, strName, LAYOUT_TYPE_TEXT_SIMPLE, "", LAYOUT_FORMAT_DELIMITED, strDelimiter, "");
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdLayoutSession = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create the session definition
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_view", ListCENTCatalogViewDef1), intIdView);
            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);
            
            for (CENTData CENTData2 : ListCENTData1)
            {
                intIdCommand = CENTData2.GetInt(GetFieldObject("id_command", ListCENTCatalogViewDef1));

                if (intIdCommand <= SELECTABLE_FIELD)
                {                    
                    intIdField = CENTData2.GetInt(GetFieldObject("id_field", ListCENTCatalogViewDef1));
                    strFieldObject = GetFieldObject(intIdField, ListCENTCatalog1);
                    intIdFieldType = GetFieldType(intIdField, ListCENTCatalog1);
                    
                    if (isMappeableField(strFieldObject))
                    {
                        strDefaultValue = "";
                        intPosition ++;
                        intSize = 0;

                        if (intIdFieldType == TYPE_DATE) 
                            strMask = "yyyyMMdd";
                        else if (intIdFieldType == TYPE_DOUBLE)
                            strMask = "0,0000";
                        else
                            strMask = "";

                        /*
                         * Map the field
                         */                        
                        CENTData1 = this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, intPosition, intSize, intIdTransaction, intIdField, strMask, strDefaultValue, "");
                        CDALCore1.SetIdView(TRN_LAYOUT_SESSION_DEFINITION);
                        CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                        CDALCore1.Persist(CENTData1, EVENT_INSERT);
                    }
                }
            }          


            /*
             * Map the data source
             */            
            strMask = "";
            strDefaultValue = "";
            intPosition ++;
            intSize = 0;
            intIdField = GetFieldId(SYSTEM_FIELD_DATA_SOURCE, this.GetCatalog(intIdTransaction));
            CENTData1 = this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, intPosition, intSize, intIdTransaction, intIdField, strMask, strDefaultValue, "");
            CDALCore1.SetIdView(TRN_LAYOUT_SESSION_DEFINITION);
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);            
            
            
            /*
             * Create default reconciliation
             */
            CENTData1 = this.GetReconcile(0, strName, intIdArea, intIdTransaction, RECONCILE_1_1, intIdView, intIdView, RESULT_ALL, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdReconcile = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create the reconcile step
             */            
            CENTData1 = this.GetReconcileStep(0, intIdReconcile, strName, strName + " 1", strName + " 2", Yes, "");
            CDALCore1.SetIdView(TRN_RECONCILIATION_STEP);
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdStep = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create the reconcile step rule
             */
            CENTData1 = this.GetReconcileStepRule(0, intIdReconcile, intIdStep, strName, Yes, "");
            CDALCore1.SetIdView(TRN_RECONCILIATION_STEP_RULE);
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdRule = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create the reconcile step rule field - add the first as key and the other as criteria
             */
            ListCENTCatalog1 = this.GetCatalog(intIdTransaction);

            for (CENTData CENTField1 : ListCENTCatalog1)
            {
                strFieldObject = CENTField1.GetText(FIELD_OBJECT);
                intIdField = CENTField1.GetInt(SYSTEM_FIELD_ID);
                intIdType = COMPARE_CRITERIA;
                intIdOperator = OPERATOR_EQUALS;
                intIdEnabled = Yes;
                    
                if (isMappeableField(strFieldObject))
                {
                    CENTData1 = this.GetReconcileStepRuleField(0, intIdReconcile, intIdStep, intIdRule, intIdTransaction, intIdField, intIdType, intIdOperator, dblTolerance, intIdToleranceType, intIdEnabled, strNote);
                    CDALCore1.SetIdView(TRN_RECONCILIATION_STEP_RULE_FIELD);
                    CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
                }
            }
                        
            /*
             * Create the schedule
             */           
            CENTData1 = this.GetSchedule(0, strName, intIdArea, 8, "00:00", strNote); // 8 means daily recurrence
            CDALCore1.SetIdTransaction(TRN_SCHEDULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdSchedule = CENTData1.GetInt(SYSTEM_FIELD_ID);
            
            /*
             * Create the schedule job
             */           
            CENTData1 = this.GetScheduleJob(0, intIdSchedule, strName, 1, intIdReconcile, strNote); // see domain_23
            CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            
            CENTData1 = this.GetScheduleJob(0, intIdSchedule, strName, 2, intIdLayout, strNote); // see domain_23
            CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);            
             
        }
        catch (CENTException CENTException1)
        {
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
            strFieldObject = null;
            strDelimiter = null;
            strMask = null;  
            strDefaultValue = null;
            strName = null;
            strNote = null;

            CENTFilter1 = null;
            ListCENTCatalogView1 = null;
            ListCENTCatalogViewDef1 = null;
            ListCENTCatalog1 = null;

            ListCENTData1 = null;
            CDALCore1 = null;
        }    
    }
    
    public void Duplicate(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */        
        int intId = 0;
        String strName = "";
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTCatalog1 = null;
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());

        try
        {
            
            switch (this.GetIdTransaction())
            {
                case TRN_TRANSACTION:
                    
                    this.CopyTransaction(CENTData1);
                    break;

                case TRN_VIEW:   
                    
                    this.CopyView(CENTData1);
                    break;
                    
                case TRN_LAYOUT:   
                    
                    this.CopyLayout(CENTData1);
                    break;
                    
                case TRN_RECONCILIATION:   
                    
                    this.CopyReconciliation(CENTData1);
                    break;

                case TRN_PROFILE:   
                    
                    this.CopyProfile(CENTData1);
                    break;

                case TRN_SCHEDULE:   
                    
                    this.CopySchedule(CENTData1);
                    break;
                    
                default:                    
                    
                    /*
                     * Copy current row
                     */
                    intId = CENTData1.GetInt(SYSTEM_FIELD_ID);
                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, intId);
                    
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
                    ListCENTData1 = CDALCore1.GetList(CENTFilter1);

                    for (CENTData CENTData2 : ListCENTData1)
                    {           
                        /*
                         * Current catalog
                         */                        
                        ListCENTCatalog1 = GetCatalog(this.GetIdTransaction());                        
                        
                        /*
                         * Clear the id to avoid issues
                         */
                        CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                        
                        switch (this.GetIdTransaction())
                        {
                            case TRN_TABLE:
                            case TRN_MENU:
                            case TRN_TRANSACTION:
                            case TRN_CATALOG:
                            case TRN_VIEW:
                            case TRN_DOMAIN:    
                            case TRN_COUNTRY:
                            case TRN_LANGUAGE:
                            case TRN_SCHEDULE:
                            case TRN_SCHEDULE_JOB:
                            case TRN_COMPANY:
                            case TRN_AREA:
                            case TRN_PROFILE:
                            case TRN_FUNCTION:
                                
                                strName = CENTData2.GetText(GetFieldObject("name", ListCENTCatalog1));
                                CENTData2.SetText(GetFieldObject("name", ListCENTCatalog1), strName + " (1)");                                  
                                break;
                                
                            case TRN_DICTIONARY:
                                strName = CENTData2.GetText(GetFieldObject("code", ListCENTCatalog1));
                                CENTData2.SetText(GetFieldObject("code", ListCENTCatalog1), strName + " (1)");                                
                                break;
                                
                            case TRN_USER:                                
                                strName = CENTData2.GetText(GetFieldObject("username", ListCENTCatalog1));
                                CENTData2.SetText(GetFieldObject("username", ListCENTCatalog1), strName + " (1)");                                
                                break;                                
                        }

                        /*
                         * Save it
                         */
                        CDALCore1.SetIdTransaction(this.GetIdTransaction());
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                        CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                        intId = CENTData2.GetInt(SYSTEM_FIELD_ID);               
                    }    
            }
                        
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            CENTFilter1 = null;
            ListCENTData1 = null;
            CDALCore1 = null;
        }    
    }     
    
    public int CopyView(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdView1 = 0;
        int intIdView2 = 0;

        String strName = "";
        
        CDALCore CDALCore1 = null;
        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTCatalogView1 = null;
        List<CENTData> ListCENTCatalogViewDef1 = null;        
        List<CENTData> ListCENTData2 = null;

        try
        {
            /*
             * View to be mapped
             */            
            intIdView1 = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogView1 = GetCatalog(TRN_VIEW);
            ListCENTCatalogViewDef1 = GetCatalog(TRN_VIEW_DEF);
                
            /*
             * Duplicate the view and keep the id
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdView1);
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogView1))) + " (1)";                
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogView1), strName);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_VIEW);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdView2 = CENTData2.GetInt(SYSTEM_FIELD_ID);                
            }
            
            
            /*
             * Copy the view definition
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_view", ListCENTCatalogViewDef1), intIdView1);
            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_view", ListCENTCatalogViewDef1), intIdView2);
            
                /*
                 * Persist the view definition
                 */
                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Return new view id
             */            
            return intIdView2;
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;
            CDALCore1 = null;
            CENTFilter1 = null;
            ListCENTCatalogView1 = null;
            ListCENTCatalogViewDef1 = null;        
            ListCENTData2 = null;
        }    
    }
    
    public void CopyTransaction(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdView2 = 0;
        int intIdTransaction1 = 0;
        int intIdTransaction2 = 0;

        String strName = "";
        
        CDALCore CDALCore1 = null;
        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTCatalogView1 = null;
        List<CENTData> ListCENTCatalogViewDef1 = null;
        List<CENTData> ListCENTCatalogTransaction1 = null;
        List<CENTData> ListCENTCatalogProfileTransaction1 = null;
        List<CENTData> ListCENTCatalogTransactionFunction1 = null;
        List<CENTData> ListCENTCatalogCatalog1 = null;        
        List<CENTData> ListCENTData2 = null;

        try
        {
            /*
             * View to be mapped
             */            
            intIdTransaction1 = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogTransaction1 = GetCatalog(TRN_TRANSACTION);
            ListCENTCatalogCatalog1 = GetCatalog(TRN_CATALOG);
            ListCENTCatalogView1 = GetCatalog(TRN_VIEW); 
            ListCENTCatalogViewDef1 = GetCatalog(TRN_VIEW_DEF); 
            ListCENTCatalogProfileTransaction1 = GetCatalog(TRN_PROFILE_TRANSACTION); 
            ListCENTCatalogTransactionFunction1 = GetCatalog(TRN_TRANSACTION_FUNCTION); 
                
            /*
             * Duplicate the transaction
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogTransaction1))) + " (1)";                
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogTransaction1), strName);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdTransaction2 = CENTData2.GetInt(SYSTEM_FIELD_ID);                
            }
            
            
            /*
             * Copy the catalog definition
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalogCatalog1), intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_transaction", ListCENTCatalogCatalog1), intIdTransaction2);
            
                /*
                 * Persist the view definition
                 */
                CDALCore1.SetIdTransaction(TRN_CATALOG);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }                        

            /*
             * Copy associated view
             */            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogView1))) + " (1)";                
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogView1), strName);
                CENTData2.SetInt(GetFieldObject("id_transaction", ListCENTCatalogView1), intIdTransaction2);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_VIEW);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdView2 = CENTData2.GetInt(SYSTEM_FIELD_ID);                
            }
            
            
            /*
             * Copy the view definition
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_view", ListCENTCatalogViewDef1), intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_view", ListCENTCatalogViewDef1), intIdView2);
            
                /*
                 * Persist the view definition
                 */                                
                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Copy the profile x transaction
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalogProfileTransaction1), intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_transaction", ListCENTCatalogProfileTransaction1), intIdTransaction2);
            
                /*
                 * Persist the view definition
                 */                                
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Copy the transaction x function
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalogTransactionFunction1), intIdTransaction1);
            CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_transaction", ListCENTCatalogTransactionFunction1), intIdTransaction2);
            
                /*
                 * Persist the view definition
                 */                                
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }            
            
            
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;

            CDALCore1 = null;

            CENTFilter1 = null;
            CENTData1 = null;

            ListCENTCatalogView1 = null;
            ListCENTCatalogViewDef1 = null;
            ListCENTCatalogTransaction1 = null;
            ListCENTCatalogProfileTransaction1 = null;
            ListCENTCatalogTransactionFunction1 = null;
            ListCENTCatalogCatalog1 = null;        
            ListCENTData2 = null;
        }    
    }        

    public void CopyLayout(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdLayout1 = 0;
        int intIdLayout2 = 0;
        int intIdSession1 = 0;
        int intIdSession2 = 0;

        String strName = "";
        
        CDALCore CDALCore1 = null;
        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTCatalogLayout1 = null;
        List<CENTData> ListCENTCatalogLayoutSession1 = null;        
        List<CENTData> ListCENTCatalogLayoutSessionDef1 = null;
        
        List<CENTData> ListCENTData2 = null;
        List<CENTData> ListCENTData3 = null;
        List<CENTData> ListCENTData4 = null;

        try
        {
            /*
             * Layout to be mapped
             */            
            intIdLayout1 = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogLayout1 = GetCatalog(TRN_LAYOUT);
            ListCENTCatalogLayoutSession1 = GetCatalog(TRN_LAYOUT_SESSION);
            ListCENTCatalogLayoutSessionDef1 = GetCatalog(TRN_LAYOUT_SESSION_DEFINITION);
                
            /*
             *****************************************************************            
             *********************** COPY LAYOUTS ****************************
             *****************************************************************
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdLayout1);
            CDALCore1.SetIdTransaction(TRN_LAYOUT);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */          
                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogLayout1))) + " (1)";
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogLayout1), strName);
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_LAYOUT);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdLayout2 = CENTData2.GetInt(SYSTEM_FIELD_ID);
                                                
                /*
                 *****************************************************************
                 *********************** COPY SESSION ****************************
                 *****************************************************************                
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogLayoutSession1), intIdLayout1);
                CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                ListCENTData3 = CDALCore1.GetList(CENTFilter1);

                for (CENTData CENTData3 : ListCENTData3)
                {
                    /*
                     * Current session
                     */                      
                    intIdSession1 = CENTData3.GetInt(SYSTEM_FIELD_ID);
                    strName = CENTData3.GetText(GetFieldObject("name", ListCENTCatalogLayoutSession1));
                    
                    CENTData3.SetInt(GetFieldObject("id_layout", ListCENTCatalogLayoutSession1), intIdLayout2);
                    CENTData3.SetText(GetFieldObject("name", ListCENTCatalogLayoutSession1), strName);
                    CENTData3.SetInt(SYSTEM_FIELD_ID, 0);
                    
                    /*
                     * Persist the view definition
                     */                                
                    CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                    CDALCore1.Persist(CENTData3, EVENT_INSERT);
                    intIdSession2 = CENTData3.GetInt(SYSTEM_FIELD_ID);

                    /*
                     *****************************************************************            
                     *********************** COPY SESSION DEF ************************
                     *****************************************************************
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogLayoutSessionDef1), intIdLayout1);
                    CENTFilter1.SetInt(GetFieldObject("id_session", ListCENTCatalogLayoutSessionDef1), intIdSession1);
                    CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
                    ListCENTData4 = CDALCore1.GetList(CENTFilter1);

                    for (CENTData CENTData4 : ListCENTData4)
                    {
                        CENTData4.SetInt(GetFieldObject("id_layout", ListCENTCatalogLayoutSessionDef1), intIdLayout2);
                        CENTData4.SetInt(GetFieldObject("id_session", ListCENTCatalogLayoutSessionDef1), intIdSession2);
                        CENTData4.SetInt(SYSTEM_FIELD_ID, 0);
                        
                        /*
                         * Persist the view definition
                         */                                
                        CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                        CDALCore1.Persist(CENTData4, EVENT_INSERT);
                    }                    
                }                
            }

        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;

            CDALCore1 = null;

            CENTFilter1 = null;

            ListCENTCatalogLayout1 = null;
            ListCENTCatalogLayoutSession1 = null;     
            ListCENTCatalogLayoutSessionDef1 = null;
            ListCENTData2 = null;
            ListCENTData3 = null;
            ListCENTData4 = null;
        }    
    }

    public void CopyReconciliation(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdReconcile1 = 0;
        int intIdReconcile2 = 0;
        int intIdStep1 = 0;
        int intIdStep2 = 0;
        int intIdRule1 = 0;
        int intIdRule2 = 0;        

        String strName = "";
        
        CDALCore CDALCore1 = null;
        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTCatalogReconcile1 = null;
        List<CENTData> ListCENTCatalogReconcileStep1 = null;        
        List<CENTData> ListCENTCatalogReconcileStepRule1 = null;
        List<CENTData> ListCENTCatalogReconcileStepRuleField1 = null;
        
        List<CENTData> ListCENTData2 = null;
        List<CENTData> ListCENTData3 = null;
        List<CENTData> ListCENTData4 = null;
        List<CENTData> ListCENTData5 = null;

        try
        {
            /*
             * Reconcile to be mapped
             */            
            intIdReconcile1 = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogReconcile1 = GetCatalog(TRN_RECONCILIATION);
            ListCENTCatalogReconcileStep1 = GetCatalog(TRN_RECONCILIATION_STEP);
            ListCENTCatalogReconcileStepRule1 = GetCatalog(TRN_RECONCILIATION_STEP_RULE);
            ListCENTCatalogReconcileStepRuleField1 = GetCatalog(TRN_RECONCILIATION_STEP_RULE_FIELD);
                
            /*
             *****************************************************************            
             *********************** COPY Reconciliation *********************
             *****************************************************************
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdReconcile1);
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogReconcile1))) + " (1)";
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogReconcile1), strName);
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdReconcile2 = CENTData2.GetInt(SYSTEM_FIELD_ID);
                                                
                /*
                 *****************************************************************
                 *********************** COPY STEPS ******************************
                 *****************************************************************                
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStep1), intIdReconcile1);
                CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
                ListCENTData3 = CDALCore1.GetList(CENTFilter1);

                for (CENTData CENTData3 : ListCENTData3)
                {
                    intIdStep1 = CENTData3.GetInt(SYSTEM_FIELD_ID);       
                    strName = Translate(CENTData3.GetText(GetFieldObject("name", ListCENTCatalogReconcileStep1)));
                    
                    CENTData3.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStep1), intIdReconcile2);
                    CENTData3.SetText(GetFieldObject("name", ListCENTCatalogReconcileStep1), strName);
                    CENTData3.SetInt(SYSTEM_FIELD_ID, 0);

                    /*
                     * Persist the view definition
                     */                                
                    CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                    CDALCore1.Persist(CENTData3, EVENT_INSERT);
                    intIdStep2 = CENTData2.GetInt(SYSTEM_FIELD_ID);

                    /*
                     *****************************************************************            
                     *********************** COPY RULES ************************
                     *****************************************************************
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStepRule1), intIdReconcile1);
                    CENTFilter1.SetInt(GetFieldObject("id_step", ListCENTCatalogReconcileStepRule1), intIdStep1);
                    CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
                    ListCENTData4 = CDALCore1.GetList(CENTFilter1);

                    for (CENTData CENTData4 : ListCENTData4)
                    {
                        intIdRule1 = CENTData4.GetInt(SYSTEM_FIELD_ID); 
                        strName = Translate(CENTData3.GetText(GetFieldObject("name", ListCENTCatalogReconcileStepRule1)));
                        
                        CENTData4.SetText(GetFieldObject("name", ListCENTCatalogReconcileStepRule1), strName);
                        CENTData4.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStepRule1), intIdReconcile2);
                        CENTData4.SetInt(GetFieldObject("id_step", ListCENTCatalogReconcileStepRule1), intIdStep2);
                        CENTData4.SetInt(SYSTEM_FIELD_ID, 0);

                        /*
                         * Persist the view definition
                         */                                
                        CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                        CDALCore1.Persist(CENTData4, EVENT_INSERT);
                        intIdRule2 = CENTData4.GetInt(SYSTEM_FIELD_ID);
                        
                        /*
                         *****************************************************************            
                         *********************** COPY FIELDS *****************************
                         *****************************************************************
                         */
                        CENTFilter1 = new CENTData();
                        CENTFilter1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStepRuleField1), intIdReconcile1);
                        CENTFilter1.SetInt(GetFieldObject("id_step", ListCENTCatalogReconcileStepRuleField1), intIdStep1);
                        CENTFilter1.SetInt(GetFieldObject("id_rule", ListCENTCatalogReconcileStepRuleField1), intIdRule1);
                        CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
                        ListCENTData5 = CDALCore1.GetList(CENTFilter1);

                        for (CENTData CENTData5 : ListCENTData5)
                        {
                            CENTData5.SetInt(GetFieldObject("id_reconcile", ListCENTCatalogReconcileStepRuleField1), intIdReconcile2);
                            CENTData5.SetInt(GetFieldObject("id_step", ListCENTCatalogReconcileStepRuleField1), intIdStep2);
                            CENTData5.SetInt(GetFieldObject("id_rule", ListCENTCatalogReconcileStepRuleField1), intIdRule2);
                            CENTData5.SetInt(SYSTEM_FIELD_ID, 0);

                            /*
                             * Persist the view definition
                             */                                
                            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
                            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                            CDALCore1.Persist(CENTData5, EVENT_INSERT);
                        }
                    }
                }                
            }

        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;
            CDALCore1 = null;
            CENTFilter1 = null;

            ListCENTCatalogReconcile1 = null;
            ListCENTCatalogReconcileStep1 = null;     
            ListCENTCatalogReconcileStepRule1 = null;
            ListCENTCatalogReconcileStepRuleField1 = null;
            
            ListCENTData2 = null;
            ListCENTData3 = null;
            ListCENTData4 = null;
            ListCENTData5 = null;
        }    
    }
    
    public void CopyProfile(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdProfile1 = 0;
        int intIdProfile2 = 0;

        String strName = "";

        CDALCore CDALCore1 = null;        
        CENTData CENTFilter1 = null;

        List<CENTData> ListCENTData2 = null;        
        List<CENTData> ListCENTCatalogProfile1 = null;
        List<CENTData> ListCENTCatalogProfileTransaction1 = null;        
        List<CENTData> ListCENTCatalogTransactionFunction1 = null;

        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
         
            /*
             * Layout to be mapped
             */
            intIdProfile1 = CENTData1.GetInt(SYSTEM_FIELD_ID);
            
            /*
             * Get catalogs
             */                        
            ListCENTCatalogProfile1 = GetCatalog(TRN_PROFILE);
            ListCENTCatalogProfileTransaction1 = GetCatalog(TRN_PROFILE_TRANSACTION);
            ListCENTCatalogTransactionFunction1 = GetCatalog(TRN_TRANSACTION_FUNCTION);
                
            /*
             * Copy the profile
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdProfile1);
            CDALCore1.SetIdTransaction(TRN_PROFILE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogProfile1))) + " (1)";
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogProfile1), strName);
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_PROFILE);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdProfile2 = CENTData2.GetInt(SYSTEM_FIELD_ID);
            }
            
            /*
             * Copy profile x transaction
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_profile", ListCENTCatalogProfileTransaction1), intIdProfile1);
            CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */
                CENTData2.SetInt(GetFieldObject("id_profile", ListCENTCatalogProfileTransaction1), intIdProfile2);
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Copy transaction x function
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_profile", ListCENTCatalogTransactionFunction1), intIdProfile1);
            CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */
                CENTData2.SetInt(GetFieldObject("id_profile", ListCENTCatalogTransactionFunction1), intIdProfile2);
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }            
            
            
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;

            CDALCore1 = null;        
            CENTFilter1 = null;

            ListCENTData2 = null;        
            ListCENTCatalogProfile1 = null;
            ListCENTCatalogProfileTransaction1 = null;        
            ListCENTCatalogTransactionFunction1 = null;
        }    
    }    

    public int CopySchedule(CENTData CENTData1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */
        int intIdSchedule1 = 0;
        int intIdSchedule2 = 0;

        String strName = "";
        
        CDALCore CDALCore1 = null;        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTCatalogSchedule1 = null;
        List<CENTData> ListCENTCatalogScheduleJob1 = null;        
        List<CENTData> ListCENTData2 = null;

        try
        {
            /*
             * View to be mapped
             */            
            intIdSchedule1 = CENTData1.GetInt(SYSTEM_FIELD_ID);            

            /*
             * Get catalogs
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            ListCENTCatalogSchedule1 = GetCatalog(TRN_SCHEDULE);
            ListCENTCatalogScheduleJob1 = GetCatalog(TRN_SCHEDULE_JOB);
                
            /*
             * Duplicate the schedule and keep the id
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdSchedule1);
            CDALCore1.SetIdTransaction(TRN_SCHEDULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                /*
                 * Rename it
                 */                
                strName = Translate(CENTData2.GetText(GetFieldObject("name", ListCENTCatalogSchedule1))) + " (1)";
                CENTData2.SetInt(SYSTEM_FIELD_ID, 0);
                CENTData2.SetText(GetFieldObject("name", ListCENTCatalogSchedule1), strName);
                
                /*
                 * Save it
                 */
                CDALCore1.SetIdTransaction(TRN_SCHEDULE);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);              
                intIdSchedule2 = CENTData2.GetInt(SYSTEM_FIELD_ID);                
            }
            
            
            /*
             * Copy the jobs associated to the schedule
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(GetFieldObject("id_scheduler", ListCENTCatalogScheduleJob1), intIdSchedule1);
            CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    
            ListCENTData2 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                CENTData2.SetInt(GetFieldObject("id_scheduler", ListCENTCatalogScheduleJob1), intIdSchedule2);
            
                /*
                 * Persist the view definition
                 */
                CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Return new view id
             */            
            return intIdSchedule2;
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strName = null;
            CDALCore1 = null;
            CENTFilter1 = null;
            ListCENTCatalogSchedule1 = null;
            ListCENTCatalogScheduleJob1 = null;        
            ListCENTData2 = null;
        }    
    }    
   
    public List<CENTData> GetMenu(int intId, int intIdParent) throws SQLException, CENTException, Exception
    {
        /*
         * Create data layer instance
         */        
        int intIdMenu = 0;
        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTData2 = new ArrayList<CENTData>();
        CDALSetup CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());
            
        try
        {
            /*
             * Get all menus and related transactions that users have access
             */
            ListCENTData1 = CDALSetup1.GetMenu(intId, intIdParent);

            /*
             * Remove menus with no transaction (previous query returns empty menus)

            for (CENTData CENTData2 : ListCENTData1)
            {
                intIdMenu = CENTData2.GetInt(SYSTEM_FIELD_ID);
  
                CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());
                
                if (!CDALSetup1.GetTransaction(intIdMenu).isEmpty())
                {
                    ListCENTData2.add(CENTData2);
                }
                
            }
             */                         
            
            return ListCENTData1;
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }        
        finally
        {
            ListCENTData1 = null;        
            ListCENTData2 = null;
            CDALSetup1 = null;
        }

    }
    
    public List<CENTData> GetTransaction(int intIdMenu) throws SQLException, CENTException 
    {
        CDALSetup CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());
        return CDALSetup1.GetTransaction(intIdMenu);        
    }

    
    public void CreateDemoAccessControl(int intIdArea, int intIdEvent)  throws Exception
    {
        
        String strLabel = ""; 
        String strLanguage = "";
        CENTData CENTData1 = null;
        List<CENTData> ListCENTData1 = null;        

        try
        {
            /*
             * Create basic profiles
             */           
            CDALCore CDALCore1 = new CDALCore(this.GetConnection(), CENTSession1);                    
            
            /*
             * Create basic profiles
             */
            ListCENTData1 = this.GetListOfProfiles();                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                        

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_ADMIN);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_MANAGER);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_ANALYST);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                    

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_ADMIN);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_MANAGER);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                    

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_ANALYST);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }
            
            ListCENTData1 = this.GetListOfUsers(intIdArea);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_USER);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }            

        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }

        
    }
    
    
    /*
     * Setup area to show reconciliation in action (area, profiles, users, permissions, entries, etc)
     */    
    public void CreateDemoReconciliationSolution(int intIdArea, int intIdEvent) throws Exception
    {
        /*
         * General Declaration
         */
        int intPos = 0;
        int intIdView = 0;
        int intIdView1 = 0;
        int intIdView2 = 0;
        int intIdLayout = 0;
        int intIdLayoutSession = 0;
        int intIdReconciliation = 0;
        int intIdStep = 0;
        int intIdRule = 0;
        int intIdLookUp = 0;
        int intIdSchedule = 0;        
        int intIdTransaction = 0;
        int intIdCompany = 0;
        int intIdLanguage = 0;
        
        String strLabel = ""; 
        String strLanguage = "";
        CENTData CENTData1 = null;
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), CENTSession1);
        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTCatalogTransaction1 = null;

        try
        {
            /*
             * Keep current company
             */
            intIdCompany = this.GetSession().GetInt(SESSION_COMPANY);

            /*
             * Get current language and create according
             */                       
            strLanguage = this.GetSession().GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals("pt"))
                intIdLanguage = 1; // Portuguese is default language
            else if (strLanguage.equals("en"))
                intIdLanguage = 2; // English
            else if (strLanguage.equals("sp"))
                intIdLanguage = 3; // Spanish
            
            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Cambio";
                    break;
                case 2:
                    strLabel = "FX";
                    break;
                case 3:
                    strLabel = "Cambio";
                    break;                    
            }
            
            /*
             * Create example's transaction
             */                                    
            CENTData1 = this.GetTransaction(0, strLabel, "main", MENU_RA, TABLE_USER_1, TRANSACTION_TYPE_USER, "");                        
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Create basic profiles

            ListCENTData1 = this.GetListOfProfiles();                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                        

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_ADMIN);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_MANAGER);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfProfileTransaction(PROFILE_ANALYST);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                    

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_ADMIN);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_MANAGER);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                    

            ListCENTData1 = this.GetListOfTransactionFunction(PROFILE_ANALYST);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                    

            ListCENTData1 = this.GetListOfUsers(intIdArea);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.SetIdTransaction(TRN_USER);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                CDALCore1.Persist(CENTData2, intIdEvent);
            }                                                                        
             */
            
            
            /*
             * Profile x Transaction
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_OWNER, intIdTransaction, ""));
            ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_ADMIN, intIdTransaction, ""));
            ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_MANAGER, intIdTransaction, ""));
            ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_ANALYST, intIdTransaction, ""));
            CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }                                                    

            /*
             * Transaction x Function (bosses)
             */
            ListCENTData1 = new ArrayList<CENTData>();                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_NEW, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_EDIT, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_SAVE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_DELETE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_DUPLICATE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_FILTER, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_EXPORT, ""));                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_NEW, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_EDIT, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_SAVE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_DELETE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_DUPLICATE, ""));                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_FILTER, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_EXPORT, ""));                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_NEW, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_EDIT, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_SAVE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_DELETE, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_DUPLICATE, ""));                                                                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_FILTER, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_MANAGER, intIdTransaction, FUNCTION_EXPORT, ""));                        
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ANALYST, intIdTransaction, FUNCTION_FILTER, ""));
            ListCENTData1.add(this.GetTransactionFunction(0, PROFILE_ANALYST, intIdTransaction, FUNCTION_EXPORT, ""));                                               
            CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }                                                    

            /*
             * Catalog fields
             */
            ListCENTData1 = new ArrayList<CENTData>();     
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, No, No, Yes, 0, "", 1, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.MatchId", "id_match", TYPE_INT, SYSTEM_FIELD_MATCH_ID, 0, Yes, No, No, 0, "", 2, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.ProcessId", "id_process", TYPE_INT, SYSTEM_FIELD_ID_PROCESS, 0, Yes, No, No, 0, "", 3, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.MatchDate", "date_match", TYPE_DATE, SYSTEM_FIELD_MATCH_DATE, 0, Yes, No, No, 0, "", 4, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Reconciliation", "reconcile", TYPE_TEXT, SYSTEM_FIELD_MATCH_RECONCILE, 50, Yes, No, No, 0, "", 5, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Step", "step", TYPE_TEXT, SYSTEM_FIELD_MATCH_STEP, 50, Yes, No, No, 0, "", 6, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Rule", "rule", TYPE_TEXT, SYSTEM_FIELD_MATCH_RULE, 50, Yes, No, No, 0, "", 7, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Status", "status", TYPE_INT, SYSTEM_FIELD_MATCH_ID_STATUS, 0, Yes, No, No, TRN_DOMAIN, "domain_10", 8, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Field", "field", TYPE_TEXT, SYSTEM_FIELD_MATCH_FIELD, 50, Yes, No, No, 0, "", 9, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Difference", "difference", TYPE_TEXT, SYSTEM_FIELD_MATCH_DIFF, 50, Yes, No, No, 0, "", 10, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Side", "side", TYPE_TEXT, SYSTEM_FIELD_MATCH_SIDE, 50, Yes, No, No, 0, "", 11, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.DataSource", "data_source", TYPE_TEXT, SYSTEM_FIELD_DATA_SOURCE, 50, Yes, No, No, 0, "", 12, ""));                        

            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Ticket", "id_ticket", TYPE_INT, "int_1", 0, Yes, No, No, 0, "", 1, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Date", "date", TYPE_DATE, "date_1", 0, Yes, No, No, 0, "", 2, ""));                        
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Type", "type", TYPE_TEXT, "text_1", 50, Yes, No, No, 0, "", 3, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Currency", "currency", TYPE_TEXT, "text_2", 10, Yes, No, No, 0, "", 4, ""));            
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Amount", "amount", TYPE_DOUBLE, "double_1", 50, Yes, No, No, 0, "", 5, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Price", "price", TYPE_DOUBLE, "double_2", 50, Yes, No, No, 0, "", 6, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Settlement", "dt_settle", TYPE_DATE, "date_2", 0, Yes, No, No, 0, "", 7, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Fixing", "dt_fixing", TYPE_DATE, "date_3", 10, Yes, No, No, 0, "", 8, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "RA.Counterparty", "ctpy", TYPE_TEXT, "text_3", 10, Yes, No, No, 0, "", 9, ""));


            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

           /*
            * Keep the catalog to manage fields later
            */                        
            ListCENTCatalogTransaction1 = this.GetCatalog(intIdTransaction); 
            
            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Cambio";
                    break;
                case 2:
                    strLabel = "FX";                    
                    break;                    
                case 3:
                    strLabel = "Cambio";                    
                    break;                    
            }

            /*
             * Create main view (must be like transaction)
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);
            
            ListCENTData1 = new ArrayList<CENTData>();                            
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Id", ListCENTCatalogTransaction1), 0, "", 1, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), 0, "", 2, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), 0, "", 3, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), 0, "", 4, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), 0, "", 5, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), 0, "", 6, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), 0, "", 7, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), 0, "", 8, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), 0, "", 9, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), 0, "", 10, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.DataSource", ListCENTCatalogTransaction1), 0, "", 11, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.IdProcess", ListCENTCatalogTransaction1), 0, "", 12, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Status", ListCENTCatalogTransaction1), 0, "", 13, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Difference", ListCENTCatalogTransaction1), 0, "", 14, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_ORDER_BY_DESC, intIdTransaction, this.GetFieldId("RA.Status", ListCENTCatalogTransaction1), 0, "", 15, ""));

            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }                            

            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Operações de Cambio";
                    break;
                case 2:
                    strLabel = "Trade List";
                    break;                    
                case 3:
                    strLabel = "Cambio Operaciones";                    
                    break;                    
            }
                        
            /*
             * Create the view for transaction
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);
            intIdView1 = intIdView;
            
            ListCENTData1 = new ArrayList<CENTData>();                        
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), 0, "", 1, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), 0, "", 2, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), 0, "", 3, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), 0, "", 4, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), 0, "", 5, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), 0, "", 6, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), 0, "", 7, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), 0, "", 8, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), 0, "", 9, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.DataSource", ListCENTCatalogTransaction1), 0, "", 10, ""));                        
            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }                        
            
            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Somatória (quantidade) por moeda";
                    break;
                case 2:
                    strLabel = "Sum (amount) by currency";
                    break;                    
                case 3:
                    strLabel = "Suma (cantidad) por moneda";                    
                    break;                    
            }            
            
            /*
             * Create the view for balances
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);
            intIdView2 = intIdView;

            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), 0, "", 1, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), 0, "", 2, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_SUM, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), 0, "", 3, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_AVG, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), 0, "", 4, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("RA.DataSource", ListCENTCatalogTransaction1), 0, "", 5, ""));
            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                                
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            
            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Importar operações";
                    break;
                case 2:
                    strLabel = "Import transaction";
                    break;                    
                case 3:
                    strLabel = "Importar transacción";
                    break;                    
            }
            
            /*
             * Create the layouts/Session/Definition
             */
            CENTData1 = this.GetLayout(0, strLabel, AREA_FX, LAYOUT_TYPE_TEXT_FILE, intIdTransaction, this.GetPathFileToImport("fx.txt"), 2, 0, 0, 1, "");
            CDALCore1.SetIdTransaction(TRN_LAYOUT);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdLayout = CENTData1.GetInt(SYSTEM_FIELD_ID);

            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Operações de cambio";
                    break;
                case 2:
                    strLabel = "Transaction FX";
                    break;                    
                case 3:
                    strLabel = "Transaction de cambio";
                    break;                    
            }            
            
            CENTData1 = this.GetLayoutSession(0, intIdLayout, strLabel, LAYOUT_TYPE_TEXT_SIMPLE, "", LAYOUT_FORMAT_DELIMITED, ";", "");
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdLayoutSession = CENTData1.GetInt(SYSTEM_FIELD_ID);                            
            
            intPos = 0;
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), "", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), "yyyyMMdd", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), "", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), "", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), "0.00", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), "0.0000", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), "yyyyMMdd", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), "yyyyMMdd", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), "", "", ""));
            ListCENTData1.add(this.GetLayoutSessionDefinition(0, intIdLayout, intIdLayoutSession, ++intPos, 0, intIdTransaction, this.GetFieldId("RA.DataSource", ListCENTCatalogTransaction1), "", "", ""));
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Define the label according to language
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Contrapartes";
                    break;
                case 2:
                    strLabel = "Counterparty";
                    break;                    
                case 3:
                    strLabel = "Contrapartes";
                    break;                    
            }     
            
            /*
             * Create lookup for currency
             */
            CENTData1 = this.GetLookUp(0, strLabel,  "");
            CDALCore1.SetIdTransaction(TRN_LAYOUT_LOOKUP);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdLookUp = CENTData1.GetInt(SYSTEM_FIELD_ID);

            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetLookUpItem(0, intIdLookUp, "00001", "Citibank", ""));
            ListCENTData1.add(this.GetLookUpItem(0, intIdLookUp, "00002", "Bank of America", ""));
            ListCENTData1.add(this.GetLookUpItem(0, intIdLookUp, "00003", "JP Morgan Chase", ""));
            CDALCore1.SetIdTransaction(TRN_LAYOUT_LOOKUP_ITEM);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            ListCENTData1 = new ArrayList<CENTData>();                        
            ListCENTData1.add(this.GetLayoutLookUp(0, intIdLayout, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), intIdLookUp, Yes, ""));
            CDALCore1.SetIdTransaction(TRN_LAYOUT_LAYOUT_LOOKUP);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);          
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create functions
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetLayoutFunction(0, intIdLayout, intIdTransaction, this.GetFieldId("currency", ListCENTCatalogTransaction1), 1, ".", "/", ""));    // Replace(".", "/")
            CDALCore1.SetIdTransaction(TRN_LAYOUT_FUNCTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Create reconciliation (Spot)
             */
            
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "A Vista";
                    break;
                case 2:
                    strLabel = "Spot";
                    break;                    
                case 3:
                    strLabel = "A Vista";
                    break;                    
            }                        
            
            CENTData1 = this.GetReconcile(0, strLabel, intIdArea, intIdTransaction, RECONCILE_1_1, intIdView1, intIdView1, RESULT_ALL, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdReconciliation = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "A Vista (Passo 1)";
                    break;
                case 2:
                    strLabel = "Spot (Step 1)";
                    break;                    
                case 3:
                    strLabel = "A Vista (Passo 1)";
                    break;                    
            }
            
            CENTData1 = this.GetReconcileStep(0, intIdReconciliation, strLabel, "Spot (Ticket)", "Spot (Settle)", Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdStep = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "A Vista (Regra 1)";
                    break;
                case 2:
                    strLabel = "Spot (Rule 1)";
                    break;                    
                case 3:
                    strLabel = "A Vista (Regla 1)";
                    break;                    
            }            
            
            CENTData1 = this.GetReconcileStepRule(0, intIdReconciliation, intIdStep, strLabel, Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdRule = CENTData1.GetInt(SYSTEM_FIELD_ID);                        

            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));                                                
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create reconciliation (Forward)
             */                   
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Futuros";
                    break;
                case 2:
                    strLabel = "Forward";
                    break;                    
                case 3:
                    strLabel = "Futuros";
                    break;                    
            }            
            
            CENTData1 = this.GetReconcile(0, strLabel, intIdArea, intIdTransaction, RECONCILE_1_1, intIdView1, intIdView1, RESULT_ALL, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdReconciliation = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Futuros (Passo 1)";
                    break;
                case 2:
                    strLabel = "Forward (Step 1)";
                    break;                    
                case 3:
                    strLabel = "Futuros (Paso 1)";
                    break;                    
            }
            
            CENTData1 = this.GetReconcileStep(0, intIdReconciliation, strLabel, "Fwd (Ticket)", "Fwd (Settle)", Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdStep = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Futuros (Regra 1)";
                    break;
                case 2:
                    strLabel = "Forward (Rule 1)";
                    break;                    
                case 3:
                    strLabel = "Futuros (Regla 1)";
                    break;                    
            }            
            
            CENTData1 = this.GetReconcileStepRule(0, intIdReconciliation, intIdStep, strLabel, Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdRule = CENTData1.GetInt(SYSTEM_FIELD_ID);                        

            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));                                                
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));            
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Create reconciliation (NDF)
             */                        
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "NDFs";
                    break;
                case 2:
                    strLabel = "NDFs";
                    break;                    
                case 3:
                    strLabel = "NDFs";
                    break;                    
            }
            
            CENTData1 = this.GetReconcile(0, strLabel, intIdArea, intIdTransaction, RECONCILE_1_1, intIdView1, intIdView1, RESULT_ALL, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdReconciliation = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "NDFs (Passo 1)";
                    break;
                case 2:
                    strLabel = "NDFs (Step 1)";
                    break;                    
                case 3:
                    strLabel = "NDFs (Paso 1)";
                    break;                    
            }
            
            CENTData1 = this.GetReconcileStep(0, intIdReconciliation, strLabel, "Ndf (Ticket)", "Ndf (Settle)", Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdStep = CENTData1.GetInt(SYSTEM_FIELD_ID);

            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "NDFs  (Rule 1)";
                    break;
                case 2:
                    strLabel = "NDFs  (Rule 1)";
                    break;                    
                case 3:
                    strLabel = "NDFs  (Rule 1)";
                    break;                    
            }            
            
            CENTData1 = this.GetReconcileStepRule(0, intIdReconciliation, intIdStep, strLabel, Yes, "");
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);
            intIdRule = CENTData1.GetInt(SYSTEM_FIELD_ID);                        

            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Ticket", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Date", ListCENTCatalogTransaction1), KEY_SEARCH, OPERATOR_EQUALS, 0, 0, Yes, ""));                                                
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Type", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Currency", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Amount", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Price", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));            
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Settlement", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Fixing", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            ListCENTData1.add(this.GetReconcileStepRuleField(0, intIdReconciliation, intIdStep, intIdRule, intIdTransaction, this.GetFieldId("RA.Counterparty", ListCENTCatalogTransaction1), COMPARE_CRITERIA, OPERATOR_EQUALS, 0, 0, Yes, ""));                        
            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Schedule the processes (Spot)
             */            
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Cambio";
                    break;
                case 2:
                    strLabel = "FX";
                    break;                    
                case 3:
                    strLabel = "Cambio";
                    break;                    
            }
            
            CENTData1 = this.GetSchedule(0, strLabel, AREA_FX, 8, "00:00", ""); // 8 (domain_22)
            CDALCore1.SetIdTransaction(TRN_SCHEDULE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
            intIdSchedule = CENTData1.GetInt(SYSTEM_FIELD_ID);
            
            ListCENTData1 = new ArrayList<CENTData>();
            
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Importar Operações";
                    break;
                case 2:
                    strLabel = "Import Transacitons";
                    break;                    
                case 3:
                    strLabel = "Importar Transacciónes";
                    break;                    
            }            
            
            ListCENTData1.add(this.GetScheduleJob(0, intIdSchedule, strLabel, SCHEDULE_TYPE_IMPORT_FILE, 1, "")); 
            
            
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Conciliar";
                    break;
                case 2:
                    strLabel = "Reconcile";
                    break;                    
                case 3:
                    strLabel = "Conferencia";
                    break;                    
            }
            
            ListCENTData1.add(this.GetScheduleJob(0, intIdSchedule, strLabel, SCHEDULE_TYPE_RECONCILE, 1, ""));
            ListCENTData1.add(this.GetScheduleJob(0, intIdSchedule, strLabel, SCHEDULE_TYPE_RECONCILE, 2, ""));            
            ListCENTData1.add(this.GetScheduleJob(0, intIdSchedule, strLabel, SCHEDULE_TYPE_RECONCILE, 3, ""));                        
            CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
           CENTData1 = null;
           CDALCore1 = null;
           ListCENTData1 = null;        
           ListCENTCatalogTransaction1 = null;      
           strLabel = null;
        }    
    }
    
    /*
     * Setup area to show entry manager in action (area, profiles, users, permissions, entries, etc)
     */        
    public void CreateDemoEntryManager(int intIdArea, int intIdEvent) throws Exception
    {
        /*
         * General Declaration
         */
        int intId = 0;
        int intPos = 0;
        int intIdView = 0;
        int intIdTransaction = 0;
        int intIdCompany = 0;
        int intIdLanguage = 0;
        int intIdTransactionEntity = 0;
        
            
        final int TYPE_PERSON = 1;
        final int TYPE_COMPANY = 2;        
        
        final int CLASSIFICATION_CUSTOMER = 1;
        final int CLASSIFICATION_SUPPLIER = 2;
        final int CLASSIFICATION_EMPLOYEE = 3;        
        final int CLASSIFICATION_OUTSOURCED = 3;        
        
        String strLabel = ""; 
        String strLanguage = "";
        CENTData CENTData1 = null;
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), CENTSession1);
        List<Integer> listTransactions = new ArrayList<Integer> ();
        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTCatalogTransaction1 = null;

        try
        {
            /*
             * Keep current company
             */
            intIdCompany = this.GetSession().GetInt(SESSION_COMPANY);

            /*
             * Get current language and create according
             */                       
            strLanguage = this.GetSession().GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals("pt"))
                intIdLanguage = 1; // Portuguese is default language
            else if (strLanguage.equals("en"))
                intIdLanguage = 2; // English
            else if (strLanguage.equals("sp"))
                intIdLanguage = 3; // Spanish

            
            /*
             * *****************************************************************
             * CREATE USER TRANSACTION ENTITIES (PEOPLE)
             * *****************************************************************
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Entidade";
                    break;
                case 2:
                    strLabel = "Entity";
                    break;
                case 3:
                    strLabel = "Entidad";
                    break;                    
            }            
            
            /*
             * Create transaction to manage entities
             */                                    
            CENTData1 = this.GetTransaction(0, strLabel, "main", MENU_ENTITY, TABLE_USER_2, TRANSACTION_TYPE_USER, "");
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);
            listTransactions.add(intIdTransaction);
            intIdTransactionEntity = intIdTransaction;
                        
            /*
             * Domain related to Entity
             */
            ListCENTData1 = new ArrayList<CENTData>();                 
            
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Tipo", 1, "Física", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Tipo", 2, "Jurídica", DOMAIN_USER, ""));    
            
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Classificacao", 1, "Cliente", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Classificacao", 2, "Fornecedor", DOMAIN_USER, ""));            
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Classificacao", 3, "Funcionário", DOMAIN_USER, ""));                        
            ListCENTData1.add(GetDomain(++ intId, "Entidade.Classificacao", 4, "Prestador de Serviço", DOMAIN_USER, ""));                                    
            
            CDALCore1.SetIdTransaction(TRN_DOMAIN);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Catalog fields related to entities
             */
            intPos = 0;
            ListCENTData1 = new ArrayList<CENTData>();     
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Nome/Razão Social", "nome", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_YES, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Apelido", "apelido", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Tipo", "tipo", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "Entidade.Tipo", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Classificação", "classificacao", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "Entidade.Classificacao", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create main view related to Entity (must be like transaction)
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

            intPos = 0;            
            ListCENTData1 = new ArrayList<CENTData>();                            
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("id", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("nome", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("apelido", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("classificacao", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }
            

            /*
             * Create view to count entity
             */
            for (int i=1; i<=2; i++)
            {                
                if (i == 1)
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Tipo [Grid]", intIdTransaction, "", DISPLAY_GRID, "");
                }
                else
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Tipo [Gráfico]", intIdTransaction, "", DISPLAY_CHART_COLUMNS, "");                                
                }
                
                CDALCore1.SetIdTransaction(TRN_VIEW);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
                CDALCore1.Persist(CENTData1, EVENT_INSERT);
                intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

                intPos = 0;            
                ListCENTData1 = new ArrayList<CENTData>();                            
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_COUNT, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_GROUP_BY, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));            

                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                for (CENTData CENTData3 : ListCENTData1)
                {
                    CDALCore1.Persist(CENTData3, EVENT_INSERT);
                }
            }
            
            for (int i=1; i<=2; i++)
            {                
                if (i == 1)
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Classifição [Grid]", intIdTransaction, "", DISPLAY_GRID, "");
                }
                else
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Classifição [Gráfico]", intIdTransaction, "", DISPLAY_CHART_SPLINE, "");                                
                }
                
                CDALCore1.SetIdTransaction(TRN_VIEW);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
                CDALCore1.Persist(CENTData1, EVENT_INSERT);
                intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

                intPos = 0;            
                ListCENTData1 = new ArrayList<CENTData>();                            
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("classificacao", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_COUNT, intIdTransaction, this.GetFieldId("classificacao", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_GROUP_BY, intIdTransaction, this.GetFieldId("classificacao", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));            

                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                for (CENTData CENTData3 : ListCENTData1)
                {
                    CDALCore1.Persist(CENTData3, EVENT_INSERT);
                }
            }            

            
            /*
             * Populate Entity example, different types
             */
           
            ListCENTData1 = new ArrayList<CENTData>();                            
            
            CENTData1 = new CENTData();
            CENTData1.SetText(1, "Adriano Bonifácio");
            CENTData1.SetText(2, "Adriano");
            CENTData1.SetInt(1, TYPE_PERSON);
            CENTData1.SetInt(2, CLASSIFICATION_CUSTOMER);
            ListCENTData1.add(CENTData1);
            
            CENTData1 = new CENTData();
            CENTData1.SetText(1, "Mariana Medeiros");
            CENTData1.SetText(2, "Mariana");
            CENTData1.SetInt(1, TYPE_PERSON);
            CENTData1.SetInt(2, CLASSIFICATION_CUSTOMER);
            ListCENTData1.add(CENTData1);            
            
            CENTData1 = new CENTData();
            CENTData1.SetText(1, "João Carlos da Silva");
            CENTData1.SetText(2, "João");
            CENTData1.SetInt(1, TYPE_PERSON);
            CENTData1.SetInt(2, CLASSIFICATION_CUSTOMER);
            ListCENTData1.add(CENTData1);

            CENTData1 = new CENTData();
            CENTData1.SetText(1, "Microsoft do Brasil");
            CENTData1.SetText(2, "Microsoft");
            CENTData1.SetInt(1, TYPE_COMPANY);
            CENTData1.SetInt(2, CLASSIFICATION_SUPPLIER);
            ListCENTData1.add(CENTData1);            
            
            CENTData1 = new CENTData();
            CENTData1.SetText(1, "Maria da Silva");
            CENTData1.SetText(2, "Maria");
            CENTData1.SetInt(1, TYPE_PERSON);
            CENTData1.SetInt(2, CLASSIFICATION_EMPLOYEE);
            ListCENTData1.add(CENTData1);

            CENTData1 = new CENTData();
            CENTData1.SetText(1, "Roberto da Costa");
            CENTData1.SetText(2, "Beto");
            CENTData1.SetInt(1, TYPE_PERSON);
            CENTData1.SetInt(2, CLASSIFICATION_OUTSOURCED);
            ListCENTData1.add(CENTData1);
            
            CDALCore1.SetIdTransaction(intIdTransaction);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }             
            
            
            
            
            /*
             * *****************************************************************
             * CREATE USER TRANSACTION ADDRESS
             * *****************************************************************
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Endereços";
                    break;
                case 2:
                    strLabel = "Address";
                    break;
                case 3:
                    strLabel = "Dirección";
                    break;                    
            }            
            
            /*
             * Create transaction to manage address
             */                                    
            CENTData1 = this.GetTransaction(0, strLabel, "main", MENU_ENTITY, TABLE_USER_3, TRANSACTION_TYPE_USER, "");
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);
            listTransactions.add(intIdTransaction);
                        
            /*
             * Domain related to Address
             */
            ListCENTData1 = new ArrayList<CENTData>();                 
            
            ListCENTData1.add(GetDomain(++ intId, "Endereco.Tipo", 1, "Correspondência", DOMAIN_USER, ""));
            
            CDALCore1.SetIdTransaction(TRN_DOMAIN);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Catalog fields related to address
             */
            intPos = 0;
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Entidade", "entidade", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, intIdTransactionEntity, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Tipo", "tipo", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "Endereco.Tipo", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Logradouro", "logradouro", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Numero", "numero", TYPE_TEXT, "text_2", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Complemento", "complemento", TYPE_TEXT, "text_3", 50, NULLABLE_YES, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Cep", "cep", TYPE_TEXT, "text_4", 10, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Estado", "estado", TYPE_TEXT, "text_5", 2, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Cidade", "cidade", TYPE_TEXT, "text_6", 100, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create main view related to Address (must be like transaction)
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

            intPos = 0;            
            ListCENTData1 = new ArrayList<CENTData>();                            
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("id", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("entidade", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("logradouro", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("numero", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("complemento", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("cep", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("estado", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("cidade", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }
            
            for (int i=1; i<=2; i++)
            {                
                if (i == 1)
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Estado [Grid]", intIdTransaction, "", DISPLAY_GRID, "");
                }
                else
                {
                    CENTData1 = this.GetView (0, strLabel + "::Total por Estado [Gráfico]", intIdTransaction, "", DISPLAY_CHART_SPLINE, "");                                
                }
                
                CDALCore1.SetIdTransaction(TRN_VIEW);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
                CDALCore1.Persist(CENTData1, EVENT_INSERT);
                intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

                intPos = 0;            
                ListCENTData1 = new ArrayList<CENTData>();                            
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("estado", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_COUNT, intIdTransaction, this.GetFieldId("estado", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
                ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_GROUP_BY, intIdTransaction, this.GetFieldId("estado", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));            

                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                for (CENTData CENTData3 : ListCENTData1)
                {
                    CDALCore1.Persist(CENTData3, EVENT_INSERT);
                }
            }               
            
            
            /*
             * Populate address example, one per entity
             */
            ListCENTData1 = new ArrayList<CENTData>();
            
            for (int i=1; i<=6; i++)
            {
                CENTData1 = new CENTData();
                
                CENTData1.SetInt(1, i);
                CENTData1.SetInt(2, 1);
                
                switch (i)
                {
                    case 1: 
                        CENTData1.SetText(1, "Av. Paulista");                    
                        break;
                    case 2: 
                        CENTData1.SetText(1, "Av. Angélica");
                        break;
                    case 3: 
                        CENTData1.SetText(1, "Av. João Dias");
                        break;
                    case 4: 
                        CENTData1.SetText(1, "Rua da Consolução");
                        break;
                    default:    
                        CENTData1.SetText(1, "Av. Roberto Marinho");                        
                }
                
                
                CENTData1.SetText(2, "100" + i);
                CENTData1.SetText(3, i + " andar");
                CENTData1.SetText(4, "04127-020");
                
                if (i<=3)
                {
                    CENTData1.SetText(5, "SP");
                    CENTData1.SetText(6, "São Paulo");
                }
                else
                {
                    CENTData1.SetText(5, "RJ");
                    CENTData1.SetText(6, "Rio de Janeiro");
                }
                
                
                ListCENTData1.add(CENTData1);
            }
            
            CDALCore1.SetIdTransaction(intIdTransaction);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }                
            
            
            

            /*
             * *****************************************************************
             * CREATE USER TRANSACTION DOCUMENTOS
             * *****************************************************************
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Documentos";
                    break;
                case 2:
                    strLabel = "Document";
                    break;
                case 3:
                    strLabel = "Documento";
                    break;                    
            }            
            
            /*
             * Create transaction to manage documents
             */                                    
            CENTData1 = this.GetTransaction(0, strLabel, "main", MENU_ENTITY, TABLE_USER_4, TRANSACTION_TYPE_USER, "");
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);
            listTransactions.add(intIdTransaction);
                        
            /*
             * Domain related to Documents
             */
            ListCENTData1 = new ArrayList<CENTData>();                 
            
            ListCENTData1.add(GetDomain(++ intId, "Documento.Tipo", 1, "Rg", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Documento.Tipo", 2, "Cpf", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Documento.Tipo", 3, "Cnpj", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Documento.Tipo", 4, "Passaporte", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Documento.Tipo", 5, "Reservista", DOMAIN_USER, ""));
            
            CDALCore1.SetIdTransaction(TRN_DOMAIN);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Catalog fields related to documents
             */
            intPos = 0;
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Entidade", "entidade", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, intIdTransactionEntity, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Tipo", "tipo", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, TRN_DOMAIN, "Documento.Tipo", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Numero", "numero", TYPE_TEXT, "text_1", 50,NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create main view related to Pessoa (must be like transaction)
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

            intPos = 0;            
            ListCENTData1 = new ArrayList<CENTData>();                            
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("id", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("entidade", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("numero", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }           
            
            /*
             * Populate document example, one per entity
             */
            ListCENTData1 = new ArrayList<CENTData>();
            
            for (int i=1; i<=6; i++)
            {
                CENTData1 = new CENTData();                
                CENTData1.SetInt(1, i);
                CENTData1.SetInt(2, 1); // RG
                CENTData1.SetText(1, "26.705.777-0" + i);
                ListCENTData1.add(CENTData1);
                
                CENTData1 = new CENTData();                
                CENTData1.SetInt(1, i);
                CENTData1.SetInt(2, 2); // CPF
                CENTData1.SetText(1, i + "7" + i + ".057.177-0" + i);
                ListCENTData1.add(CENTData1);
            }
            
            CDALCore1.SetIdTransaction(intIdTransaction);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }                            

            
            /*
             * *****************************************************************
             * CREATE USER TRANSACTION CONTATOS
             * *****************************************************************
             */
            switch (intIdLanguage)
            {
                case 1:
                    strLabel = "Contatos";
                    break;
                case 2:
                    strLabel = "Contact";
                    break;
                case 3:
                    strLabel = "Contato";
                    break;                    
            }            
            
            /*
             * Create transaction to manage documents
             */                                    
            CENTData1 = this.GetTransaction(0, strLabel, "main", MENU_ENTITY, TABLE_USER_5, TRANSACTION_TYPE_USER, "");
            CDALCore1.SetIdTransaction(TRN_TRANSACTION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);
            listTransactions.add(intIdTransaction);
                        
            /*
             * Domain related to Documents
             */
            ListCENTData1 = new ArrayList<CENTData>();                 
            
            ListCENTData1.add(GetDomain(++ intId, "Contato.Tipo", 1, "Fone Res.", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Contato.Tipo", 2, "Fone Com.", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Contato.Tipo", 3, "Celular", DOMAIN_USER, ""));
            ListCENTData1.add(GetDomain(++ intId, "Contato.Tipo", 4, "E-mail", DOMAIN_USER, ""));
            
            CDALCore1.SetIdTransaction(TRN_DOMAIN);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }
            
            /*
             * Catalog fields related to documents
             */
            intPos = 0;
            ListCENTData1 = new ArrayList<CENTData>();
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, NULLABLE_NO, UNIQUE_NO, PK_YES, FK_NO, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Entidade", "entidade", TYPE_INT, "int_1", 0, NULLABLE_NO, UNIQUE_NO, PK_NO, intIdTransactionEntity, "", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Tipo", "tipo", TYPE_INT, "int_2", 0, NULLABLE_NO, UNIQUE_NO, No, TRN_DOMAIN, "Contato.Tipo", ++intPos, ""));
            ListCENTData1.add(this.GetCatalog(0, intIdTransaction, "Numero", "numero", TYPE_TEXT, "text_1", 50, NULLABLE_NO, UNIQUE_NO, PK_NO, FK_NO, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_CATALOG);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                    
            for (CENTData CENTData2 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData2, EVENT_INSERT);
            }

            /*
             * Create main view related to Pessoa (must be like transaction)
             */
            CENTData1 = this.GetView (0, strLabel, intIdTransaction, "", DISPLAY_GRID, "");
            CDALCore1.SetIdTransaction(TRN_VIEW);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
            CDALCore1.Persist(CENTData1, EVENT_INSERT);                        
            intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

            intPos = 0;            
            ListCENTData1 = new ArrayList<CENTData>();                            
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("id", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("entidade", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("tipo", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));
            ListCENTData1.add(this.GetViewDef(0, intIdView, COMMAND_SELECT_FIELD, intIdTransaction, this.GetFieldId("numero", this.GetCatalog(intIdTransaction)), 0, "", ++intPos, ""));

            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }
            
            /*
             * Populate contact example, one per entity
             */
            ListCENTData1 = new ArrayList<CENTData>();
            
            for (int i=1; i<=6; i++)
            {
                CENTData1 = new CENTData();                
                CENTData1.SetInt(1, i);
                CENTData1.SetInt(2, 3); // Mobile
                CENTData1.SetText(1, "55 11 9 9090-505" + i);
                ListCENTData1.add(CENTData1);
                
                CENTData1 = new CENTData();                
                CENTData1.SetInt(1, i);
                CENTData1.SetInt(2, 4); // Email
                CENTData1.SetText(1, "email" + i + "@gmail.com.br");
                ListCENTData1.add(CENTData1);
            }
            
            CDALCore1.SetIdTransaction(intIdTransaction);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            for (CENTData CENTData3 : ListCENTData1)
            {
                CDALCore1.Persist(CENTData3, EVENT_INSERT);
            }                            
            

            /*
             * Give necessary permissions for each new transaction
             */            
            for (int transaction : listTransactions)
            {
                /*
                 * Profile x Transaction
                 */
                ListCENTData1 = new ArrayList<CENTData>();
                ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_OWNER, transaction, ""));
                ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_ADMIN, transaction, ""));
                ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_MANAGER, transaction, ""));
                ListCENTData1.add(this.GetProfileTransaction(0, PROFILE_ANALYST, transaction, ""));
                CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
                for (CENTData CENTData2 : ListCENTData1)
                {
                    CDALCore1.Persist(CENTData2, EVENT_INSERT);
                }                                                    

                /*
                 * Transaction x Function (1-PROFILE_OWNER; 2-PROFILE_ADMIN; 3-PROFILE_MANAGER (see constants))
                 */
                for (int i=1; i<=3; i++)
                {
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_NEW, ""));
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_EDIT, ""));
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_SAVE, ""));
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_DELETE, ""));
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_DUPLICATE, ""));                                                                        
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_FILTER, ""));
                    ListCENTData1.add(this.GetTransactionFunction(0, i, transaction, FUNCTION_EXPORT, ""));                                        
                }

                CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);                        
                for (CENTData CENTData2 : ListCENTData1)
                {
                    CDALCore1.Persist(CENTData2, EVENT_INSERT);
                }
            }


        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }        
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
           CENTData1 = null;
           CDALCore1 = null;
           ListCENTData1 = null;        
           ListCENTCatalogTransaction1 = null;      
           strLabel = null;
        }    
    }
    
    
    
    
    
}