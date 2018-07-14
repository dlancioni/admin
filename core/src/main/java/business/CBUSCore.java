package business;

import connection.ConnectionFactory;
import entity.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import persistence.CDALCore;
import java.util.*;
import persistence.CDALSetup;

public class CBUSCore extends CBUS
{           
    public CBUSCore(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
      
    /*
     * Generic methods
     */    
    public int ExecuteEvent(int intIdEvent, List<CENTData> ListCENTData1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */        
        int intId = 0;
        int intIdArea = 0;        
        int intIdMatchAs = 0;
        String strName = "";
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTCatalog1 = null;
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
        CBUSSetup CBUSSetup1 = new CBUSSetup(this.GetConnection(), this.GetSession());

        try
        {            
            /*
             * Start the transaction
             */
            this.GetConnection().setAutoCommit(false);
            
            /*
             * Handle individual events
             */            
            switch (intIdEvent)
            {
                /*
                 * Do regular crud operation (insert, update, delete)
                 */
                case EVENT_INSERT:
                case EVENT_UPDATE:
                case EVENT_DELETE:

                    /*
                     * Get the base catalog
                     */
                    ListCENTCatalog1 = this.GetCatalog(this.GetIdTransaction());                                
                    
                    /*
                     * Persist each item
                     */            
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        /*
                         * Basic validation
                         */            
                        if (intIdEvent == EVENT_INSERT || intIdEvent == EVENT_UPDATE)
                        {
                            this.Validate(ListCENTCatalog1, CENTData1, intIdEvent);
                        }

                        if (intIdEvent == EVENT_DELETE)
                        {
                            this.ValidateFK(ListCENTCatalog1, CENTData1, intIdEvent);
                        }                                                
                        
                        /*
                         * Apply the business rules according to the transaction
                         */
                        switch (this.GetIdTransaction())
                        {
                            case TRN_COMPANY:
                                
                                Company(CENTData1, intIdEvent);                    
                                break;

                            case TRN_AREA:
                                
                                Area(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;
                                
                            case TRN_PROFILE:
                                
                                Profile(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;                                

                            /*
                             * Transação x Catalogo   
                             */
                            case TRN_MENU:
                                
                                intId = Menu(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;                                       
                                
                            case TRN_TRANSACTION:
                                
                                intId = Transaction(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;                

                            case TRN_CATALOG:
                                
                                Catalog(ListCENTCatalog1, CENTData1, intIdEvent, true);
                                break;

                            case TRN_VIEW:
                                
                                View(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;
                                
                            case TRN_VIEW_DEF:
                                
                                intIdMatchAs = Integer.valueOf(this.GetPropertieValue("MATCH_AS"));
                                /*
                                 * Get the current view transaction
                                 */
                                if (intIdEvent != EVENT_DELETE)
                                {                                
                                    CENTFilter1 = new CENTData();
                                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, CENTData1.GetInt(GetFieldObject("id_view", ListCENTCatalog1)));

                                    this.SetIdTransaction(TRN_VIEW);
                                    this.SetIdView(DO_NOT_USE_VIEW);                                
                                    ListCENTData1 = this.GetList(CENTFilter1);

                                    for (CENTData CENTView1 : ListCENTData1)
                                    {
                                        intId = CENTView1.GetInt(GetFieldObject("id_transaction", this.GetCatalog(TRN_VIEW)));
                                    }       

                                    /*
                                     * Cannot cross transactions (joins), match as row don't allow cross
                                     */
                                    if (CENTData1.GetInt(GetFieldObject("id_transaction", ListCENTCatalog1)) != intId)
                                    {
                                        if (intIdMatchAs == MATCH_AS_COL)
                                        {
                                            throw new CENTException("EXCEPTION_CANNOT_SELECT_FIELD_DIFF_TRANSACTION");
                                        }
                                    }
                                }
                                
                                /*
                                 * Persist the record (except for companies)
                                 */
                                CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                                CDALCore1.Persist(CENTData1, intIdEvent);
                                
                                break;

                            /*
                             * Layout
                             */
                            case TRN_LAYOUT:
                                
                                Layout(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;

                            case TRN_LAYOUT_SESSION:
                                
                                LayoutSession(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;

                            case TRN_LAYOUT_SESSION_DEFINITION:
                                
                                LayoutSessionDefinition(ListCENTCatalog1, CENTData1, intIdEvent);                    
                                break;

                            /*
                             * Reconcile
                             */                        
                            case TRN_RECONCILIATION:
                                
                                Reconcile(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;

                            case TRN_RECONCILIATION_STEP:
                                
                                ReconcileStep(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;

                            case TRN_RECONCILIATION_STEP_RULE:
                                
                                ReconcileStepRule(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;

                            case TRN_RECONCILIATION_STEP_RULE_FIELD:
                                
                                ReconcileStepRuleField(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;

                            case TRN_SCHEDULE:
                                
                                Scheduler(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;                                
                                
                            case TRN_QUEUE:
                                
                                Queue(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;

                            case TRN_FILE_MANAGER:
                                
                                FileManager(ListCENTCatalog1, CENTData1, intIdEvent);
                                break;                                
                                
                            default:    

                                /*
                                 * Persist the record (except for companies)
                                 */
                                CDALCore1.SetIdTransaction(this.GetIdTransaction());
                                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                                CDALCore1.Persist(CENTData1, intIdEvent);
                        }

                    }
                
                    break;
                
                /*
                 * Create a new record at queue to import data    
                 */
                case EVENT_IMPORT:
                case EVENT_EXPORT:
                case EVENT_RECONCILE:
                    
                    /*
                     * Get queue catalog
                     */            
                    ListCENTCatalog1 = this.GetCatalog(TRN_QUEUE);
                    
                    /*
                     * Get the transaction destination
                     */
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        intId = CENTData1.GetInt(SYSTEM_FIELD_ID);
                        
                        CENTData1.SetInt(GetFieldObject("id", ListCENTCatalog1), 0);
                        CENTData1.SetDate(GetFieldObject("date", ListCENTCatalog1), this.GetSession().GetDate(SESSION_DATE));

                        switch (intIdEvent)
                        {
                            case EVENT_IMPORT:
                                
                                /*
                                 * Get layout details
                                 */
                                CENTFilter1 = new CENTData();
                                CENTFilter1.SetInt(SYSTEM_FIELD_ID, intId);
                                this.SetIdTransaction(TRN_LAYOUT);
                                this.SetIdView(DO_NOT_USE_VIEW);
                                strName = this.GetList(CENTFilter1).get(0).GetText(1);
                                intIdArea = this.GetList(CENTFilter1).get(0).GetInt(2);
                                
                                CENTData1.SetInt(GetFieldObject("id_type", ListCENTCatalog1), QUEUE_TYPE_IMPORT_FILE);
                                CENTData1.SetText(GetFieldObject("name_service", ListCENTCatalog1), strName);
                                break;
                                
                            case EVENT_RECONCILE:
                                
                                /*
                                 * Get reconciliation details
                                 */
                                CENTFilter1 = new CENTData();
                                CENTFilter1.SetInt(SYSTEM_FIELD_ID, intId);
                                this.SetIdTransaction(TRN_RECONCILIATION);
                                this.SetIdView(DO_NOT_USE_VIEW);
                                strName = this.GetList(CENTFilter1).get(0).GetText(1);     
                                intIdArea = this.GetList(CENTFilter1).get(0).GetInt(2);                                
                                
                                CENTData1.SetText(GetFieldObject("name_service", ListCENTCatalog1), strName);                                
                                CENTData1.SetInt(GetFieldObject("id_type", ListCENTCatalog1), QUEUE_TYPE_RECONCILIATION);
                                break;                                
                        }                        
                        
                        CENTData1.SetInt(GetFieldObject("id_service", ListCENTCatalog1), intId);
                        CENTData1.SetInt(GetFieldObject("id_status", ListCENTCatalog1), QUEUE_STATUS_PENDING);
                        CENTData1.SetInt(GetFieldObject("id_user", ListCENTCatalog1), this.GetSession().GetInt(SESSION_USER));
                        CENTData1.SetText(GetFieldObject("description", ListCENTCatalog1), "");
                        CENTData1.SetText(GetFieldObject("note", ListCENTCatalog1), "");

                        CDALCore1.GetSession().SetInt(SESSION_AREA, intIdArea);                        
                        
                        CDALCore1.SetIdTransaction(TRN_QUEUE);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTData1, EVENT_INSERT);
                    }                    
                    
                    break;

                case EVENT_SETUP:
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CBUSSetup1.Setup(CENTData1);
                    }                    
                    
                    break;
                    
                case EVENT_DUPLICATE:
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CBUSSetup1.SetIdTransaction(this.GetIdTransaction());
                        CBUSSetup1.Duplicate(CENTData1);
                    }                    
                    
                    break;
                    
                case EVENT_REPROCESS:
                                        
                    /*
                     * Get the base catalog
                     */
                    ListCENTCatalog1 = this.GetCatalog(TRN_QUEUE);
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        intId = CENTData1.GetInt(SYSTEM_FIELD_ID);
                        
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(SYSTEM_FIELD_ID, intId);                        
                        CDALCore1.SetIdTransaction(TRN_QUEUE);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        ListCENTData1 = CDALCore1.GetList(CENTData1);
                        
                        for (CENTData CENTData2 : ListCENTData1)
                        {
                            CENTData2.SetText(GetFieldObject("start_date", ListCENTCatalog1), "");
                            CENTData2.SetText(GetFieldObject("end_date", ListCENTCatalog1), "");
                            CENTData2.SetInt(GetFieldObject("id_status", ListCENTCatalog1), QUEUE_STATUS_PENDING);
                            CENTData2.SetInt(GetFieldObject("id_user", ListCENTCatalog1), this.GetSession().GetInt(SESSION_USER));
                            CENTData2.SetText(GetFieldObject("description", ListCENTCatalog1), "");
                            
                            CDALCore1.SetIdTransaction(TRN_QUEUE);
                            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                            CDALCore1.Persist(CENTData2, EVENT_UPDATE);
                        }
                    }                  
                    
                    break;                                       
            }    
                

            /*
             * Sucess
             */
            this.GetConnection().commit();

            /*
             * Return the id
             */            
            return intId;

        }    
        catch (CENTException CENTException1)
        {
            /*
             * Undo the transaction
             */
            this.GetConnection().rollback();
            
            /*
             * Throw it
             */
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
            /*
             * Undo the transaction
             */
            this.GetConnection().rollback();
            
            /*
             * Throw it
             */            
            throw new CENTException(this.Translate("EXCEPTION_FAIL_BUSINESS"), Exception1.getMessage());
        } 
        finally
        {
            strName = null;
            CENTFilter1 = null;
            ListCENTCatalog1 = null;
            CDALCore1 = null;
            CBUSSetup1 = null;
        }
    }    
    
    public List<CENTData> GetList(CENTData CENTData1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */                                   
        String strFieldName = "";
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
        List<CENTData> ListCENTData2 = null;

        try
        {            
            /*
             * Validate the view id
             */            
            if (this.GetIdView() == 0)
            {
                throw new CENTException("EXCEPTION_INVALID_VIEW_ID");
            }
            
            /*
             * Business rules before query
             */            
            switch (this.GetIdTransaction())
            {
                /*
                 * User transactions only
                 */
                case TRN_ALL:
                    
                    // Do something
                    break;
                    
                default:
            }

            /*
             * Qery the record
             */
            CDALCore1.SetIdTransaction(this.GetIdTransaction());
            CDALCore1.SetIdView(this.GetIdView());    
            ListCENTData2 = CDALCore1.GetList(CENTData1);     
            
            /*
             * Business rules after query
             */            
            switch (this.GetIdTransaction())
            {
                case TRN_USER:
             
                    /*
                     * Hide the password before present into screen

                    strFieldName = GetFieldObject("password", GetCatalog(TRN_USER));
                    
                    for (CENTData CENTData3 : ListCENTData2)
                    {
                        CENTData3.SetText(strFieldName, "***");
                    }            
                     */                                        
                    break;
                                        
                default:
            }            
                        
            /*
             * Return the list
             */
            return ListCENTData2;    

        }    
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw new CENTException(this.Translate("EXCEPTION_FAIL_BUSINESS"), Exception1.getMessage());
        } 
        finally
        {
            strFieldName = null;
            CDALCore1 = null;  
            ListCENTData2 = null;
        }
    }

    public List<CENTData> GetList(List<CENTData> ListCENTView1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */         
        String strFieldName = "";
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
        List<CENTData> ListCENTData2 = null;

        try
        {            
            /*
             * Validate the view id
             */            
            if (ListCENTView1.isEmpty())
            {
                throw new CENTException("EXCEPTION_INVALID_VIEW_ID");
            }
            
            /*
             * Business rules to query
             */            
            switch (this.GetIdTransaction())
            {
                case TRN_MATCH:
                    //ListCENTView1.add(FilterCriteria(this.GetIdTransaction(), SYSTEM_FIELD_SYSTEM_DATE, TYPE_DATE, OPERATOR_EQUALS, DateToString(this.GetSession().GetDate(SESSION_DATE))));
                    break;

                case TRN_USER:
                    //ListCENTView1.add(FilterCriteria(this.GetIdTransaction(), SYSTEM_FIELD_SYSTEM_DATE, TYPE_DATE, OPERATOR_EQUALS, DateToString(this.GetSession().GetDate(SESSION_DATE))));
                    break;                                                            
            }
            
            /*
             * Persist the record
             */
            CDALCore1.SetIdTransaction(this.GetIdTransaction());
            CDALCore1.SetIdView(this.GetIdView());    
            ListCENTData2 = CDALCore1.GetList(ListCENTView1);
            
            /*
             * Persist the record
             */            
            switch (this.GetIdTransaction())
            {
                case TRN_USER:
                    
                    strFieldName = GetFieldObject("password", GetCatalog(TRN_USER));
                    
                    for (CENTData CENTData3 : ListCENTData2)
                    {
                        CENTData3.SetText(strFieldName, "***");
                    }

                    break;                    
            }            
            
            /*
             * Persist the record
             */
            return ListCENTData2;    

        }    
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            throw new CENTException(this.Translate("EXCEPTION_FAIL_BUSINESS"), Exception1.getMessage());
        } 
        finally
        {
            strFieldName = null;
            CDALCore1 = null;  
            ListCENTData2 = null;
        }
    }    
    

    /*
     * Specific methods
     */
    public CENTData Login(CENTData CENTData1) throws CENTException, Exception
    {
        /*
         * General Declaration
         */        
        int intIdCompany = 0;
        int intIdProfile = 0;
        int intIdArea = 0;
        
        String strUsername = "";
        String strPassword = "";
        
        CENTData CENTFilter1 = null;        
        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTCatalog1 = null;
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());            
                
        try
        {            
            /*
             * Get information to login
             */            
            intIdCompany = CENTData1.GetInt(1);
            strUsername =  CENTData1.GetText(1);
            strPassword =  CENTData1.GetText(2);   

            if (intIdCompany == 0)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Login.Company", "txtCompany");
            }
            
            /*
             * Validate the company
             */                    
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdCompany);
            
            CDALCore1.SetIdTransaction(TRN_COMPANY);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);
            
            if (ListCENTData1.isEmpty())
            {
                /*
                 * Must inform default company to access the catalog
                 */
                this.GetSession().SetInt(SESSION_COMPANY, SYSTEM_DEFAULT_COMPANY);
                
                /*
                 * Throw the exception
                 */
                throw new CENTException("EXCEPTION_COMPANY_NOT_FOUND");
            }
            
            

            if (strUsername.trim().equals(""))
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Login.Username", "txtUsername");
            }

            if (strPassword.trim().equals(""))
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Login.Password", "txtPassword");
            }

            /*
             * Query user information to display
             */        
            ListCENTCatalog1 = GetCatalog(TRN_USER);            
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetText(GetFieldObject("username", ListCENTCatalog1), strUsername);
            CENTFilter1.SetText(GetFieldObject("password", ListCENTCatalog1), strPassword);
            CENTFilter1.SetInt(SYSTEM_FIELD_ID_COMPANY, intIdCompany);
            
            CDALCore1.SetIdTransaction(TRN_USER);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);
            
            if (ListCENTData1.isEmpty())
            {
                throw new CENTException("EXCEPTION_USER_NOT_FOUND");
            }

            for (CENTData CENTData2 : ListCENTData1)
            {
                CENTSession1.SetInt(SESSION_USER, CENTData2.GetInt(GetFieldObject("id", ListCENTCatalog1)));
                CENTSession1.SetText(SESSION_USER, CENTData2.GetText(GetFieldObject("username", ListCENTCatalog1)));
                
                intIdProfile = CENTData2.GetInt(GetFieldObject("id_profile", ListCENTCatalog1));
                intIdArea = CENTData2.GetInt(GetFieldObject("id_area", ListCENTCatalog1));
            }

            /*
             * Query company information to display
             */        
            ListCENTCatalog1 = GetCatalog(TRN_COMPANY);
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdCompany);

            CDALCore1.SetIdTransaction(TRN_COMPANY);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {               
                CENTSession1.SetInt(SESSION_COMPANY, CENTData2.GetInt(GetFieldObject("id", ListCENTCatalog1)));
                CENTSession1.SetText(SESSION_COMPANY, CENTData2.GetText(GetFieldObject("name", ListCENTCatalog1)));
            }        

            /*
             * Query user information to display
             */
            ListCENTCatalog1 = GetCatalog(TRN_PROFILE);
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdProfile);
            CDALCore1.SetIdTransaction(TRN_PROFILE);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {
                CENTSession1.SetInt(SESSION_PROFILE, CENTData2.GetInt(GetFieldObject("id", ListCENTCatalog1)));
                CENTSession1.SetText(SESSION_PROFILE, CENTData2.GetText(GetFieldObject("name", ListCENTCatalog1)));
            }

            /*
             * Query user information to display
             */        
            ListCENTCatalog1 = GetCatalog(TRN_AREA);     
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdArea);
            CDALCore1.SetIdTransaction(TRN_AREA);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {
                CENTSession1.SetInt(SESSION_AREA, CENTData2.GetInt(GetFieldObject("id", ListCENTCatalog1)));
                CENTSession1.SetText(SESSION_AREA, CENTData2.GetText(GetFieldObject("name", ListCENTCatalog1)));
            }                        
            
            /*
             * Regional settings according to the culture
             */                    
            if (CENTData1.GetText(SESSION_LANGUAGE).toLowerCase().equals("pt"))
            {
                CENTSession1.SetText(SESSION_MASK_DATE, "dd/MM/yyyy");
                CENTSession1.SetText(SESSION_DECIMAL_SIMBOL, ",");
            }
            
            if (CENTData1.GetText(SESSION_LANGUAGE).toLowerCase().equals("en"))
            {
                CENTSession1.SetText(SESSION_MASK_DATE, "MM/dd/yyyy");
                CENTSession1.SetText(SESSION_DECIMAL_SIMBOL, ".");
            }
            
            if (CENTData1.GetText(SESSION_LANGUAGE).toLowerCase().equals("sp"))
            {
                CENTSession1.SetText(SESSION_MASK_DATE, "dd/MM/yyyy");
                CENTSession1.SetText(SESSION_DECIMAL_SIMBOL, ",");
            }

            CENTSession1.SetText(SESSION_COUNTRY, CENTData1.GetText(SESSION_COUNTRY).toLowerCase());
            CENTSession1.SetText(SESSION_LANGUAGE, CENTData1.GetText(SESSION_LANGUAGE).toLowerCase());
            
            /*
             * Set the system date
             */
            if (CENTSession1.GetDate(SESSION_DATE) == null)
                CENTSession1.SetDate(SESSION_DATE, new Date());            
            
            /*
             * Return the login status
             */            
            return CENTSession1;

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
            strUsername = null;
            strPassword = null;

            CENTFilter1 = null;        
            ListCENTData1 = null;        
            CDALCore1 = null;
        }
    }    

    public List<CENTData> GetPermission(CENTData CENTSession1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */                           
        List<CENTData> ListCENTData1 = null;
        CDALSetup CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());

        try
        {
            CDALSetup1.SetIdTransaction(this.GetIdTransaction());            
            ListCENTData1 = CDALSetup1.GetPermission(CENTSession1);
            
            return ListCENTData1;

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
            ListCENTData1 = null;
        }
    }
    
    public List<CENTData> GetListOfCompany() throws CENTException, SQLException
    {
        /*
         * General Declaration
         */                           
        CDALSetup CDALSetup1 = new CDALSetup(this.GetConnection(), this.GetSession());
        List<CENTData> ListCENTData1 = null;        
        
        try
        {
            CDALSetup1.SetIdTransaction(this.GetIdTransaction());            
            ListCENTData1 = CDALSetup1.GetListOfCompany();
            
            
            return ListCENTData1;

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
            ListCENTData1 = null;
        }
    }   
    
    public List<CENTData> GetDictionary() throws CENTException
    {
        /*
         * General Declaration
         */              
        int intIdLanguage = 0;
        
        CENTData CENTData1 = null;

        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTCatalog1 = null;
        List<CENTData> ListCENTCatalog2 = null;

        try
        {           
            /*
             * Get the dictionaries
             */            
            ListCENTCatalog1 = this.GetCatalog(TRN_LANGUAGE);
            ListCENTCatalog2 = this.GetCatalog(TRN_DICTIONARY);
            
            /*
             * Get the language id
             */                        
            CENTData1 = new CENTData();
            CENTData1.SetText(GetFieldObject("language_code", ListCENTCatalog1), this.GetSession().GetText(SESSION_LANGUAGE));            
            this.SetIdTransaction(TRN_LANGUAGE);
            this.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = GetList(CENTData1);
            
            if (!ListCENTData1.isEmpty())
            {
                intIdLanguage = ListCENTData1.get(0).GetInt(GetFieldObject("id", ListCENTCatalog1));
            }
            
            /*
             * Get the language id
             */                        
            CENTData1 = new CENTData();
            CENTData1.SetInt(GetFieldObject("id_language", ListCENTCatalog2), intIdLanguage);
            this.SetIdTransaction(TRN_DICTIONARY);
            this.SetIdView(DO_NOT_USE_VIEW);            
            ListCENTData1 = GetList(CENTData1);            
            
            /*
             * Return the dictionary for current language
             */       
            return ListCENTData1;

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
            CENTData1 = null;
            ListCENTData1 = null;        
            ListCENTCatalog1 = null;
            ListCENTCatalog2 = null;
        }
    }

    public void ImplementSystemInstance() throws CENTException, SQLException
    {
        /*
         * General Declaration
         */                         
        int intId = 0;
        CDALCore CDALCore1 = null;
        CBUSSetup CBUSSetup1 = null;
        ConnectionFactory ConnectionFactory1 = null;
        List<CENTData> ListCENTCatalog1 = null;
        CENTData CENTData1 = null;

        try
        {                     
            /*
             * Start the transaction
             */
            this.GetConnection().setAutoCommit(false);

            /*
             * Recreate the system
             */            
            ConnectionFactory1 = new ConnectionFactory();            
            CBUSSetup1 = new CBUSSetup(this.GetConnection(), this.GetSession());
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
                        
            /*
             * Create the system instance
             */
            CBUSSetup1.ImplementSystemInstance();
                        
            /*
             * Sucess
             */
            this.GetConnection().commit();
            
        }    
        catch (CENTException CENTException1)
        {
            this.GetConnection().rollback();
            throw CENTException1;
        } 
        catch (Exception Exception1)
        {
            this.GetConnection().rollback();
            throw new CENTException("EXCEPTION_FAIL_BUSINESS", Exception1.getMessage());
        } 
        finally
        {
            CBUSSetup1 = null;
        }
    }

    /*
     * private methods used to isolate the logics among the main methods Persist
     */        
    private int Menu(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException, Exception
    {        
        /*
         * General declaration
         */
        int i = 0;
        int intIdMenu = 0;
        int intIdMenuType = 0;
        
        CDALCore CDALCore1 = null;
        CBUSSetup CBUSSetup1 = null;
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        List<CENTData> ListCENTCatalog3 = null;
        

        try
        {
            /*
             * Create the objects
             */        
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            CBUSSetup1 = new CBUSSetup(this.GetConnection(), this.GetSession());
            
            /*
             * Get current transaction to be deleted
             */                    
            intIdMenu = CENTData1.GetInt(SYSTEM_FIELD_ID);         

            /*
             * Custom validation
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT:                                                                        
                case EVENT_UPDATE:

                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    break;
            }
            
            /*
             * Persist the information
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT: 

                    /*
                     * Insert current menu
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    
                    break;                    
                    
                case EVENT_UPDATE:
                    
                    /*
                     * Insert current menu
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    
                    break;                    

                case EVENT_DELETE:

                    /*
                     * Query details about menu
                     */                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdMenu);
                    CDALCore1.SetIdTransaction(TRN_MENU);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTCatalog3 = CDALCore1.GetList(CENTFilter1);
                    
                    if (ListCENTCatalog3.size() > 0)
                    {
                        /*
                         * Get current item
                         */
                        CENTData1 = ListCENTCatalog3.get(0);                        
                        
                        /*
                         * Figure out menu type
                         */
                        intIdMenuType = CENTData1.GetInt(GetFieldObject("id_type", ListCENTCatalog1));

                        /*
                         * Cannot delete system menu
                         */                        
                        if (intIdMenuType == MENU_TYPE_SYSTEM)
                        {
                            throw new CENTException("EXCEPTION_CANNOT_DELETE_MENU_TYPE_SYSTEM");
                        }
                        
                        /*
                         * Delete selected menu
                         */                    
                        CDALCore1.SetIdTransaction(this.GetIdTransaction());
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTData1, intIdEvent);
                        
                    }
                    
                    break;
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
            CDALCore1 = null;
            CBUSSetup1 = null;
            CENTFilter1 = null;
            ListCENTCatalog2 = null;
            ListCENTCatalog3 = null;
        }        
        
        /*
         * Delete the transaction
         */        
        return intIdMenu;        

    }        
        
    
    private int Transaction(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException, Exception
    {        
        /*
         * General declaration
         */
        int i = 0;
        int intIdTransaction = 0;
        int intIdTransactionType = 0;
        int intIdLayout = 0;
        int intIdReconcile = 0;
        
        String strNote = "";
        String strName = "";
        CDALCore CDALCore1 = null;
        CBUSSetup CBUSSetup1 = null;
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        List<CENTData> ListCENTCatalog3 = null;

        try
        {
            /*
             * Create the objects
             */        
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            CBUSSetup1 = new CBUSSetup(this.GetConnection(), this.GetSession());

            /*
             * Custom validation
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT:                                                                        
                case EVENT_UPDATE:

                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    break;
            }
            
            /*
             * Persist the information
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT: 

                    /*
                     * Create the transaction
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());                    
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);
                    intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);
                    strName = CENTData1.GetText(GetFieldObject("name", ListCENTCatalog1));
                    intIdTransactionType = CENTData1.GetInt(GetFieldObject("id_type", ListCENTCatalog1));
                    
                    // CREATE RELATIVE SYSTEM OBJECTS     
                    
                    /*
                     * Add permission to administrator on new transaction
                     */                    
                    CENTData1 = CBUSSetup1.GetProfileTransaction(0, PROFILE_OWNER, intIdTransaction, "");
                    CDALCore1.SetIdView(TRN_PROFILE_TRANSACTION);
                    CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);                    
                    
                    CENTData1 = CBUSSetup1.GetProfileTransaction(0, PROFILE_ADMIN, intIdTransaction, "");
                    CDALCore1.SetIdView(TRN_PROFILE_TRANSACTION);
                    CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);
                    
                    /*
                     * Add permission to administrator on new transaction (functions)
                     */
                    CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);                    
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_NEW, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_EDIT, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_DELETE, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_SAVE, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_FILTER, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_OWNER, intIdTransaction, FUNCTION_EXPORT, ""), EVENT_INSERT);

                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_NEW, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_EDIT, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_DELETE, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_SAVE, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_FILTER, ""), EVENT_INSERT);
                    CDALCore1.Persist(CBUSSetup1.GetTransactionFunction(0, PROFILE_ADMIN, intIdTransaction, FUNCTION_EXPORT, ""), EVENT_INSERT);                    
                    
                    /*
                     * Create the view (all views are updated when somebody change the catalog)
                     */
                    i = 1;
                    CENTData1 = CBUSSetup1.GetView(0, strName, intIdTransaction, "", DISPLAY_GRID, strNote);
                    CDALCore1.SetIdView(TRN_VIEW);
                    CDALCore1.SetIdTransaction(TRN_VIEW);
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);

                    /*
                     * System fields (critical, used to report matches)
                     */
                    CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Id", "id", TYPE_INT, SYSTEM_FIELD_ID, 0, No, No, Yes, 0, "", 1, "");
                    Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, true); // add in catalog and view                    


                    /*
                     * When transaction is type of Reconciliation area, must add fields used to control reconciliation proces
                     */
                    if (intIdTransactionType == 3) // See domain_26 to understand this field
                    {
                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.MatchId", "id_match", TYPE_INT, SYSTEM_FIELD_MATCH_ID, 0, Yes, No, No, 0, "", 2, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.ProcessId", "id_process", TYPE_INT, SYSTEM_FIELD_ID_PROCESS, 0, Yes, No, No, 0, "", 3, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.MatchDate", "date_match", TYPE_DATE, SYSTEM_FIELD_MATCH_DATE, 0, Yes, No, No, 0, "", 4, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                    

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Reconciliation", "reconcile", TYPE_TEXT, SYSTEM_FIELD_MATCH_RECONCILE, 50, Yes, No, No, 0, "", 5, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                    

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Step", "step", TYPE_TEXT, SYSTEM_FIELD_MATCH_STEP, 50, Yes, No, No, 0, "", 6, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                                        

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Rule", "rule", TYPE_TEXT, SYSTEM_FIELD_MATCH_RULE, 50, Yes, No, No, 0, "", 7, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                        

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Status", "status", TYPE_INT, SYSTEM_FIELD_MATCH_ID_STATUS, 0, Yes, No, No, TRN_DOMAIN, "domain_10", 8, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                                            

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Field", "field", TYPE_TEXT, SYSTEM_FIELD_MATCH_FIELD, 50, Yes, No, No, 0, "", 9, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                                        

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Difference", "difference", TYPE_TEXT, SYSTEM_FIELD_MATCH_DIFF, 50, Yes, No, No, 0, "", 10, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                                                            

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.Side", "side", TYPE_TEXT, SYSTEM_FIELD_MATCH_SIDE, 50, Yes, No, No, 0, "", 11, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false); // add in catalog and view                         

                        CENTData1 = CBUSSetup1.GetCatalog(0, intIdTransaction, "RA.DataSource", "data_source", TYPE_TEXT, SYSTEM_FIELD_DATA_SOURCE, 50, Yes, No, No, 0, "", 12, "");
                        Catalog(ListCENTCatalog1, CENTData1, EVENT_INSERT, false);                    
                    }    
                    
                case EVENT_UPDATE:

                    /*
                     * Delete the transaction
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, EVENT_UPDATE);
                    
                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    /*
                     * Get current transaction to be deleted
                     */                    
                    intIdTransaction = CENTData1.GetInt(SYSTEM_FIELD_ID);                    
                    
                    /*
                     * Delete associated layout, layout session and definitions
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_LAYOUT);                       
                    
                    if (ListCENTCatalog2.size() > 0)
                    {
                        CENTFilter1 = new CENTData();
                        CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);                    
                        CDALCore1.SetIdTransaction(TRN_LAYOUT);
                        CDALCore1.SetIdView(TRN_LAYOUT);
                        ListCENTCatalog3 = CDALCore1.GetList(CENTFilter1);

                        for (CENTData CENTData3 : ListCENTCatalog3)
                        {
                            intIdLayout = CENTData3.GetInt(SYSTEM_FIELD_ID);

                            ListCENTCatalog2 = this.GetCatalog(TRN_LAYOUT_SESSION_DEFINITION);
                            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                            CDALCore1.SetIdView(TRN_LAYOUT_SESSION_DEFINITION);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id_layout", ListCENTCatalog2), intIdLayout);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);                    

                            ListCENTCatalog2 = this.GetCatalog(TRN_LAYOUT_SESSION);
                            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                            CDALCore1.SetIdView(TRN_LAYOUT_SESSION);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id_layout", ListCENTCatalog2), intIdLayout);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);

                            CDALCore1.SetIdTransaction(TRN_LAYOUT);
                            CDALCore1.SetIdView(TRN_LAYOUT);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id", ListCENTCatalog2), intIdLayout);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);                        
                        }
                    }

                    /*
                     * Delete associated reconciliation, steps and rules
                     */                                
                    ListCENTCatalog2 = this.GetCatalog(TRN_RECONCILIATION);   
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CENTFilter1 = new CENTData();
                        CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);                    
                        CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
                        CDALCore1.SetIdView(TRN_RECONCILIATION);
                        ListCENTCatalog3 = CDALCore1.GetList(CENTFilter1);

                        for (CENTData CENTData3 : ListCENTCatalog3)
                        {                        
                            intIdReconcile = CENTData3.GetInt(SYSTEM_FIELD_ID);

                            ListCENTCatalog2 = this.GetCatalog(TRN_RECONCILIATION_STEP_RULE_FIELD);
                            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE_FIELD);
                            CDALCore1.SetIdView(TRN_RECONCILIATION_STEP_RULE_FIELD);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalog2), intIdReconcile);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);                        

                            ListCENTCatalog2 = this.GetCatalog(TRN_RECONCILIATION_STEP_RULE);
                            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP_RULE);
                            CDALCore1.SetIdView(TRN_RECONCILIATION_STEP_RULE);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalog2), intIdReconcile);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);

                            ListCENTCatalog2 = this.GetCatalog(TRN_RECONCILIATION_STEP);
                            CDALCore1.SetIdTransaction(TRN_RECONCILIATION_STEP);
                            CDALCore1.SetIdView(TRN_RECONCILIATION_STEP);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id_reconcile", ListCENTCatalog2), intIdReconcile);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);

                            CDALCore1.SetIdTransaction(TRN_RECONCILIATION);
                            CDALCore1.SetIdView(TRN_RECONCILIATION);
                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id", ListCENTCatalog2), intIdReconcile);
                            CDALCore1.Persist(CENTData1, EVENT_DELETE);
                        }    
                    }
                   
                    /*
                     * Delete permissions
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_PROFILE_TRANSACTION);
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                        CDALCore1.SetIdView(TRN_PROFILE_TRANSACTION);
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);                    
                    }
                    
                    /*
                     * Delete fields from transaction x function
                     */                    
                    ListCENTCatalog2 = this.GetCatalog(TRN_TRANSACTION_FUNCTION);
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                        CDALCore1.SetIdView(TRN_TRANSACTION_FUNCTION);
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);    
                    }

                    /*
                     * Delete associated view definition
                     */            
                    ListCENTCatalog2 = this.GetCatalog(TRN_VIEW_DEF);
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                        CDALCore1.SetIdView(TRN_VIEW_DEF);
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);                    
                    }
                    
                    /*
                     * Delete associated view
                     */            
                    ListCENTCatalog2 = this.GetCatalog(TRN_VIEW);
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CDALCore1.SetIdTransaction(TRN_VIEW);
                        CDALCore1.SetIdView(TRN_VIEW);
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);                    
                    }
                                        
                    /*
                     * Delete fields from catalog
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_CATALOG);
                    
                    if (ListCENTCatalog2.size() > 0)
                    {                    
                        CDALCore1.SetIdTransaction(TRN_CATALOG);
                        CDALCore1.SetIdView(TRN_CATALOG);
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);                    
                    }
                    
                    /*
                     * Delete the transaction
                     */                                
                    CDALCore1.SetIdTransaction(TRN_TRANSACTION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CENTData1.SetInt(SYSTEM_FIELD_ID, intIdTransaction);
                    CDALCore1.Persist(CENTData1, EVENT_DELETE);
                    
                    break;
            }
            
            return intIdTransaction;
            
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
            strNote = null;
            CDALCore1 = null;
            CBUSSetup1 = null;
            CENTFilter1 = null;
            ListCENTCatalog2 = null;
            ListCENTCatalog3 = null;
        }        
    }        
    
    private void Catalog(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent, boolean boolReflectView) throws CENTException, SQLException, Exception
    {        
        /*
         * General declaration
         */        
        int intIdFieldType = 0;
        int intIdFk = 0;
        int intIdField = 0;
        int intIdTransaction = 0;
        int intIdView = 0;
        int intPosition = 0;        
        
        CDALCore CDALCore1 = null;        
        CENTData CENTFilter1 = null;
        
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        List<CENTData> ListCENTCatalog3 = null;

        
        try
        {            
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());            
            
            /*
             * Cannot, never ever, choose foreign key with field type different from int
             */
            intIdFieldType = CENTData1.GetInt(this.GetFieldObject("id_type", this.GetCatalog(TRN_CATALOG)));
            intIdFk = CENTData1.GetInt(this.GetFieldObject("id_fk", this.GetCatalog(TRN_CATALOG)));
            
            if (intIdFk > 0)
            {
                if (intIdFieldType != TYPE_INT)
                {
                    throw new CENTException("EXCEPTION_FK_FIELD_MUST_BE_INT");
                }
            }
            
            
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT:
                    
                    /*
                     * For text datatype fields, size is mandatory
                     */
                    if (CENTData1.GetInt(FIELD_TYPE) == TYPE_TEXT)
                    {
                        if (CENTData1.GetInt(FIELD_SIZE) == 0 || CENTData1.GetInt(FIELD_SIZE) == Integer.MIN_VALUE)
                        {
                            throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Catalog.Size", "size");
                        }
                    }

                    /*
                     * If transaction is domain, must inform the domain name
                     */
                    if (CENTData1.GetInt(FIELD_ID_FK) == TRN_DOMAIN)
                    {
                        if (CENTData1.GetText(FIELD_DOMAIN_NAME).trim().equals(""))
                        {
                            throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Catalog.Domain", "domain_name");
                        }
                    }                        
                    
                    /*
                     * Avoid duplicate the field label
                     */                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(FIELD_ID_TRN, CENTData1.GetInt(FIELD_ID_TRN));
                    CENTFilter1.SetText(FIELD_LABEL, CENTData1.GetText(FIELD_LABEL));

                    this.SetIdTransaction(TRN_CATALOG);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData1 = this.GetList(CENTFilter1);
                    
                    if (!ListCENTData1.isEmpty())
                    {
                        throw new CENTException("EXCEPTION_FIELD_LABEL_ALREAD_MAPPED", "Catalog.Label", "label");
                    }
                    
                    /*
                     * Avoid duplicate the field name
                     */                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(FIELD_ID_TRN, CENTData1.GetInt(FIELD_ID_TRN));
                    CENTFilter1.SetText(FIELD_NAME, CENTData1.GetText(FIELD_NAME));

                    this.SetIdTransaction(TRN_CATALOG);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData1 = this.GetList(CENTFilter1);
                    
                    if (!ListCENTData1.isEmpty())
                    {
                        throw new CENTException("EXCEPTION_FIELD_NAME_ALREAD_MAPPED", "Catalog.Name", "name");
                    }
                    
                    /*
                     * Avoid duplicate the field object
                     */                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(FIELD_ID_TRN, CENTData1.GetInt(FIELD_ID_TRN));
                    CENTFilter1.SetText(FIELD_OBJECT, CENTData1.GetText(FIELD_OBJECT));

                    this.SetIdTransaction(TRN_CATALOG);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData1 = this.GetList(CENTFilter1);
                    
                    if (!ListCENTData1.isEmpty())
                    {
                        throw new CENTException("EXCEPTION_FIELD_OBJECT_ALREAD_MAPPED", "Catalog.Object", "object");
                    }

                    break;                    
                    
                case EVENT_UPDATE:

                    /*
                     * For text datatype fields, size is mandatory
                     */
                    if (CENTData1.GetInt(FIELD_TYPE) == TYPE_TEXT)
                    {
                        if (CENTData1.GetInt(FIELD_SIZE) == 0 || CENTData1.GetInt(FIELD_SIZE) == Integer.MIN_VALUE)
                        {
                            throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Catalog.Size", "size");
                        }
                    }

                    /*
                     * If transaction is domain, must inform the domain name
                     */
                    if (CENTData1.GetInt(FIELD_ID_FK) == TRN_DOMAIN)
                    {
                        if (CENTData1.GetText(FIELD_DOMAIN_NAME).trim().equals(""))
                        {
                            throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Catalog.Domain", "domain_name");
                        }
                    }
                
                    break;
                    
                case EVENT_DELETE: // Delete everything, with no exception

                    /*
                     * Query the field information
                     */                    
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, CENTData1.GetInt(SYSTEM_FIELD_ID));

                    this.SetIdTransaction(TRN_CATALOG);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData1 = this.GetList(CENTFilter1);
                    
                    for (CENTData CENTField1 : ListCENTData1)        
                    {                                                                        
                        if (this.isMappeableField(CENTField1.GetText(FIELD_OBJECT)) == false)
                        {
                            throw new CENTException("EXCEPTION_CANNOT_DELETE_SYSTEM_FIELD", CENTField1.GetText(FIELD_LABEL));
                        }                           
                    }
                    
                    break;
            }
     
            /*
             * Custom actions
             */
            switch (intIdEvent)
            {
                case EVENT_INSERT:
                    
                    /*
                     * Create the transaction
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());                    
                    CDALCore1.Persist(CENTData1, EVENT_INSERT);

                    /*
                     * Keep the key
                     */
                    intIdTransaction = CENTData1.GetInt(FIELD_ID_TRN);
                    intIdField = CENTData1.GetInt(SYSTEM_FIELD_ID);

                    /*
                     * When creating transactions, field ID must be 1. We have no catalog at this moment
                     */                    
                    try
                    {
                        intPosition = CENTData1.GetInt(GetFieldObject("position", ListCENTCatalog1));                                            
                    }
                    catch (Exception ex)
                    {
                        intPosition = 1;    
                    }

                    
                    /*
                     * Add the new field for each view related to the transaction
                     */
                    if (boolReflectView == true)
                    {                    
                        ListCENTCatalog2 = this.GetCatalog(TRN_VIEW);
                        CENTFilter1 = new CENTData();
                        CENTFilter1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog2), intIdTransaction);                    
                        CDALCore1.SetIdTransaction(TRN_VIEW);
                        CDALCore1.SetIdView(TRN_VIEW);
                        ListCENTCatalog2 = CDALCore1.GetList(CENTFilter1);

                        for (CENTData CENTData2 : ListCENTCatalog2)
                        {

                            intIdView = CENTData2.GetInt(SYSTEM_FIELD_ID);
                            ListCENTCatalog3 = this.GetCatalog(TRN_VIEW_DEF);

                            CENTData1 = new CENTData();
                            CENTData1.SetInt(GetFieldObject("id", ListCENTCatalog3), 0);
                            CENTData1.SetInt(GetFieldObject("id_view", ListCENTCatalog3), intIdView);
                            CENTData1.SetInt(GetFieldObject("id_command", ListCENTCatalog3), COMMAND_SELECT_FIELD);
                            CENTData1.SetInt(GetFieldObject("id_transaction", ListCENTCatalog3), intIdTransaction);
                            CENTData1.SetInt(GetFieldObject("id_field", ListCENTCatalog3), intIdField);
                            CENTData1.SetInt(GetFieldObject("id_operator", ListCENTCatalog3), 0);
                            CENTData1.SetText(GetFieldObject("value", ListCENTCatalog3), "");
                            CENTData1.SetInt(GetFieldObject("position", ListCENTCatalog3), intPosition);
                            CENTData1.SetText(GetFieldObject("note", ListCENTCatalog3), "");

                            CDALCore1.SetIdView(TRN_VIEW_DEF);
                            CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                            CDALCore1.Persist(CENTData1, EVENT_INSERT);
                        }
                    }
                    
                    break;
                    
                case EVENT_UPDATE:

                    /*
                     * Update the field from catalog
                     */
                    CDALCore1.SetIdView(TRN_CATALOG);
                    CDALCore1.SetIdTransaction(TRN_CATALOG);
                    CDALCore1.Persist(CENTData1, EVENT_UPDATE);

                    break;

                case EVENT_DELETE:

                    /*
                     * Primary key for this process
                     */                    
                    intIdField = CENTData1.GetInt(SYSTEM_FIELD_ID);
                    
                    /*
                     * Get related transaction
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdField);
                    CDALCore1.SetIdTransaction(TRN_CATALOG);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTCatalog2 = CDALCore1.GetList(CENTFilter1);

                    for (CENTData CENTData2 : ListCENTCatalog2)
                    {
                        intIdTransaction = CENTData2.GetInt(FIELD_ID_TRN);
                    }
                    
                    /*
                     * Delete the field from catalog
                     */
                    CDALCore1.SetIdView(TRN_CATALOG);
                    CDALCore1.SetIdTransaction(TRN_CATALOG);
                    CDALCore1.Persist(CENTData1, EVENT_DELETE);
                    
                    /*
                     * Delete the field from all views
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_VIEW_DEF);
                    CDALCore1.DeleteAll(TRN_VIEW_DEF, GetFieldObject("id_field", ListCENTCatalog2), intIdField);                    
                    
                    /*
                     * Delete the field from all reconciliations
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_RECONCILIATION_STEP_RULE_FIELD);
                    CDALCore1.DeleteAll(TRN_RECONCILIATION_STEP_RULE_FIELD, GetFieldObject("id_field", ListCENTCatalog2), intIdField);
                    
                    
                    break;
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
            CDALCore1 = null;        
            CENTFilter1 = null;

            ListCENTData1 = null;
            ListCENTCatalog2 = null;
            ListCENTCatalog3 = null;
        }        
    }    
    
    private void Company(CENTData CENTCompany1, int intIdEvent) throws CENTException, SQLException, Exception
    {        
        /*
         * General declaration
         */    
        int intId = 0;
        int intIdType = 0;
        int intIdCompany = 0;
        int intIdAreaOwner = 0;
        int intCompanyType = 0;
        
        String strName = "";
        CENTData CENTData2 = null;
        CENTData CENTData3 = null;
        CENTData CENTData5 = null;                
        
        List<CENTData> ListCENTCatalogCatalog1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTCatalog1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTCompany1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        CBUSSetup CBUSSetup1 = new CBUSSetup(this.GetConnection(), this.GetSession());
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
        
        try
        {
            switch (intIdEvent)
            {
                case EVENT_INSERT: // Create company and all structure
                    
                    /*
                     * Get next company id
                     */
                    ListCENTCompany1 = this.GetListOfCompany();
                    for (CENTData CENTData1 : ListCENTCompany1)
                    {
                        intIdCompany = CENTData1.GetInt(SYSTEM_FIELD_ID);
                    }
                    intIdCompany ++;
                    CENTCompany1.SetInt(SYSTEM_FIELD_ID, intIdCompany);        
                    
                    /*
                     * Get company typep
                     */                    
                    intCompanyType = CENTCompany1.GetInt(SYSTEM_FIELD_COMPANY_TYPE);
                    
                    /*
                     * Set into the session
                     */
                    this.GetSession().SetInt(SESSION_COMPANY, intIdCompany);                    
                    
                    /*
                     * Insert the catalog as the other transactions depends on it
                     */
                    ListCENTCatalog1 = CBUSSetup1.GetCatalogCatalog();
                    ListCENTCatalogCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_ALL);
                            
                    CDALCore1.SetIdTransaction(TRN_CATALOG);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    for (CENTData CENTCatalog1 : ListCENTCatalogCatalog1)
                    {
                        CDALCore1.Persist(CENTCatalog1, intIdEvent, ListCENTCatalog1);                        
                    }

                    /*
                     * Insert the company (1 is system setup)
                     */                                        
                    ListCENTCompany1 = CBUSSetup1.GetListOfCatalog(TRN_COMPANY);
                    
                    /*
                     * Check duplication by document
                     */                    
                    this.Validate(ListCENTCompany1, CENTCompany1, intIdEvent);
                    
                    /*
                     * Persist it
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_COMPANY);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTCompany1, intIdEvent, ListCENTCompany1);
                    
                    /*
                     * Insert the tables
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_TABLE);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_TABLE);
                    ListCENTData1 = CBUSSetup1.GetListOfTable();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }                    

                    /*
                     * Insert the menus
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_MENU);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_MENU);                    
                    ListCENTData1 = CBUSSetup1.GetListOfMenu(intCompanyType);
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }

                    /*
                     * Insert the transaction
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_TRANSACTION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_TRANSACTION);
                    ListCENTData1 = CBUSSetup1.GetListOfTransactions();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }

                    /*
                     * Insert the languages
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_LANGUAGE);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_LANGUAGE);
                    ListCENTData1 = CBUSSetup1.GetListOfLanguages();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }

                    /*
                     * Insert the countries
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_COUNTRY);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_COUNTRY);
                    ListCENTData1 = CBUSSetup1.GetListOfCountry();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                    
                    /*
                     * Insert the dictionary
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_DICTIONARY);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_DICTIONARY);
                    ListCENTData1 = CBUSSetup1.GetListOfDictionaries();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                    
                    /*
                     * Insert the field event
                     */                               
                    CDALCore1.SetIdTransaction(TRN_FIELD_EVENT);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_FIELD_EVENT);
                    ListCENTData1 = CBUSSetup1.GetListOfFieldEvents();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                                        
                    /*
                     * Insert the domains
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_DOMAIN);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_DOMAIN);
                    ListCENTData1 = CBUSSetup1.GetListOfDomains();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                                                            
                    /*
                     * Insert the functions
                     */                                        
                    CDALCore1.SetIdTransaction(TRN_FUNCTION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_FUNCTION);
                    ListCENTData1 = CBUSSetup1.GetListOfFunctions();
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                    
                    /*
                     * Create the views
                     */                                      
                    CDALCore1.SetIdTransaction(TRN_VIEW);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_VIEW);
                    ListCENTData1 = CBUSSetup1.GetListOfView(CBUSSetup1.GetListOfCatalog(TRN_TRANSACTION), CBUSSetup1.GetListOfTransactions());
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }
                                        
                    /*
                     * Create the views def
                     */                    
                    CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);                                        
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_VIEW_DEF);
                    ListCENTData1 = CBUSSetup1.GetListOfViewDef(CBUSSetup1.GetListOfCatalog(TRN_TRANSACTION), CBUSSetup1.GetListOfTransactions(), CBUSSetup1.GetListOfCatalog(TRN_ALL));
                    
                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        CDALCore1.Persist(CENTData1, intIdEvent, ListCENTCatalog1);                        
                    }     
                    
                    /*
                     * Specific transactions for reconciliation type
                     */
                    ListCENTData1 = new ArrayList<CENTData>();
                    ListCENTCatalog1 = CBUSSetup1.GetListOfCatalog(TRN_TRANSACTION);                            

                    /*
                     * Create match view (totais)
                     */
                    CENTCompany1 = new CENTData();                 
                    CENTCompany1 = CBUSSetup1.GetView(0, "Totais", TRN_MATCH, "", DISPLAY_GRID, "");                    
                    CDALCore1.SetIdTransaction(TRN_VIEW);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                    intId = CENTCompany1.GetInt(SYSTEM_FIELD_ID);

                    CENTCompany1 = new CENTData();                    
                    ListCENTCatalog1 = this.GetCatalog(TRN_MATCH);
                    CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);          

                    CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("date", ListCENTCatalog1), 0, "", 1, ""); 
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);                    
                    CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("id_process", ListCENTCatalog1), 0, "", 2, ""); 
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                    CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("id_reconcile", ListCENTCatalog1), 0, "", 3, ""); 
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                    CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("id_status", ListCENTCatalog1), 0, "", 4, ""); 
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                    CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_COUNT, TRN_MATCH, this.GetFieldId("id_status", ListCENTCatalog1), 0, "", 5, ""); 
                    CDALCore1.Persist(CENTCompany1, EVENT_INSERT);               

                    /*
                     * Create match view (Orfão/Conciliação)
                     */
                    for (int i=3; i>0; i--)
                    {
                        switch (i)
                        {
                            case 1:
                                strName = "Conciliação/Orfão";
                                intIdType = DISPLAY_CHART_COLUMNS;
                                break;

                            case 2:
                                strName = "Conciliação/Divergentes";
                                intIdType = DISPLAY_CHART_COLUMNS;                        
                                break;

                            case 3:
                                strName = "Conciliação/Batidos";
                                intIdType = DISPLAY_CHART_COLUMNS;                        
                                break;                        
                        }

                        CENTCompany1 = new CENTData();                 
                        CENTCompany1 = CBUSSetup1.GetView(0, strName, TRN_MATCH, "", intIdType, "");                    
                        CDALCore1.SetIdTransaction(TRN_VIEW);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                        intId = CENTCompany1.GetInt(SYSTEM_FIELD_ID);

                        CENTCompany1 = new CENTData();                    
                        ListCENTCatalog1 = this.GetCatalog(TRN_MATCH);
                        CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);                    

                        CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("id_reconcile", ListCENTCatalog1), 0, "", 1, ""); 
                        CDALCore1.Persist(CENTCompany1, EVENT_INSERT);                
                        CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_FIELD, TRN_MATCH, this.GetFieldId("id_status", ListCENTCatalog1), 0, "", 2, ""); 
                        CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                        CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_SELECT_COUNT, TRN_MATCH, this.GetFieldId("id_status", ListCENTCatalog1), 0, "", 3, ""); 
                        CDALCore1.Persist(CENTCompany1, EVENT_INSERT);
                        CENTCompany1 = CBUSSetup1.GetViewDef(0, intId, COMMAND_CONDITION_AND, TRN_MATCH, this.GetFieldId("id_status", ListCENTCatalog1), OPERATOR_EQUALS, String.valueOf(i), 4, ""); 
                        CDALCore1.Persist(CENTCompany1, EVENT_INSERT);                    
                    }
                                        
                    /*
                     * Insert IT/Admin area
                     */
                    ListCENTData1 = new ArrayList<CENTData>();
                    CENTData2 = CBUSSetup1.GetArea(AREA_IT, intIdCompany, "IT", "admin@recon1.com", "");
                    ListCENTData1.add(CENTData2);
                    this.SetIdTransaction(TRN_AREA);
                    this.SetIdView(DO_NOT_USE_VIEW);                                        
                    this.ExecuteEvent(EVENT_INSERT, ListCENTData1);
                    
                    /*
                     * Setup contents to area IT
                     */                    
                    CENTData3 = CBUSSetup1.GetProfile(PROFILE_ADMIN, "owner", "owner@recon1.com.br", "");                        
                    CDALCore1.SetIdTransaction(TRN_PROFILE);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTData3, intIdEvent);

                    ListCENTData1 = CBUSSetup1.GetListOfProfileTransaction(PROFILE_OWNER);

                    for (CENTData CENTData4 : ListCENTData1)
                    {
                        CDALCore1.SetIdTransaction(TRN_PROFILE_TRANSACTION);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTData4, intIdEvent);
                    }

                    ListCENTData1 = CBUSSetup1.GetListOfTransactionFunction(PROFILE_OWNER);

                    for (CENTData CENTData4 : ListCENTData1)
                    {
                        CDALCore1.SetIdTransaction(TRN_TRANSACTION_FUNCTION);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTData4, intIdEvent);
                    }

                    CENTData3 = CBUSSetup1.GetUser(0, "owner", "owner", PROFILE_OWNER, AREA_IT, "owner@recon1.com.br", "");                        
                    CDALCore1.SetIdTransaction(TRN_USER);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTData3, intIdEvent);
                    
                    /*
                     * Insert FX Area
                     */
                    ListCENTData1 = new ArrayList<CENTData>();                        
                    CENTData2 = CBUSSetup1.GetArea(AREA_FX, ADMINISTRATOR_COMPANY, "AREA_FX", "fx@fx.com.br", "");
                    ListCENTData1.add(CENTData2);
                    this.SetIdTransaction(TRN_AREA);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    this.ExecuteEvent(EVENT_INSERT, ListCENTData1); 

                    /*
                     * Create a demo instance to show ENTRY MANAGER functionalities
                     */
                    intIdAreaOwner = this.GetSession().GetInt(SESSION_AREA);
                    this.GetSession().SetInt(SESSION_AREA, AREA_FX);                                      
                   
                    /*
                     * All the structure is here
                     */
                    CBUSSetup1.CreateDemoAccessControl(AREA_FX, intIdEvent);
                    
                    /*
                     * Transaction related to entry manager instance only
                     */
                    if (intCompanyType == COMPANY_TYPE_ENTRY_MANAGER)
                    {
                        CBUSSetup1.CreateDemoEntryManager(AREA_FX, intIdEvent);       
                    }

                    /*
                     * Transaction related to reconciliation soluion instance only
                     */
                    if (intCompanyType == COMPANY_TYPE_RECONCILIATION)
                    {
                        CBUSSetup1.CreateDemoReconciliationSolution(AREA_FX, intIdEvent);
                    }

                    this.GetSession().SetInt(SESSION_AREA, intIdAreaOwner);                        

                    
                    
                    /*
                     * Back to main company 
                     */
                    this.GetSession().SetInt(SESSION_COMPANY, 1);

                    break;

                case EVENT_UPDATE:

                    if (CENTCompany1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CDALCore1.Persist(CENTCompany1, intIdEvent);                    
                    
                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    /*
                     * !! Critical !! will delete all information related to selected company
                     */                    
                    if (CENTCompany1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_CANNOT_DELETE_COMPANY");
                    }

                    for (int i=1; i<=TRN_COUNT; i++)
                    {
                        CDALCore1.DeleteAll(i, "", 0);
                    }

                    break;
            }
            
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
            strName = "";

            ListCENTCatalogCatalog1 = null;
            ListCENTCatalog1 = null;
            ListCENTCompany1 = null;
            ListCENTData1 = null;

            CBUSSetup1 = null;
            CDALCore1 = null;
        }        
    }
    
    /*
     * Layout
     */
    private void Layout(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */      
        CDALCore CDALCore1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                case EVENT_UPDATE:
                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    if (CENTData1.GetInt(GetFieldObject("id", ListCENTCatalog1)) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }
                    break;                            
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
            }

            
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
            CDALCore1 = null;
        }        
    }

    private void LayoutSession(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdLayout = 0;
        int intIdSession = 0;
        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
            }
            
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }        
    
    private void LayoutSessionDefinition(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intPosition = 0;        
        int intSize = 0;
        int intIdDef = 0;                
        int intIdSession = 0;        
        int intIdLayout = 0;
        
        CBUSCore CBUSCore1 = null;
        CDALCore CDALCore1 = null;
        
        List<CENTData> ListCENTData2 = null;
        List<CENTData> ListCENTData3 = null;
        
        List<CENTData> ListCENTCatalogSession1 = null;
        List<CENTData> ListCENTCatalogDefinition1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CENTData CENTFilter1 = new CENTData();
            CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            ListCENTCatalogSession1 = this.GetCatalog(TRN_LAYOUT_SESSION);
            ListCENTCatalogDefinition1 = this.GetCatalog(TRN_LAYOUT_SESSION_DEFINITION);            
            
            /*
             * Validate according to the action
             */            
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                case EVENT_UPDATE:

                    /*
                     * Keep position and size to be validated
                     */
                    intPosition = CENTData1.GetInt(GetFieldObject("position", ListCENTCatalog1));
                    intSize = CENTData1.GetInt(GetFieldObject("size", ListCENTCatalog1));

                    /*
                     * For date fields, the mask is mandatory
                     */                         
                    CENTFilter1 = new CENTData();                            
                    CENTFilter1.SetInt(FIELD_ID_TRN, CENTData1.GetInt(GetFieldObject("id_transaction", ListCENTCatalog1)));
                    CENTFilter1.SetInt(FIELD_ID, CENTData1.GetInt(GetFieldObject("id_field", ListCENTCatalog1)));
                    CBUSCore1.SetIdTransaction(TRN_CATALOG);
                    CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData2 = CBUSCore1.GetList(CENTFilter1);

                    if (ListCENTData2.isEmpty())
                    {
                        throw new CENTException("EXCEPTION_MAPPED_FIELD_NOT_FOUND_AT_RECONCILIATION_AREA");
                    }    

                    if (ListCENTData2.get(0).GetInt(FIELD_TYPE) == TYPE_DATE || ListCENTData2.get(0).GetInt(FIELD_TYPE) == TYPE_DOUBLE)
                    {
                        if (CENTData1.GetText(GetFieldObject("mask", ListCENTCatalog1)).trim().equals(""))
                        {
                            throw new CENTException("EXCEPTION_MANDATORY_FIELD", "LayoutDef.Mask", "mask");
                        }
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    if (CENTData1.GetInt(GetFieldObject("id", ListCENTCatalog1)) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }
                    
                    break;                            
            }
            
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:

                    /*
                     * Keep the key
                     */                    
                    intIdDef = CENTData1.GetInt(SYSTEM_FIELD_ID);

                    /*
                     * Query the record to get the keys
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdDef);
                    CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData2 = CDALCore1.GetList(CENTFilter1);
                    
                    for (CENTData CENTData2 : ListCENTData2)
                    {                                        
                        intIdLayout = CENTData2.GetInt(GetFieldObject("id_layout", ListCENTCatalogDefinition1));
                        intIdSession = CENTData2.GetInt(GetFieldObject("id_session", ListCENTCatalogDefinition1));                    
                    }

                    /*
                     * Delete the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);                    
                                                
                    /*
                     * Reorder layouts based on positioning
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(GetFieldObject("id", ListCENTCatalogSession1), intIdSession);
                    CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
                    CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    ListCENTData2 = CDALCore1.GetList(CENTFilter1);
                    
                    for (CENTData CENTData2 : ListCENTData2)
                    {
                        if (CENTData2.GetInt(GetFieldObject("id_format", ListCENTCatalogSession1)) != LAYOUT_FORMAT_FIXED_LENGTH)
                        {
                            /*
                             * Query the session def and recalculate the position
                             */                    
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetInt(GetFieldObject("id_session", ListCENTCatalogDefinition1), intIdSession);
                            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                            ListCENTData3 = CDALCore1.GetList(CENTFilter1);
                            
                            for (CENTData CENTData3 : ListCENTData3)
                            {
                                /*
                                 * Update the positions
                                 */                                
                                intPosition ++;
                                CENTData3.SetInt(GetFieldObject("position", ListCENTCatalogDefinition1), intPosition);
                                
                                /*
                                 * Persist the changes
                                 */
                                CDALCore1.SetIdTransaction(this.GetIdTransaction());
                                CDALCore1.SetIdView(this.GetIdView());
                                CDALCore1.Persist(CENTData3, EVENT_UPDATE);                                
                            }


                        }    
                    }
                
                    break;
            }            
            
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
            CBUSCore1 = null;
            CDALCore1 = null;

            ListCENTData2 = null;
            ListCENTData3 = null;

            ListCENTCatalogSession1 = null;
            ListCENTCatalogDefinition1 = null;
        }        
    }

    /*
     * Queue
     */    
    private void Queue(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdQueue = 0;
        int intIdService = 0;
        int intIdType = 0;
        int intIdProcess = 0;
        int intIdTransaction = 0;
        
        CENTData CENTFilter1 = null;
        CENTData CENTFilter2 = null;
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTJob1 = null;
        List<CENTData> ListCENTQueue1 = null;
        List<CENTData> ListCENTLayout1 = null;
        List<CENTData> ListCENTCatalogLayout1 = null;
        List<CENTData> ListCENTCatalogScheduleJob1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());            
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_DELETE:
                    
                    
                    ListCENTCatalogLayout1 = this.GetCatalog(TRN_LAYOUT);
                    
                    /*
                     * Delete layout definition
                     */
                    intIdQueue = CENTData1.GetInt(SYSTEM_FIELD_ID);
                    intIdService = CENTData1.GetInt(SYSTEM_FIELD_ID);

                    /*
                     * Query the process details regarding current queue
                     */                      
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdService);                    
                    CDALCore1.SetIdTransaction(TRN_QUEUE);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    
                    ListCENTQueue1 = CDALCore1.GetList(CENTFilter1);
                    
                    for (CENTData CENTQueue1 : ListCENTQueue1)
                    {
                        intIdQueue = CENTQueue1.GetInt(GetFieldObject("id", ListCENTCatalog1));
                        intIdType = CENTQueue1.GetInt(GetFieldObject("id_type", ListCENTCatalog1));
                        intIdService = CENTQueue1.GetInt(GetFieldObject("id_service", ListCENTCatalog1));
                        
                        switch (intIdType)
                        {
                            case QUEUE_TYPE_IMPORT_FILE:
                                
                                /*
                                 * The process is the queue id
                                 */                                
                                intIdProcess = intIdQueue;

                                /*
                                 * Query associated transaction
                                 */
                                CENTFilter1 = new CENTData();
                                CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdService);                    
                                CDALCore1.SetIdTransaction(TRN_LAYOUT);
                                CDALCore1.SetIdView(DO_NOT_USE_VIEW);

                                ListCENTLayout1 = CDALCore1.GetList(CENTFilter1);
                                
                                for (CENTData CENTLayout1 : ListCENTLayout1)
                                {                                
                                    intIdTransaction = CENTLayout1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
                                }
                                
                                if (intIdTransaction != 0)
                                {
                                    CDALCore1.UndoDataImport(intIdProcess, intIdTransaction);
                                }
                                
                                break;
                                
                            case QUEUE_TYPE_RECONCILIATION:
                                
                                /*
                                 * The process is the queue id
                                 */                                
                                intIdProcess = intIdQueue;
                                
                                /*
                                 * Query associated transaction
                                 */                                                      
                                CDALCore1.UndoRecon(intIdQueue);
                                                                
                                break;
                                
                            case QUEUE_TYPE_SCHEDULE:

                                /*
                                 * Get current catalog
                                 */
                                ListCENTCatalogScheduleJob1 = this.GetCatalog(TRN_SCHEDULE_JOB);
                                
                                /*
                                 * Query associated jobs
                                 */                      
                                CENTFilter1 = new CENTData();
                                CENTFilter1.SetInt(this.GetFieldObject("id_scheduler", this.GetCatalog(TRN_SCHEDULE_JOB)), intIdService);                    
                                CDALCore1.SetIdTransaction(TRN_SCHEDULE_JOB);
                                CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                                ListCENTJob1 = CDALCore1.GetList(CENTFilter1);
                                
                                for (CENTData CENTJob1 : ListCENTJob1)
                                {
                                    intIdType = CENTJob1.GetInt(GetFieldObject("id_type", ListCENTCatalogScheduleJob1));
                                    intIdService = CENTJob1.GetInt(GetFieldObject("id_service", ListCENTCatalogScheduleJob1));
                                    
                                    if (intIdType == QUEUE_TYPE_IMPORT_FILE)
                                    {                                        
                                        /*
                                         * Query the transaction to figure out the data table
                                         */
                                        CENTFilter2 = new CENTData();
                                        CENTFilter2.SetInt(SYSTEM_FIELD_ID, intIdService);
                                        CDALCore1.SetIdTransaction(TRN_LAYOUT);
                                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);

                                        ListCENTLayout1 = CDALCore1.GetList(CENTFilter2);

                                        for (CENTData CENTLayout1 : ListCENTLayout1)
                                        {                                
                                            intIdTransaction = CENTLayout1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
                                        }                                        

                                        /*
                                         * Generate the process id
                                         */
                                        intIdProcess = Integer.valueOf(String.valueOf(intIdQueue) + String.valueOf(intIdService));                                        

                                        /*
                                         * Undo the import process
                                         */                                        
                                        CDALCore1.UndoDataImport(intIdProcess, intIdTransaction);                                        
                                    }
                                    
                                    if (intIdType == QUEUE_TYPE_RECONCILIATION)
                                    {
                                        /*
                                         * Generate the process id
                                         */
                                        intIdService = Integer.valueOf(String.valueOf(intIdQueue) + String.valueOf(intIdService));
                                        
                                        /*
                                         * Undo it
                                         */                                        
                                        CDALCore1.UndoRecon(intIdService);                                        
                                    }
                                }
                                
                                break;
                        }
                        
                        /*
                         * Delete selected queue/process
                         */                      
                        CENTData1 = new CENTData();
                        CENTData1.SetInt(SYSTEM_FIELD_ID, intIdQueue);                    
                        CDALCore1.SetIdTransaction(TRN_QUEUE);
                        CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                        CDALCore1.Persist(CENTData1, EVENT_DELETE);
                        
                    }
            }            
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
            CENTFilter1 = null;
            CENTFilter2 = null;
            CDALCore1 = null;
            ListCENTJob1 = null;
            ListCENTQueue1 = null;
            ListCENTLayout1 = null;
            ListCENTCatalogLayout1 = null;
            ListCENTCatalogScheduleJob1 = null;
        }        
    }    
    
    /*
     * Reconcile
     */    
    private void Reconcile(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdReconcile = 0;
        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
            }
            
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }
    
    private void ReconcileStep(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdStep = 0;
        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
            }
            
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }
    
    private void ReconcileStepRule(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdRule = 0;
        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
            }
                        
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }    
    
    private void ReconcileStepRuleField(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {
        /*
         * General declaration
         */
        int intIdField = 0;
        int intIdFieldType = 0;
                        
        String strFieldObject = "";
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */        
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());            
            
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:

                    /*
                     * Keep the full catalog in memory
                     */
                    ListCENTCatalog2 = this.GetCatalog(TRN_ALL);                            

                    /*
                     * Tolerance not allowed when the operator is different from equals
                     */                                                       
                    if (CENTData1.GetInt(this.GetFieldObject("id_operator", ListCENTCatalog1)) != OPERATOR_EQUALS)
                    {
                        strFieldObject = this.GetFieldObject("tolerance_value", ListCENTCatalog1);                                    
                        if (
                                CENTData1.GetDouble(strFieldObject) != Double.MIN_VALUE && 
                                CENTData1.GetDouble(strFieldObject) != 0
                            )
                        {
                            throw new CENTException("EXCEPTION_OPERATOR_TOLERANCE_EQUALS_ONLY", "ReconcileStepRuleField.Tol");
                        }

                        strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                        if (
                                CENTData1.GetInt(strFieldObject) != Integer.MIN_VALUE && 
                                CENTData1.GetInt(strFieldObject) != 0
                            )
                        {
                            throw new CENTException("EXCEPTION_OPERATOR_TOLERANCE_EQUALS_ONLY", "ReconcileStepRuleField.TolType");
                        }                            
                    }


                    /*
                     * If define a tolerance, tolerance type if mandatory
                     */                            
                    strFieldObject = this.GetFieldObject("tolerance_value", ListCENTCatalog1);                            
                    if (CENTData1.GetDouble(strFieldObject) != Double.MIN_VALUE && CENTData1.GetDouble(strFieldObject) != 0)
                    {
                        strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                        if (CENTData1.GetInt(strFieldObject) == Integer.MIN_VALUE && CENTData1.GetInt(strFieldObject) != 0)
                        {                                
                            throw new CENTException("EXCEPTION_TOLERANCE_TYPE_MANDATORY", "ReconcileStepRuleField.TolType");
                        }
                    }

                    /*
                     * Get the field selected to compose the rule
                     */
                    strFieldObject = this.GetFieldObject("id_field", ListCENTCatalog1);                            
                    intIdField = CENTData1.GetInt(strFieldObject);
                    intIdFieldType = this.GetFieldType(intIdField, ListCENTCatalog2);

                    switch (intIdFieldType)
                    {                                
                        case TYPE_INT:

                            /*
                             * Validate the tolerance type for integer fields
                             */
                            strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                            if (CENTData1.GetInt(strFieldObject) == TOLERANCE_TYPE_DATE)
                            {
                                throw new CENTException("EXCEPTION_INVALID_TOLERANCE_TYPE_INT", "ReconcileStepRuleField.TolType");                                                                 
                            }

                            break;

                        case TYPE_TEXT:

                            strFieldObject = this.GetFieldObject("tolerance_value", ListCENTCatalog1);                                    
                            if (
                                    CENTData1.GetDouble(strFieldObject) != Double.MIN_VALUE && 
                                    CENTData1.GetDouble(strFieldObject) != 0
                                )
                            {
                                throw new CENTException("EXCEPTION_INVALID_TOLERANCE_TYPE_TEXT", "ReconcileStepRuleField.Tol");
                            }

                            strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                            if (
                                    CENTData1.GetInt(strFieldObject) != Integer.MIN_VALUE && 
                                    CENTData1.GetInt(strFieldObject) != 0
                                )
                            {
                                throw new CENTException("EXCEPTION_INVALID_TOLERANCE_TYPE_TEXT", "ReconcileStepRuleField.TolType");
                            }                                    

                            /*
                             * Validate invalid operators for text fields
                             */                                    
                            strFieldObject = this.GetFieldObject("id_operator", ListCENTCatalog1);
                            if (
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_GREATER || 
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_GREATER_EQUALS || 
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_SMALLER || 
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_SMALLER_EQUALS)
                            {
                                throw new CENTException("EXCEPTION_INVALID_OPERATOR_TEXT_FIELD", "ReconcileStepRuleField.Operator");
                            }

                            break;    

                        case TYPE_DATE:

                            /*
                             * Validate the tolerance type for date fields
                             */                                    
                            strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                            if (
                                    CENTData1.GetInt(strFieldObject) != TOLERANCE_TYPE_DATE && 
                                    CENTData1.GetInt(strFieldObject) != 0  && 
                                    CENTData1.GetInt(strFieldObject) != Integer.MIN_VALUE)
                            {
                                throw new CENTException("EXCEPTION_INVALID_TOLERANCE_TYPE_DATE", "ReconcileStepRuleField.TolType");
                            }

                            /*
                             * Validate invalid operators for date fields
                             */                                    
                            strFieldObject = this.GetFieldObject("id_operator", ListCENTCatalog1);
                            if (    
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_GREATER_EQUALS ||
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_SMALLER_EQUALS ||
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_IN ||                                            
                                    CENTData1.GetInt(strFieldObject) == OPERATOR_NOT_IN
                                )
                            {
                                throw new CENTException("EXCEPTION_INVALID_OPERATOR_DATE_FIELD", "ReconcileStepRuleField.Operator");
                            }                                    

                            break;                                        

                        case TYPE_DOUBLE:

                            /*
                             * Validate the tolerance type for double fields
                             */                                    
                            strFieldObject = this.GetFieldObject("id_tolerance_type", ListCENTCatalog1);
                            if (CENTData1.GetInt(strFieldObject) == TOLERANCE_TYPE_DATE)
                            {
                                throw new CENTException("EXCEPTION_INVALID_TOLERANCE_TYPE_DOUBLE", "ReconcileStepRuleField.TolType");
                            }

                            break;                                    
                    }

                    break;

                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
                        
            /*
             * Persist the record (insert or update)
             */
            CDALCore1.SetIdTransaction(this.GetIdTransaction());
            CDALCore1.SetIdView(this.GetIdView());
            CDALCore1.Persist(CENTData1, intIdEvent);
            
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
            strFieldObject = "";
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }

    private void FileManager(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        File File1 = null;
        String strFileName = "";        
        CDALCore CDALCore1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    
                    break;
                    
                case EVENT_DELETE:
                    
                    /*
                     * Keep the catalog
                     */
                    ListCENTCatalog1 = this.GetCatalog(TRN_FILE_MANAGER);                    
                    
                    /*
                     * Query file name
                     */
                    this.SetIdTransaction(TRN_FILE_MANAGER);
                    this.SetIdView(DO_NOT_USE_VIEW);
                    CENTData1 = this.GetList(CENTData1).get(0);

                    /*
                     * Delete the file
                     */                    
                    strFileName = this.GetPropertieValue("FILE_MANAGER") + this.GetSession().GetInt(SESSION_COMPANY) + "\\" + CENTData1.GetText(GetFieldObject("file", ListCENTCatalog1));
                    File1 = new File(strFileName);
                    File1.setWritable(true);
                    File1.delete();
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    

            }
            
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
            File1 = null;
            strFileName = null;
            CDALCore1 = null;
        }        
    }    
    
    private void Scheduler(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdScheduler = 0;        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Delete layout definition
                     */
                    intIdScheduler = CENTData1.GetInt(SYSTEM_FIELD_ID);

                    ListCENTCatalog2 = this.GetCatalog(TRN_SCHEDULE_JOB);
                    CDALCore1.DeleteAll(TRN_SCHEDULE_JOB, GetFieldObject("id_scheduler", ListCENTCatalog2), intIdScheduler);
                    
                    ListCENTCatalog2 = this.GetCatalog(TRN_SCHEDULE);
                    CDALCore1.DeleteAll(TRN_SCHEDULE, GetFieldObject("id", ListCENTCatalog2), intIdScheduler);                    
            }
                        
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }        
    
    
    
    private void View(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */
        int intIdView = 0;
        
        CDALCore CDALCore1 = null;
        List<CENTData> ListCENTCatalog2 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                    
                    break;
                    
                case EVENT_UPDATE:

                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_UPDATE_MISSING_ID");
                    }

                    break;

                case EVENT_DELETE: // Delete everything, with no exception
                    
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }

                    break;
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                         
                    /*
                     * Persist the record (Insert or Update)
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    
                    break;
                    
                case EVENT_UPDATE:
            
                    /*
                     * Persist the record (Insert or Update)
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
            
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Delete layout definition
                     */
                    intIdView = CENTData1.GetInt(SYSTEM_FIELD_ID);

                    ListCENTCatalog2 = this.GetCatalog(TRN_VIEW_DEF);
                    CDALCore1.SetIdTransaction(TRN_VIEW_DEF);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CENTData1 = new CENTData();
                    CENTData1.SetInt(GetFieldObject("id_view", ListCENTCatalog2), intIdView);
                    CDALCore1.Persist(CENTData1, EVENT_DELETE);
                    
                    ListCENTCatalog2 = this.GetCatalog(TRN_VIEW);
                    CDALCore1.SetIdTransaction(TRN_VIEW);
                    CDALCore1.SetIdView(DO_NOT_USE_VIEW);
                    CENTData1 = new CENTData();
                    CENTData1.SetInt(GetFieldObject("id", ListCENTCatalog2), intIdView);
                    CDALCore1.Persist(CENTData1, EVENT_DELETE);
                    
                    break;
            }
            
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
            CDALCore1 = null;
            ListCENTCatalog2 = null;
        }        
    }    

    /*
     * Cannot delete record with 
     */
    private void ValidateFK(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdAction) throws CENTException, Exception
    {
        /*
         * General declaration
         */
        int intIdTransaction = 0;
        int intIdCurrentTransaction = 0;        
        String strFieldName = "";
        String strTransactionName = "";
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTData2 = null;
        List<CENTData> ListCENTData3 = null;        
        
        try
        {
            /*
             * Keep current transaction
             */            
            intIdCurrentTransaction = this.GetIdTransaction();
            
            /*
             * Query catalog for transactions
             */
            ListCENTData3 = GetCatalog(TRN_TRANSACTION);
                    
                    
            /*
             * Query dependency transactions
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(FIELD_ID_FK, this.GetIdTransaction());
            this.SetIdTransaction(TRN_CATALOG);
            this.SetIdView(DO_NOT_USE_VIEW);                                
            ListCENTData1 = this.GetList(CENTFilter1);            
            
            for (CENTData CENTData2 : ListCENTData1)
            {
                /*
                 * Keep transaction information
                 */                
                intIdTransaction = CENTData2.GetInt(FIELD_ID_TRN);
                strFieldName = CENTData2.GetText(FIELD_OBJECT);

                /*
                 * Keep transaction name
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(SYSTEM_FIELD_ID, intIdTransaction);
                this.SetIdTransaction(TRN_TRANSACTION);
                this.SetIdView(DO_NOT_USE_VIEW);
                ListCENTData2 = this.GetList(CENTFilter1);
                
                if (ListCENTData2.size() > 0)
                {
                    strTransactionName = Translate(ListCENTData2.get(0).GetText(this.GetFieldObject("name", ListCENTData3)));
                }
                
                /*
                 * Query data for dependency transactions
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(strFieldName, CENTData1.GetInt(SYSTEM_FIELD_ID));            
                this.SetIdTransaction(intIdTransaction);
                this.SetIdView(DO_NOT_USE_VIEW);
                ListCENTData2 = this.GetList(CENTFilter1);
                
                if (ListCENTData2.size() > 0)
                {
                    throw new CENTException(Translate("EXCEPTION_FK_DEPENDENCIES"), strTransactionName);
                }
            }
            
            /*
             * Set current transaction
             */            
            this.SetIdTransaction(intIdCurrentTransaction);            

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
            strFieldName = null;
            strTransactionName = null;
            CENTFilter1 = null;
            ListCENTData1 = null;
            ListCENTData2 = null;
        }

    }
        
    /*
     * Validate entity dinamically 
     */
    private void Validate(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdAction) throws CENTException, Exception
    {
        /*
         * General Declaration
         */
        int intId = 0;
        int intFieldType = 0;
        int intFieldSize = 0;
        int intFieldNullable = 0;
        int intUnique = 0;
        int intValue = 0;

        double dblValue = 0;        
        
        String strFieldLabel = "";
        String strFieldName = "";        
        String strFieldObject = "";        
        String strFieldValue = "";
        
        Date datValue = null;
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTData1 = null;

        try
        {           
            /*
             * Validate the system fields
             */
            if (this.GetSession().GetInt(SESSION_COMPANY) == 0)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.Company");
            }

            if (this.GetIdTransaction() == 0)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.Transaction");
            }                

            if (this.GetSession().GetInt(SESSION_USER) == 0)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.User");
            }                              

            if (this.GetSession().GetDate(SESSION_DATE) == null)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.Date");
            }

            if (this.GetSession().GetText(SESSION_COUNTRY).equals(""))
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.Country");
            }         
            
            if (this.GetSession().GetText(SESSION_LANGUAGE).equals(""))
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Session.Language");
            }
            
            if (CENTData1 == null)
            {
                throw new CENTException("EXCEPTION_MANDATORY_FIELD", "Entity");
            }            
                        
            /*
             * Create the Objects
             */
            this.SetIdView(DO_NOT_USE_VIEW);
            this.SetIdTransaction(this.GetIdTransaction());
                                
            /*
             * Read the system catalog and validate information like data type, size, etc
             */
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {                       
                strFieldLabel = CENTCatalog1.GetText(FIELD_LABEL);
                strFieldName = CENTCatalog1.GetText(FIELD_NAME);
                strFieldObject = CENTCatalog1.GetText(FIELD_OBJECT);
                intFieldType = CENTCatalog1.GetInt(FIELD_TYPE);
                intFieldSize = CENTCatalog1.GetInt(FIELD_SIZE);
                intFieldNullable = CENTCatalog1.GetBoolean(FIELD_NULLABLE);    
                intUnique = CENTCatalog1.GetBoolean(FIELD_ID_UNIQUE);
                
                
                /*
                 * Keep ID from current item
                 */
                intId = CENTData1.GetInt(SYSTEM_FIELD_ID);
                
                /*
                 * Validate the fields according to the catalog
                 */
                switch (intFieldType)
                {
                    case TYPE_INT:
                        
                        if (intIdAction == EVENT_INSERT && strFieldName.equals("id"))
                        {
                            // do nothing
                        }
                        else
                        {
                            intValue = CENTData1.GetInt(strFieldObject);

                            if (intFieldNullable == False)
                            {
                                if (intValue == Integer.MIN_VALUE || intValue == 0)
                                {
                                    throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                                }
                            }
                        }

                        /*
                         * Validate, field by field, if value is unique
                         */
                        if (intUnique == Yes)
                        {
                            /*
                             * Query current field
                             */
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetInt(strFieldObject, intValue);                            
                            ListCENTData1 = this.GetList(CENTFilter1);

                            /*
                             * For insert, check if record already exists is enough
                             */
                            if (intIdAction == EVENT_INSERT)
                            {
                                if (!ListCENTData1.isEmpty())
                                {
                                    throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                }                                
                            }
                            
                            /*
                             * For update, value id duplicated when ID is different, otherwise, usuer is saving without change anything
                             */                            
                            if (intIdAction == EVENT_UPDATE)
                            {
                                if (!ListCENTData1.isEmpty())
                                {                                    
                                    if (ListCENTData1.get(0).GetInt(SYSTEM_FIELD_ID) != intId)
                                    {
                                        throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                    }    
                                }                                
                            }
                        }

                        break;
                        
                    case TYPE_TEXT:
                        
                        strFieldValue = CENTData1.GetText(strFieldObject).trim();

                        if (intFieldNullable == False)
                        {
                            if (strFieldValue == null)
                            {
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }

                            if (strFieldValue.equals(""))
                            {                                
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }

                            if (strFieldValue.length() == 0)
                            {
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }

                            if (strFieldValue.length() > intFieldSize)
                            {
                                throw new CENTException("EXCEPTION_TEXT_TOO_LONG", strFieldLabel, strFieldName);
                            }
                        }

                        /*
                         * Validate, field by field, if value is unique
                         */
                        if (intUnique == Yes)
                        {
                            /*
                             * Query current field
                             */
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetText(strFieldObject, strFieldValue);                         
                            ListCENTData1 = this.GetList(CENTFilter1);

                            /*
                             * For insert, check if record already exists is enough
                             */
                            if (intIdAction == EVENT_INSERT)
                            {
                                if (!ListCENTData1.isEmpty())
                                {
                                    throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                }                                
                            }
                            
                            /*
                             * For update, value id duplicated when ID is different, otherwise, usuer is saving without change anything
                             */                            
                            if (intIdAction == EVENT_UPDATE)
                            {
                                if (!ListCENTData1.isEmpty())
                                {                                    
                                    if (ListCENTData1.get(0).GetInt(SYSTEM_FIELD_ID) != intId)
                                    {
                                        throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                    }    
                                }                                
                            }
                        }                        
                        
                        break;
                        
                    case TYPE_DATE:
                        
                        datValue = CENTData1.GetDate(strFieldObject);

                        if (intFieldNullable == False)
                        {
                            if (datValue == null)
                            {
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }
                        }

                        /*
                         * Validate, field by field, if value is unique
                         */
                        if (intUnique == Yes)
                        {
                            /*
                             * Query current field
                             */                            
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetDate(strFieldObject, datValue);                            
                            ListCENTData1 = this.GetList(CENTFilter1);
                            
                            /*
                             * For insert, check if record already exists is enough
                             */
                            if (intIdAction == EVENT_INSERT)
                            {
                                if (!ListCENTData1.isEmpty())
                                {
                                    throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                }                                
                            }
                            
                            /*
                             * For update, value id duplicated when ID is different, otherwise, usuer is saving without change anything
                             */                            
                            if (intIdAction == EVENT_UPDATE)
                            {
                                if (!ListCENTData1.isEmpty())
                                {                                    
                                    if (ListCENTData1.get(0).GetInt(SYSTEM_FIELD_ID) != intId)
                                    {
                                        throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                    }    
                                }                                
                            }
                        }
                        
                        break;    
                        
                    case TYPE_DOUBLE:
                        
                        dblValue = CENTData1.GetDouble(strFieldObject);

                        if (intFieldNullable == False)
                        {
                            if (dblValue == Double.MIN_VALUE)
                            {
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }
                        }

                        /*
                         * Validate, field by field, if value is unique
                         */                        
                        if (intUnique == Yes)
                        {
                            /*
                             * Query current field
                             */                            
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetDouble(strFieldObject, dblValue);                            
                            ListCENTData1 = this.GetList(CENTFilter1);
                            
                            /*
                             * For insert, check if record already exists is enough
                             */
                            if (intIdAction == EVENT_INSERT)
                            {
                                if (!ListCENTData1.isEmpty())
                                {
                                    throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                }                                
                            }
                            
                            /*
                             * For update, value id duplicated when ID is different, otherwise, usuer is saving without change anything
                             */                            
                            if (intIdAction == EVENT_UPDATE)
                            {
                                if (!ListCENTData1.isEmpty())
                                {                                    
                                    if (ListCENTData1.get(0).GetInt(SYSTEM_FIELD_ID) != intId)
                                    {
                                        throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                                    }    
                                }                                
                            }
                        }
                        
                        break;      
                        
                    case TYPE_BOOLEAN:
                        
                        intValue = CENTData1.GetBoolean(strFieldObject);

                        if (intFieldNullable == False)
                        {
                            if (intValue == Null)
                            {
                                throw new CENTException("EXCEPTION_MANDATORY_FIELD", strFieldLabel, strFieldName);
                            }
                        }                        
                        
                        if (intUnique == Yes && intIdAction == EVENT_INSERT)
                        {
                            CENTFilter1 = new CENTData();
                            CENTFilter1.SetBoolean(strFieldObject, intValue);
                            
                            ListCENTData1 = this.GetList(CENTFilter1);
                            
                            if (!ListCENTData1.isEmpty())
                            {
                                throw new CENTException("EXCEPTION_FIELD_DO_NOT_ACCEPT_DUPLICATED_VALUES", strFieldLabel, strFieldName);
                            }
                        }                        
                                                
                        break;                        
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
            strFieldLabel = null;
            strFieldName = null;        
            strFieldObject = null;        
            strFieldValue = null;

            datValue = null;
            CENTFilter1 = null;
            ListCENTData1 = null;
        }
    }        
    
    
    
    private void Profile(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException
    {        
        /*
         * General declaration
         */       
        CDALCore CDALCore1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                case EVENT_UPDATE:
                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    if (CENTData1.GetInt(GetFieldObject("id", ListCENTCatalog1)) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }
                    break;                            
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:

                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;                    
            }

            
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
            CDALCore1 = null;
        }        
    }    
    
    private void Area(List<CENTData> ListCENTCatalog1, CENTData CENTData1, int intIdEvent) throws CENTException, SQLException, Exception
    {        
        /*
         * General declaration
         */
        CDALCore CDALCore1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Custom validation
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                case EVENT_UPDATE:
                    break;

                case EVENT_DELETE: // Delete everything, with no exception

                    if (CENTData1.GetInt(GetFieldObject("id", ListCENTCatalog1)) == 0)
                    {
                        throw new CENTException("EXCEPTION_DELETE_MISSING_ID");
                    }
                    break;                            
            }
            
            /*
             * Specific action
             */
            switch (intIdEvent)
            {                        
                case EVENT_INSERT:
                            
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);
                    
                    break;
                    
                case EVENT_UPDATE:
                                
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);            
                    
                    break;

                case EVENT_DELETE:
                    
                    /*
                     * Persist the record
                     */
                    CDALCore1.SetIdTransaction(this.GetIdTransaction());
                    CDALCore1.SetIdView(this.GetIdView());
                    CDALCore1.Persist(CENTData1, intIdEvent);                        
                    
                    break;
            }
            
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
            CDALCore1 = null;
        }        
    }    
    
}
