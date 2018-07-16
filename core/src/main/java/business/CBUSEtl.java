/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import entity.CENTData;
import entity.CENTException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import oracle.jdbc.OracleTypes;
import persistence.CDALCore;
import persistence.CDALEtl;

/**
 *
 * @author David Lancioni - 2016
 * @target Import information from any source of information due to mapping process
 */
public class CBUSEtl extends CBUS
{
    public CBUSEtl(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
    
    /*
     * Private properties
     */
    private Path FilePathName;
    private final int ID_LAYOUT = 2;    
    private final String LAYOUT_SEQUENTIAL_DELIMITER = ";";        
    private final String FIELD_UNIFIELD = "FIELD_UNIFIED";
    private final String FIELD_REFUSED = "FIELD_REFUSED";

    /*
     * Catalogs used during the processing
     */
    private List<CENTData> ListCENTCatalogLayout1 = null;  // Layout
    private List<CENTData> ListCENTCatalogSession1 = null;  // Layout x Session
    private List<CENTData> ListCENTCatalogDefinition1 = null;  // Layotu x Session x Definition
    private List<CENTData> ListCENTCatalogTransaction1 = null;  // Current transaction
    private List<CENTData> ListCENTCatalogLayoutLookup1 = null;  // Current transaction
    private List<CENTData> ListCENTCatalogLayoutLookupItem1 = null;  // Current transaction    
    private List<CENTData> ListCENTJoinField1 = null;  // Layout
    
    /*
     * Import engeneering
     */
    private List<CENTData> ListCENTLayout1 = null;
    private List<CENTData> ListCENTLayoutSession1 = null;
    private List<CENTData> ListCENTLayoutDefinition1 = null;
    private List<CENTData> ListCENTLayoutFunction1 = null;
    private List<CENTData> ListCENTLayoutLookUpItem1 = null;
    private List<CENTData> ListCENTLayoutLookUp1 = null;    
    private List<CENTData> ListCENTDictionary1 = null;

    /*
     * Export engeneering
     */    
    private List<CENTData> ListCENTView1 = null;
    
    public void Import(List<CENTData> ListCENTLayout1) throws CENTException, Exception
    {
        /*
         * General Declaration
         */                
        int intIdArea = 0;
        int intIdProcess = 0;
        int intIdLayout = 0;
        int intIdLayoutType = 0;
        int intIdTransaction = 0;
        int intIdTable = 0;
        
        CENTData CENTFilter1 = new CENTData();            
        CBUSCore CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
        CDALEtl CDALIntegration1 = new CDALEtl(this.GetConnection(), this.GetSession());
        List<CENTData> ListCENTData1 = null;

        try
        {            
            /*
             * Keep all catalogs
             */
            ListCENTCatalogLayout1 = this.GetCatalog(TRN_LAYOUT);
            ListCENTCatalogSession1 = this.GetCatalog(TRN_LAYOUT_SESSION);
            ListCENTCatalogDefinition1 = this.GetCatalog(TRN_LAYOUT_SESSION_DEFINITION);
            ListCENTCatalogLayoutLookup1 = this.GetCatalog(TRN_LAYOUT_LAYOUT_LOOKUP);
            ListCENTCatalogLayoutLookupItem1 = this.GetCatalog(TRN_LAYOUT_LOOKUP_ITEM);
            
            /*
             * Validate each layout before start importing
             */
            for (CENTData CENTLayout1 : ListCENTLayout1)
            {                               
                /*
                 * Keep the unique id process
                 */                
                intIdProcess = CENTLayout1.GetInt(SYSTEM_FIELD_ID_PROCESS_ETL);
                
                /*
                 * Validate if layout exists and keep it's id
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(SYSTEM_FIELD_ID, CENTLayout1.GetInt(SYSTEM_FIELD_ID));
                CBUSCore1.SetIdTransaction(TRN_LAYOUT);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                ListCENTData1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTData1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_NOT_FOUND");
                }
                
                /*
                 * Keep layout details
                 */                
                CENTLayout1 = ListCENTData1.get(0);                                                
                intIdLayout = CENTLayout1.GetInt(GetFieldObject("id", ListCENTCatalogLayout1));
                intIdLayoutType = CENTLayout1.GetInt(GetFieldObject("id_type", ListCENTCatalogLayout1));
                intIdTransaction = CENTLayout1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
                intIdArea = CENTLayout1.GetInt(GetFieldObject("id_area", ListCENTCatalogLayout1));
                ListCENTCatalogTransaction1 = this.GetCatalog(intIdTransaction);
                CENTLayout1.SetInt(SYSTEM_FIELD_ID_PROCESS_ETL, intIdProcess);
                
                /*
                 * Validate if layout has sessions
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);                
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                ListCENTLayoutSession1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTLayoutSession1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_SESSION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }

                /*
                 * Validate if layout has session's definitions
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogDefinition1), intIdLayout);
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTLayoutDefinition1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTLayoutDefinition1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_SESSION_DEFINITION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }
                
                /*
                 * Get the list of functions to do translation
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(2, intIdLayout);            
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_FUNCTION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTLayoutFunction1 = CBUSCore1.GetList(CENTFilter1);

                /*
                 * Get the list of lookups to do translations
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(2, intIdLayout);            
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_LAYOUT_LOOKUP);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTLayoutLookUp1 = CBUSCore1.GetList(CENTFilter1);

                /*
                 * Get the list of lookup item definition
                 */                
                CENTFilter1 = new CENTData();
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_LOOKUP_ITEM);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTLayoutLookUpItem1 = CBUSCore1.GetList(CENTFilter1);

                /*
                 * Delete duplicated import process
                 */
                CDALIntegration1.Delete(intIdProcess, intIdTransaction);     

                /*
                 * Make sure it won't duplicate the id
                 */                
                CENTLayout1.SetInt(ID_LAYOUT, CENTLayout1.GetInt(SYSTEM_FIELD_ID));
                CENTLayout1.SetInt(SYSTEM_FIELD_ID, this.GetLastIdByTransaction(intIdTransaction));

                /*
                 * Decide the algorhitim according to the layout
                 */
                switch (intIdLayoutType)
                {
                    case LAYOUT_TYPE_TEXT_FILE:
                        ImportTextFile(CENTLayout1);
                        break;                  

                    case LAYOUT_TYPE_DATABASE_CONNECTION:
                        ImportData(CENTLayout1);
                        break;
                        
                    default:
                        throw new CENTException("EXCEPTION_INVALID_LAYOUT_TYPE", String.valueOf(intIdLayoutType));
                }
                
                /*
                 * Update process id for this layout
                 */                
                CDALIntegration1.UpdateProcessId(intIdTransaction, intIdLayout, intIdProcess, intIdArea);

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
            throw new CENTException("EXCEPTION_FAIL_BUSINESS", Exception1.getMessage());
        } 
        finally
        {            
            CBUSCore1 = null;
            CENTFilter1 = null;
            CDALIntegration1 = null;
            ListCENTData1 = null;
            
            ListCENTLayoutSession1 = null;
            ListCENTLayoutDefinition1 = null;
            ListCENTLayoutFunction1 = null;
            ListCENTLayoutLookUpItem1 = null;
            ListCENTLayoutLookUp1 = null;
            
            ListCENTCatalogLayout1 = null;
            ListCENTCatalogSession1 = null;
            ListCENTCatalogDefinition1 = null;
            ListCENTCatalogTransaction1 = null;

        }
    } 

    public void Export(List<CENTData> ListCENTLayout1) throws CENTException, Exception
    {
        /*
         * General Declaration
         */                
        int intIdProcess = 0;
        int intIdLayout = 0;
        int intIdLayoutType = 0;
        int intIdTransaction = 0;
        
        CBUSCore CBUSCore1 = null;
        CENTData CENTLayout2 = null;
        CENTData CENTFilter1 = null;                
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTLayout2 = null;
        CBUSEtl CBUSIntegration1 = null;
        
        try
        {
            /*
             * Create the Objects
             */
            ListCENTData1 = new ArrayList<CENTData>();
            CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
            CBUSIntegration1 = new CBUSEtl(this.GetConnection(), this.GetSession());
            CENTFilter1 = new CENTData();            
            
            /*
             * Keep all catalogs
             */
            ListCENTCatalogLayout1 = this.GetCatalog(TRN_LAYOUT);
            ListCENTCatalogSession1 = this.GetCatalog(TRN_LAYOUT_SESSION);
            ListCENTCatalogDefinition1 = this.GetCatalog(TRN_LAYOUT_SESSION_DEFINITION);
            
            /*
             * Get the dictionary for current language
             */
            ListCENTDictionary1 = CBUSCore1.GetDictionary();
            
            /*
             * Validate each layout before start exporting
             */
            for (CENTData CENTLayout1 : ListCENTLayout1)
            {                
                /*
                 * Validate if layout exists and keep it's id
                 */
                CENTLayout2 = new CENTData();
                CENTLayout2.SetInt(GetFieldObject("id", ListCENTCatalogLayout1), CENTLayout1.GetInt(SYSTEM_FIELD_ID));
                intIdLayout = CENTLayout1.GetInt(SYSTEM_FIELD_ID);
                intIdProcess = CENTLayout1.GetInt(SYSTEM_FIELD_ID_PROCESS_ETL);
                
                CBUSCore1.SetIdTransaction(TRN_LAYOUT);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                ListCENTLayout2 = CBUSCore1.GetList(CENTLayout2);

                if (ListCENTLayout2.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_NOT_FOUND", "Id Layout: " + intIdLayout);
                }
                
                /*
                 * Keep layout details
                 */               
                this.ListCENTLayout1 = ListCENTLayout2;
                CENTLayout2 = ListCENTLayout2.get(0);
                
                intIdLayout = CENTLayout2.GetInt(GetFieldObject("id", ListCENTCatalogLayout1));
                intIdLayoutType = CENTLayout2.GetInt(GetFieldObject("id_type", ListCENTCatalogLayout1));
                intIdTransaction = CENTLayout2.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
                ListCENTCatalogTransaction1 = this.GetCatalog(intIdTransaction);                
                
                /*
                 * Validate if layout has sessions
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);                
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                ListCENTLayoutSession1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTLayoutSession1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_SESSION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }

                /*
                 * Validate if layout has session's definitions
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogDefinition1), intIdLayout);
                CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTLayoutDefinition1 = CBUSCore1.GetList(CENTFilter1);

                if (ListCENTLayoutDefinition1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_LAYOUT_SESSION_DEFINITION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }
                
                /*
                 * Get data from transaction
                 */                
                CBUSCore1.SetIdTransaction(CENTLayout1.GetInt(SYSTEM_FIELD_ID_TRANSACTION));
                CBUSCore1.SetIdView(CENTLayout1.GetInt(SYSTEM_FIELD_ID_VIEW));
                ListCENTData1 = CBUSCore1.GetList(CENTLayout1);
                
                //if (ListCENTData1.isEmpty())
                //{
                //    throw new CENTException("EXCEPTION_LAYOUT_NO_INFORMATION_TO_EXPORT", "Id Layout: " + intIdLayout);
                //}

                /*
                 * Get the view related to transaction
                 */            
                ListCENTView1 = CBUSCore1.GetView(CENTLayout1.GetInt(SYSTEM_FIELD_ID_VIEW));

                if (ListCENTView1.isEmpty())
                {
                    throw new CENTException("EXCEPTION_VIEW_NOT_FOUND", "Id Layout: " + intIdLayout);
                }                

                /*
                 * Decide the algorhitim according to the layout
                 */
                switch (intIdLayoutType)
                {
                    case LAYOUT_TYPE_TEXT_FILE:
                        CBUSIntegration1.ExportTextFileSimple(CENTLayout1, ListCENTData1);
                        break;

                    case LAYOUT_TYPE_DATABASE_CONNECTION:
                        //CBUSIntegration1.ExportData(CENTLayout2);
                        break;
                        
                    default:
                        throw new CENTException("EXCEPTION_INVALID_LAYOUT_TYPE", String.valueOf(intIdLayoutType));
                }
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

            CBUSCore1 = null;
            CENTLayout2 = null;
            CENTFilter1 = null;                
            ListCENTData1 = null;
            ListCENTLayout2 = null;
            CBUSIntegration1 = null;            
                        
            this.ListCENTLayout1 = null;
            ListCENTLayoutSession1 = null;
            ListCENTLayoutDefinition1 = null;
            
            ListCENTCatalogLayout1 = null;
            ListCENTCatalogSession1 = null;
            ListCENTCatalogDefinition1 = null;
            ListCENTCatalogTransaction1 = null;
        }
    }

    /*
     * Import file with complex structure like header and trailer and differente sessions
     */
    private void ImportTextFile(CENTData CENTLayout1) throws CENTException, Exception
    {
        int intId = 0;         
        int intLine = 0;
        int intSize = 0;
        int intPass = 0;
        int intIdLayout = 0;        
        int intIdProcess = 0;        
        int intQttSession = 0;
        int intLineToStart = 0;
        int intIdTransaction = 0;
        int intStartPosition = 0;
        int intLineLimit = 0;
        int intFileAction = 0;
        
        String strLine = "";        
        String strTable = "";
        String strFileName = "";
        String strLineHeader = "";
        String strLineTrailer = "";
        String strNextSession = "";        
        String strCurrentSession = "";        
        String strIdentifierHeader = "";
        String strIdentifierBreak = "";
        String strIdentifierTrailer = "";
        String strIdentifierCurrent = "";
        String strNewFileName = "";

        Scanner Scanner1 = null;                
        CENTData CENTCurrentSession1 = null;
        CENTData CENTData1 = null;
        CENTData CENTFilter1 = null;
        CDALCore CDALCore1 = null;
        CDALEtl CDALEtl1 = null;
        
        List<CENTData> ListCENTData1 = null;        
        List<List<CENTData>> ListListCENTLayoutSessionDefinition1 = null;                
        List<Integer> ListLayoutSessionId = null;
        List<String> ListLayoutSessionCode = null;  

        try            
        {
            /*
             * Log information
             */            
            intId = CENTLayout1.GetInt(SYSTEM_FIELD_ID);
            
            /*
             * General Declaration
             */
            CENTData1 = new CENTData();            
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            CDALEtl1 = new CDALEtl(this.GetConnection(), this.GetSession());
            ListListCENTLayoutSessionDefinition1 = new ArrayList<List<CENTData>>();
            ListLayoutSessionId = new ArrayList<Integer>();
            ListLayoutSessionCode = new ArrayList<String>();      
            
            /*
             * Get layout details
             */       
            intIdLayout = CENTLayout1.GetInt(ID_LAYOUT);
            intIdProcess = CENTLayout1.GetInt(SYSTEM_FIELD_ID_PROCESS_ETL);
            intIdTransaction = CENTLayout1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
            intLineToStart = CENTLayout1.GetInt(GetFieldObject("line_start", ListCENTCatalogLayout1));
            strFileName = CENTLayout1.GetText(GetFieldObject("input", ListCENTCatalogLayout1));
            intStartPosition = CENTLayout1.GetInt(GetFieldObject("position", ListCENTCatalogLayout1));
            intSize = CENTLayout1.GetInt(GetFieldObject("size", ListCENTCatalogLayout1));  
            intFileAction = CENTLayout1.GetInt(GetFieldObject("file_action", ListCENTCatalogLayout1));

            if(! new File(strFileName).exists()) 
            {  
               throw new CENTException("EXCEPTION_FILE_NOT_FOUND", strFileName);
            }                

            /*
             * Keep the LayoutDefs and it's identifier in memory (critical to performance)
             */
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);
            intQttSession = ListCENTData1.size();

            for (CENTData CENTData2 : ListCENTData1)
            {
                /*
                 * Keep the ID/Code
                 */
                ListLayoutSessionId.add(CENTData2.GetInt(GetFieldObject("id", ListCENTCatalogSession1)));
                ListLayoutSessionCode.add(CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1)));

                /*
                 * Keep the session
                 */                
                ListCENTData1 = null;
                CENTFilter1 = new CENTData();            
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
                CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTData1 = CDALCore1.GetList(CENTFilter1);
                
                if (ListCENTData1.isEmpty())
                {
                    throw new CENTException("LAYOUT_DEFINITION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }                
                
                /*
                 * Keep the definition
                 */                
                ListCENTData1 = null;
                CENTFilter1 = new CENTData();            
                CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
                CENTFilter1.SetInt(GetFieldObject("id_session", ListCENTCatalogDefinition1), CENTData2.GetInt(GetFieldObject("id", ListCENTCatalogSession1)));
                CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION_DEFINITION);
                CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
                ListCENTData1 = CDALCore1.GetList(CENTFilter1);
                
                if (ListCENTData1.isEmpty())
                {
                    throw new CENTException("LAYOUT_DEFINITION_NOT_FOUND", "Id Layout: " + intIdLayout);
                }
                
                ListListCENTLayoutSessionDefinition1.add(ListCENTData1);                
            }
            
            
            /* 
             * Figure out the initial session involved (Header, Trailer and Break)
             * Break is the first session of the record group, it identifies the new record
             */
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CENTFilter1.SetInt(GetFieldObject("id_type", ListCENTCatalogSession1), LAYOUT_SESSION_TYPE_RECORD);
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {
                strIdentifierBreak = CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                break;
            }
            
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CENTFilter1.SetInt(GetFieldObject("id_type", ListCENTCatalogSession1), LAYOUT_SESSION_TYPE_HEADER);
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);            
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {
                strIdentifierHeader = CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                break;
            }
            
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CENTFilter1.SetInt(GetFieldObject("id_type", ListCENTCatalogSession1), LAYOUT_SESSION_TYPE_TRAILER);
            CDALCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CDALCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData1 = CDALCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData1)
            {
                strIdentifierTrailer = CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                break;
            }

            /*
             * Open the file
             */
            FilePathName = Paths.get(strFileName);
            Scanner1 = new Scanner(FilePathName, "ISO-8859-1");

            /*
             * Get the table to persist the information
             */            
            strTable = this.GetTable(intIdTransaction);
            
            /*
             * Read and associate with the fields
             */
            while (Scanner1.hasNextLine())
            {
                strLine = Scanner1.nextLine();   

                if (!strLine.trim().equals(""))
                {
                    intLine++;
                    System.out.println("Importing line: " + intLine);
                    
                    /*
                     * Control the limit of lines, limited by contract
                     */
                    if (intLineLimit > 0)
                    {
                        if (intLine > intLineLimit)
                        {
                            throw new CENTException("EXCEPTION_LAYOUT_LINE_LIMIT", String.valueOf(intLineLimit));
                        }
                    }

                    if (intLine >= intLineToStart)
                    {
                        strNextSession = GetSessionIdentifier(intStartPosition, intSize, strLine);

                        /*
                         * Get current session
                         */                        
                        CENTCurrentSession1 = null;
                        
                        for (CENTData CENTData3 : ListCENTLayoutSession1)
                        {
                            if (CENTData3.GetText(GetFieldObject("identifier", ListCENTCatalogSession1)).equals(strNextSession))
                            {
                                CENTCurrentSession1 = ListCENTLayoutSession1.get(0);
                                break;
                            }
                        }

                        /*
                         * Get the current layout definition
                         */
                        ListCENTLayoutDefinition1 = this.GetLayoutDefinition(strNextSession, ListLayoutSessionCode, ListListCENTLayoutSessionDefinition1);                        
                        
                        if (!ListCENTLayoutDefinition1.isEmpty())
                        {
                            if (strNextSession.equals("")) // Single session, must have one session 
                            {
                                CENTCurrentSession1 = ListCENTLayoutSession1.get(0);                                
                                CENTData1 = this.ReadLine(CENTData1, strLine, CENTCurrentSession1);
                                
                                /*
                                 * For system transactions, use the business methods
                                 */
                                if (intIdTransaction == TRN_CATALOG)
                                {
                                    ListCENTData1 = new ArrayList<CENTData>();
                                    ListCENTData1.add(CENTData1);
                                    CBUSCore CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
                                    CBUSCore1.SetIdTransaction(intIdTransaction);
                                    CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
                                    CBUSCore1.ExecuteEvent(EVENT_INSERT, ListCENTData1);
                                }
                                else
                                {
                                    CENTData1.SetInt(SYSTEM_FIELD_ID, ++ intId);
                                    CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                                    CDALEtl1.Persist(intIdTransaction, ListCENTCatalogTransaction1, CENTData1, strTable);                                                                    
                                }
                            }
                            else
                            {                               
                                if (!strNextSession.equals(strIdentifierHeader) && !strNextSession.equals(strIdentifierTrailer))
                                {                                                                       
                                    if (strNextSession.equals(strIdentifierBreak) || strNextSession.equals(strIdentifierCurrent))
                                    {
                                        intPass ++;

                                        if (intPass > 1)
                                        {
                                            CENTData1.SetInt(SYSTEM_FIELD_ID, ++ intId);
                                            CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                                            CDALEtl1.Persist(intIdTransaction, ListCENTCatalogTransaction1, CENTData1, strTable);
                                        }

                                        strCurrentSession = GetSessionIdentifier(intStartPosition, intSize, strLine);
                                        if (strCurrentSession.equals(strNextSession))
                                        {
                                            CENTData1 = this.ReadLine(CENTData1, strLine, CENTCurrentSession1);
                                            CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                                        }
                                    }
                                    else
                                    {
                                        strCurrentSession = GetSessionIdentifier(intStartPosition, intSize, strLine);
                                        if (strCurrentSession.equals(strNextSession))
                                        {                                        
                                            CENTData1 = this.ReadLine(CENTData1, strLine, CENTCurrentSession1);
                                            CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                                        }
                                        
                                    }  // Break

                                    strIdentifierCurrent = strNextSession; // Control repeat

                                } // Discard Header e Trailer
                                else
                                {
                                    if (strNextSession.equals(strIdentifierHeader))
                                    {
                                        strLineHeader = strLine;
                                    }
                                    else
                                    {
                                        strLineTrailer = strLine;
                                    }

                                }  // Session Control
                            }
                            
                        } // Mapped session
                        
                    }  // Line > LineToStart

                }  // Line <> ""

            } // While            
            
                        
            if (intQttSession > 1)
            {
                ListCENTLayoutDefinition1 = this.GetLayoutDefinition(strNextSession, ListLayoutSessionCode, ListListCENTLayoutSessionDefinition1);
                CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                CENTData1 = this.ReadLine(CENTData1, strLine, CENTCurrentSession1);
                
                CENTData1.SetInt(SYSTEM_FIELD_ID, ++ intId);
                CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                CDALEtl1.Persist(intIdTransaction, ListCENTCatalogTransaction1, CENTData1, strTable);
            }

            /*
             * Update HEADER info (must use an empty class)
             */
            if (!strIdentifierHeader.equals(""))
            {
                CENTData1 = new CENTData();
                
                ListCENTLayoutDefinition1 = this.GetLayoutDefinition(strIdentifierHeader, ListLayoutSessionCode, ListListCENTLayoutSessionDefinition1);
                CENTData1 = this.ReadLine(CENTData1, strLineHeader, CENTCurrentSession1);
                CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);
                
                CDALEtl1.SetIdTransaction(intIdTransaction);
                CDALEtl1.SetIdView(DO_NOT_USE_VIEW);
                CDALEtl1.UpdateHeaderTrailer(CENTData1);
            }

            /*
             * Update TRAILER info (must use an empty class)
             */
            if (! strIdentifierTrailer.equals(""))
            {            
                CENTData1 = new CENTData();                
                
                ListCENTLayoutDefinition1 = this.GetLayoutDefinition(strIdentifierTrailer, ListLayoutSessionCode, ListListCENTLayoutSessionDefinition1);
                CENTData1 = this.ReadLine(CENTData1, strLineTrailer, CENTCurrentSession1);
                CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);

                CDALEtl1.SetIdTransaction(intIdTransaction);
                CDALEtl1.SetIdView(DO_NOT_USE_VIEW);
                CDALEtl1.UpdateHeaderTrailer(CENTData1);
            }
            
            intLine++;
            
            /*
             * File imported, take action nothing, rename or delete. Based on domain_25.
             */
            switch (intFileAction)
            {
                case 1:                    
                    /*
                     * Just do nothig
                     */
                    
                    break;
                    
                case 2:
                    
                    /*
                     * Prepare new name
                     */                    
                    strNewFileName = this.GetNewFileName(strFileName);
                    
                    /*
                     * Rename the file
                     */                    
                    this.RenameFile(strFileName, strNewFileName);
                    
                    /*
                     * Done
                     */                                        
                    break;
                    
                case 3:
                    
                    /*
                     * Delete the file
                     */                    
                    this.DeleteFile(strFileName);
                    
                    /*
                     * Done
                     */                        
                    break;
            }
        }
        catch (CENTException CENTException1)
        {
            /*
             * Prepare new name
             */
            strNewFileName = this.GetNewFileName(strFileName);

            /*
             * Rename the file
             */
            this.RenameFile(strFileName, strNewFileName);            
            
            /*
             * Throw it
             */            
            throw CENTException1;
        }
        catch (Exception Exception1)
        {
            /*
             * Prepare new name
             */
            strNewFileName = this.GetNewFileName(strFileName);  

            /*
             * Rename the file
             */
            this.RenameFile(strFileName, strNewFileName);            
            
            /*
             * Throw it
             */                    
            throw new CENTException("EXCEPTION_FAIL_BUSINESS", Exception1.getMessage());
        }
        finally
        {
            strLine = null;
            strTable = null;
            strFileName = null;
            strLineHeader = null;
            strLineTrailer = null;
            strNextSession = null;        
            strCurrentSession = null;        
            strIdentifierHeader = null;
            strIdentifierBreak = null;
            strIdentifierTrailer = null;
            strIdentifierCurrent = null;
            strNewFileName = null;

            Scanner1 = null;                
            CENTCurrentSession1 = null;
            CENTData1 = null;
            CENTFilter1 = null;
            CDALCore1 = null;
            CDALEtl1 = null;

            ListCENTData1 = null;        
            ListListCENTLayoutSessionDefinition1 = null;                
            ListLayoutSessionId = null;
            ListLayoutSessionCode = null;  

        }
    }   

    /*
     * Import information via database, basically, execute the procedures stand of a text file
     */
    private void ImportData(CENTData CENTLayout1) throws CENTException, Exception
    {        
        /*
         * General Declaration
         */        
        int intId = 0;         
        int intLine = 0;
        int intLineToStart = 0;
        int intIdLayout = 0;
        int intIdTransaction = 0;
        int intNumberOfColumns = 0;
        int intIdColumnType = 0;        

        String strSql = "";
        String strTable = "";
        String strMask = "";
        String strLine = "";

        ResultSet ResultSet1 = null;
        ResultSetMetaData ResultSetMetaData1 = null;        
        
        CENTData CENTData1 = null;
        CDALCore CDALCore1 = null;
        CDALEtl CDALEtl1 = null;
        
        List<CENTData> ListCENTLayout1 = null;        
        CENTData CENTLayoutSession1 = null;        
        CallableStatement CallableStatement1 = null;
        
        try            
        {
            /*
             * General Declaration
             */
            CENTData1 = new CENTData();            
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            CDALEtl1 = new CDALEtl(this.GetConnection(), this.GetSession());
            
            /*
             * Get layout details
             */
            intIdLayout = CENTLayout1.GetInt(GetFieldObject("id", ListCENTCatalogLayout1));
            intIdTransaction = CENTLayout1.GetInt(GetFieldObject("id_transaction", ListCENTCatalogLayout1));
            intLineToStart = CENTLayout1.GetInt(GetFieldObject("line_start", ListCENTCatalogLayout1));
            strSql = "call " + CENTLayout1.GetText(GetFieldObject("input", ListCENTCatalogLayout1)) + "(?)";            
            
            /*
             * Get the session definition for unique session
             */            
            CENTLayoutSession1 = ListCENTLayoutSession1.get(0);                        
            
            /*
             * Get the table related to the reconciliation area
             */
            strTable = this.GetTable(intIdTransaction);
            
            /*
             * Open the data source (procedure in this case)
             */            
            CallableStatement1 = Connection1.prepareCall(strSql);
            CallableStatement1.registerOutParameter(1, OracleTypes.CURSOR);            
            CallableStatement1.execute();
            ResultSet1 = (ResultSet) CallableStatement1.getObject(1);          

            ResultSetMetaData1 = ResultSet1.getMetaData();            
            intNumberOfColumns = ResultSetMetaData1.getColumnCount();
            
            /*
             * Persist the file lines
             */            
            while (ResultSet1.next())
            {
                intLine ++;
                strLine = "";
                
                if (intLine >= intLineToStart)
                {
                    for (int intColumn=1; intColumn<=intNumberOfColumns; intColumn++)
                    {
                        intIdColumnType = ResultSetMetaData1.getColumnType(intColumn);

                        switch (intIdColumnType)
                        {
                            case java.sql.Types.INTEGER:
                            case java.sql.Types.TINYINT:
                            case java.sql.Types.BIGINT:
                            case java.sql.Types.SMALLINT:
                            case java.sql.Types.NUMERIC:
                            case java.sql.Types.DOUBLE:
                            case java.sql.Types.FLOAT:

                                for (CENTData CENTData2 : ListCENTLayoutDefinition1)
                                {                                    
                                    if (CENTData2.GetInt(GetFieldObject("position", ListCENTCatalogDefinition1)) == intColumn)
                                    {
                                        strMask = CENTData2.GetText(GetFieldObject("mask", ListCENTCatalogDefinition1));
                                        break;                                    
                                    }
                                }
                                
                                if (strMask.equals(""))
                                {
                                    strLine += ResultSet1.getInt(intColumn);
                                }
                                else
                                {
                                    strLine += CDALCore1.DoubleToString(ResultSet1.getDouble(intColumn), strMask);
                                }
                                
                                break;

                            case java.sql.Types.CHAR:
                            case java.sql.Types.VARCHAR:

                                if (ResultSet1.getString(intColumn) != null)                            
                                {
                                    strLine += ResultSet1.getString(intColumn).trim();
                                }
                                break;

                            case java.sql.Types.DATE:
                            case java.sql.Types.TIME:
                            case java.sql.Types.TIMESTAMP:                            

                                if (ResultSet1.getDate(intColumn) != null)
                                {    
                                    for (CENTData CENTData2 : ListCENTLayoutDefinition1)
                                    {                                    
                                        if (CENTData2.GetInt(GetFieldObject("position", ListCENTCatalogDefinition1)) == intColumn)
                                        {
                                            strMask = CENTData2.GetText(GetFieldObject("mask", ListCENTCatalogDefinition1));
                                            break;
                                        }
                                    }

                                    strLine += CDALCore1.DateToString(ResultSet1.getDate(intColumn), strMask);
                                }

                                break;                            

                        }

                        if (intColumn < intNumberOfColumns)
                        {
                            strLine += LAYOUT_SEQUENTIAL_DELIMITER;
                        }
                    }
                }

                /*
                 * Persist the line
                 */
                CENTData1 = this.ReadLine(CENTData1, strLine, CENTLayoutSession1);
                
                CENTData1.SetInt(SYSTEM_FIELD_ID, ++intId);
                CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT, intIdLayout);                
                CDALEtl1.Persist(intIdTransaction, ListCENTCatalogTransaction1, CENTData1, strTable);
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
            strSql = "";
            strTable = "";
            strMask = "";
            strLine = "";

            ResultSet1 = null;
            ResultSetMetaData1 = null;        

            CENTData1 = null;
            CDALCore1 = null;
            CDALEtl1 = null;

            ListCENTLayout1 = null;        
            CENTLayoutSession1 = null;        
            CallableStatement1 = null;
        }
    }    

    
    
    
    /*
     * Import simple text file
     */
    private void ExportTextFileSimple(CENTData CENTLayout1, List<CENTData> ListCENTData1) throws CENTException, Exception
    {
        /*
         * General Declaration
         */        
        int intIdExportHeader = 0;

        String strHeader = "";
        String strLine = "";
        String strFileName = "";

        Date datProcessStart = null;
        Date datProcessEnd = null;
       
        FileWriter FileWriter1 = null;
                
        try            
        {
            /*
             * Log information
             */            
            datProcessStart = new Date();
            datProcessEnd = new Date();
            
            /*
             * Get the file to export
             */
            intIdExportHeader = ListCENTLayout1.get(0).GetInt(GetFieldObject("header", ListCENTCatalogLayout1));
            strFileName = ListCENTLayout1.get(0).GetText(GetFieldObject("output", ListCENTCatalogLayout1));
            FileWriter1 = new FileWriter(strFileName);            
            
            /*
             * Export header when necessary
             */            
            if (intIdExportHeader == Yes)
            {
                strHeader += GetHeaderToExport(ListCENTView1, ListCENTLayoutDefinition1);
                FileWriter1.write(strHeader);
            }
            
            /*
             * Export the lines
             */
            for (CENTData CENTData1 : ListCENTData1)
            {
                strLine = GetLineToExport(CENTData1, ListCENTLayoutDefinition1); // See multiple sessions to remember why
                FileWriter1.write(strLine);                
            }
            
            /*
             * Log information
             */            
            datProcessEnd = new Date();                  
            
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
             * Close the file
             */        
            if (FileWriter1 != null)
            {
                FileWriter1.close();            
            }
            
            System.out.println("Start: " + datProcessStart.toString());
            System.out.println("End: " + datProcessEnd.toString());

            datProcessStart = null;
            datProcessEnd = null;
            
            strHeader = null;
            strLine = null;
            strFileName = null;

            FileWriter1 = null;

            ListCENTView1 = null;
            ListCENTData1 = null;
        }
    }

    private void ExportTextFileSession(CENTData CENTLayout1, List<CENTData> ListCENTData1) throws CENTException, Exception
    {
        /*
         * General Declaration
         */        
        boolean blnHeader = true;
        boolean blnTrailer = true;
        
        int intIdLayout = 0;
        int intIdSession = 0;
                
        String strLine = "";
        String strFileName = "";
        String strIdentifier = "";
        String strIdentifierHeader = "";
        String strIdentifierTrailer = "";        

        Date datProcessStart = null;
        Date datProcessEnd = null;

        CENTData CENTFilter1 = null;
        
        FileWriter FileWriter1 = null;
        
        List<CENTData> ListCENTData2 = null;
        List<CENTData> ListCENTDefinition1 = null;
        
        CBUSCore CBUSCore1 = null;
                
        try            
        {
            /*
             * Create the objects
             */                  
            CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());
            
            /*
             * Log information
             */            
            datProcessStart = new Date();
            datProcessEnd = new Date();
            
            /*
             * Get the file to export
             */
            intIdLayout = ListCENTLayout1.get(0).GetInt(GetFieldObject("id", ListCENTCatalogLayout1));
            strFileName = ListCENTLayout1.get(0).GetText(GetFieldObject("output", ListCENTCatalogLayout1));
            FileWriter1 = new FileWriter(strFileName);            
            
            /*
             * Identify the session header
             */
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CENTFilter1.SetInt(GetFieldObject("id_type", ListCENTCatalogSession1), LAYOUT_SESSION_TYPE_HEADER);
            CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CBUSCore1.SetIdView(DO_NOT_USE_VIEW);            
            ListCENTData2 = CBUSCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                strIdentifierHeader = CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                break;
            }

            /*
             * Identify the session trailer
             */            
            CENTFilter1 = new CENTData();            
            CENTFilter1.SetInt(GetFieldObject("id_layout", ListCENTCatalogSession1), intIdLayout);
            CENTFilter1.SetInt(GetFieldObject("id_type", ListCENTCatalogSession1), LAYOUT_SESSION_TYPE_TRAILER);
            CBUSCore1.SetIdTransaction(TRN_LAYOUT_SESSION);
            CBUSCore1.SetIdView(DO_NOT_USE_VIEW);
            ListCENTData2 = CBUSCore1.GetList(CENTFilter1);

            for (CENTData CENTData2 : ListCENTData2)
            {
                strIdentifierTrailer = CENTData2.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                break;
            }            
            
            
            /*
             * Export the lines
             */
            for (CENTData CENTData1 : ListCENTData1)
            {
                for (CENTData CENTSession1 : ListCENTLayoutSession1)
                {            
                    /*
                     * Keep the session details
                     */
                    intIdSession = CENTSession1.GetInt(GetFieldObject("id", ListCENTCatalogSession1));
                    strIdentifier = CENTSession1.GetText(GetFieldObject("identifier", ListCENTCatalogSession1));
                    strLine += strIdentifier;

                    /*
                     * Get specific session definition among all
                     */
                    ListCENTDefinition1 = new ArrayList<CENTData>();

                    for (CENTData CENTData2 : ListCENTLayoutDefinition1)
                    {
                        if (CENTData2.GetInt(GetFieldObject("id_session", ListCENTCatalogDefinition1)) == intIdSession)
                        {
                            ListCENTDefinition1.add(CENTData2);
                        }
                    }

                    /*
                     * Header, trailer or contents
                     */                    
                    if (strIdentifier.equals(strIdentifierHeader))
                    {
                        if (blnHeader)
                        {
                            strLine = GetLineToExport(ListCENTData1.get(0), ListCENTDefinition1);
                            FileWriter1.write(strLine);
                            blnHeader = false;
                        }
                    }                                
                    else if (strIdentifier.equals(strIdentifierTrailer))
                    {
                        if (blnTrailer)
                        {                        
                            strLine = GetLineToExport(ListCENTData1.get(0), ListCENTDefinition1);
                            FileWriter1.write(strLine);
                            blnTrailer = false;
                        }
                    }                
                    else
                    {
                        strLine = GetLineToExport(CENTData1, ListCENTDefinition1);
                        FileWriter1.write(strLine);                    
                    }

                }
            }
            /*
             * Log information
             */            
            datProcessEnd = new Date();                  
            
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
             * Close the file
             */        
            if (FileWriter1 != null)
            {
                FileWriter1.close();            
            }
            
            System.out.println("Start: " + datProcessStart.toString());
            System.out.println("End: " + datProcessEnd.toString());

            datProcessStart = null;
            datProcessEnd = null;
            
            strLine = null;
            strFileName = null;

            FileWriter1 = null;

            ListCENTView1 = null;
            ListCENTData1 = null;
        }
    }    
    
    
    /*
     * Private methods
     */
    private CENTData ReadLine(CENTData CENTData1, String strLine, CENTData CENTLayoutSession1) throws Exception
    {
        /*
         * General Declaration
         */     
        int i = 0;
        int intIdLayout = 0;
        int intIdField = 0;
        int intFieldType = 0;
        int intPosition = 0;
        int intNumberOfMappedFields = 0;
        
        String strField = "";        
        String strText  = "";        
        String strFieldObject = "";
        String arrField[] = null;
        String strDelimiter = "";
        String strMask = "";

        ListCENTJoinField1 = new ArrayList<CENTData>();
        strLine += " ";
        
        try
        {
            CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT_LINE_STATE, LINE_STATE_IMPORT);
            
            /*
             * Count the number of mapped fields, that must match the file. Do not consider default value fields
             */
            if (CENTLayoutSession1.GetInt(GetFieldObject("id_format", ListCENTCatalogSession1)) != LAYOUT_FORMAT_FIXED_LENGTH)
            {
                intNumberOfMappedFields = 0;

                for (CENTData CENTData2 : ListCENTLayoutDefinition1)
                {
                    if (CENTData2.GetText(GetFieldObject("default_value", ListCENTCatalogDefinition1)).trim().equals(""))
                    {
                        intNumberOfMappedFields ++;
                    }
                }
            }
            
            /*
             * Read the mapping and take each information from the line
             */            
            for (CENTData CENTDefiniton1 : ListCENTLayoutDefinition1)                                           
            {   
                if (CENTDefiniton1.GetText(GetFieldObject("default_value", ListCENTCatalogDefinition1)).trim().equals(""))
                {    
                    switch (CENTLayoutSession1.GetInt(GetFieldObject("id_format", ListCENTCatalogSession1)))
                    {
                        case LAYOUT_FORMAT_DELIMITED:
                            strDelimiter = CENTLayoutSession1.GetText(GetFieldObject("delimiter", ListCENTCatalogSession1)).trim();
                            arrField = strLine.split(strDelimiter);
                            intPosition = CENTDefiniton1.GetInt(GetFieldObject("position", ListCENTCatalogDefinition1)) -1;
                            strText = arrField[intPosition].trim();

                            break;

                        case LAYOUT_FORMAT_FIXED_LENGTH:

                            strText = this.GetText(CENTDefiniton1, strLine);
                            break;

                        case LAYOUT_FORMAT_SEQUENTIAL:    
                            
                            strDelimiter = LAYOUT_SEQUENTIAL_DELIMITER;
                            arrField = strLine.split(strDelimiter);
                            intPosition = CENTDefiniton1.GetInt(GetFieldObject("position", ListCENTCatalogDefinition1)) -1;
                            strText = arrField[intPosition].trim();

                            break;

                        default:    
                    }
                }
                else
                {
                    strText = CENTDefiniton1.GetText(GetFieldObject("default_value", ListCENTCatalogDefinition1)).trim();
                }
                
                /*
                 * Compare file and layout structure (disconsider default value fields)
                 */
                if (CENTLayoutSession1.GetInt(GetFieldObject("id_format", ListCENTCatalogSession1)) != LAYOUT_FORMAT_FIXED_LENGTH)
                {                
                    intNumberOfMappedFields --; // Disregard field identifier
                    
                    if (intNumberOfMappedFields > arrField.length)
                    {
                        throw new CENTException("EXCEPTION_LAYOUT_DEFFERENCE_FILE");
                    }
                }

                /*
                 * Read the data
                 */
                intIdLayout = CENTDefiniton1.GetInt(GetFieldObject("id_layout", ListCENTCatalogDefinition1));
                intIdField = CENTDefiniton1.GetInt(GetFieldObject("id_field", ListCENTCatalogDefinition1));
                intFieldType = this.GetFieldType(intIdField, ListCENTCatalogTransaction1);
                strFieldObject = this.GetFieldObject(intIdField, ListCENTCatalogTransaction1);
                strMask = CENTDefiniton1.GetText(GetFieldObject("mask", ListCENTCatalogDefinition1));
                
                /*
                 * Apply functions in the values
                 */
                strText = ApplyLayoutFunction(intIdField, strText);
                
                /*
                 * Apply functions in the values
                 */
                strText = ApplyLayoutLookUp(intIdLayout, intIdField, strText); 
                
                /*
                 * Apply functions in the values
                 */                
                if (strText.equals(FIELD_REFUSED))
                {
                    CENTData1.SetInt(SYSTEM_FIELD_ID_LAYOUT_LINE_STATE, LINE_STATE_REFUSE);
                }

                /*
                 * Set the information according to the data type
                 */                
                if (!strText.equals(FIELD_UNIFIELD) && !strText.equals(FIELD_REFUSED))
                {
                    switch (intFieldType)
                    {
                        case TYPE_INT:                            
                            CENTData1.SetInt(strFieldObject, StringToInteger(strText));
                            break;

                        case TYPE_TEXT:
                            CENTData1.SetText(strFieldObject, StringToString(strText));
                            break;                            

                        case TYPE_DATE:
                            CENTData1.SetDate(strFieldObject, StringToDate(strText, strMask));
                            break;
                            
                        case TYPE_DOUBLE:          
                            CENTData1.SetDouble(strFieldObject, StringToDouble(strText, strMask));
                            break;

                        case TYPE_BOOLEAN:                                        
                            CENTData1.SetBoolean(strFieldObject, StringToInteger(strText));
                            break;

                        default:
                            throw new CENTException("EXCEPTION_INVALID_DATA_TYPE");
                            
                    } // Switch Data Type
                }
            }
                        
            /*
             * Join the fields according to the function
             */
            for (CENTData CENTFieldJoin1 : ListCENTJoinField1)
            {
                strText = "";
                
                for (i=3; i<=TABLE_FIELD_COUNT; i++) // Position 1 and 2 are reserved for destination field (see ApplyLayoutFunction function)
                {
                    /*
                     * Concatenate the values
                     */                
                    intFieldType = CENTFieldJoin1.GetInt(i);
                    strField = CENTFieldJoin1.GetText(i);

                    if (!strField.trim().equals(""))
                    {
                        switch (intFieldType)
                        {
                            case TYPE_INT:
                                strText += String.valueOf(CENTData1.GetInt(strField));
                                break;

                            case TYPE_TEXT:
                                if (this.GetFieldId(strField, ListCENTCatalogTransaction1) != 0)
                                    strText += CENTData1.GetText(strField); // Transaction's field
                                else
                                    strText += strField.trim(); // Special char    
                                break;

                            case TYPE_DATE:
                                strText += CENTData1.GetText(strField);
                                break;

                            case TYPE_DOUBLE:
                                strText += String.valueOf(CENTData1.GetDouble(strField));
                                break;                                                
                        }
                    }
                }                
                
                /*
                 * Update the final value
                 */                
                if (!strText.trim().equals(""))
                {                    
                    intFieldType = CENTFieldJoin1.GetInt(1);
                    strFieldObject = CENTFieldJoin1.GetText(1);
                    intIdField = CENTFieldJoin1.GetInt(2);

                    for (CENTData CENTDefiniton1 : ListCENTLayoutDefinition1)
                    {
                        if (CENTDefiniton1.GetInt(GetFieldObject("id_field", ListCENTCatalogDefinition1)) == intIdField)
                        {
                            strMask = CENTDefiniton1.GetText(GetFieldObject("mask", ListCENTCatalogDefinition1));
                            break;
                        }
                    }    
                    
                    /*
                     * Set the information according to the data type
                     */                
                    switch (intFieldType)
                    {
                        case TYPE_INT:                            
                            CENTData1.SetInt(strFieldObject, StringToInteger(strText));
                            break;

                        case TYPE_TEXT:
                            CENTData1.SetText(strFieldObject, StringToString(strText));
                            break;                            

                        case TYPE_DATE:
                            CENTData1.SetDate(strFieldObject, StringToDate(strText, strMask));
                            break;
                            
                        case TYPE_DOUBLE:          
                            CENTData1.SetDouble(strFieldObject, StringToDouble(strText, strMask));
                            break;

                        case TYPE_BOOLEAN:                                        
                            CENTData1.SetBoolean(strFieldObject, StringToInteger(strText));
                            break;

                        default:
                            throw new CENTException("EXCEPTION_INVALID_DATA_TYPE");

                    } // Switch Data Type
                    
                } // Text diff empty
                
            } // Join list
            
            
            
            
            return CENTData1;
        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }       
        finally
        {
            strText  = null;
            strFieldObject = null;
            arrField = null;
        }    
    }    

    private String GetText(CENTData CENTLayoutDefiniton1, String strLine) throws Exception
    {
        int intX = 0;
        int intY = 0;
        String strText = "";

        try
        {
            intX = CENTLayoutDefiniton1.GetInt(GetFieldObject("position", ListCENTCatalogDefinition1))-1;
            intY = intX + CENTLayoutDefiniton1.GetInt(GetFieldObject("size", ListCENTCatalogDefinition1));
            
            
            if (intY > strLine.length())
            {
                intY = strLine.length();
            }

            strText = strLine.substring(intX, intY).trim();

            return strText.trim();
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }     
    
    public String ApplyLayoutFunction(int intIdField, String strText) throws Exception
    {
        /*
         * General Declaration
         */
        final int LAYOUT_FUNCTION_ID_FIELD = 4;
        final int LAYOUT_FUNCTION_ID_FUNCTION = 5;
        final int LAYOUT_FUNCTION_PARAMETER_1 = 1;
        final int LAYOUT_FUNCTION_PARAMETER_2 = 2;        

        int i = 0;
        int j = 0;        
        int intIdFunction = 0;
        int intSize = 0;
        int intParameter1 = 0;
        int intParameter2 = 0;
        
        String strParameter1 = "";
        String strParameter2 = "";
        String arrField[];
        CENTData CENTJoinField1 = null;
        
        try
        {

            for (CENTData CENTLayoutFunction1 : ListCENTLayoutFunction1)
            {
                if (intIdField == CENTLayoutFunction1.GetInt(LAYOUT_FUNCTION_ID_FIELD))
                {
                    intIdFunction = CENTLayoutFunction1.GetInt(LAYOUT_FUNCTION_ID_FUNCTION);
                    strParameter1 = CENTLayoutFunction1.GetText(LAYOUT_FUNCTION_PARAMETER_1);
                    strParameter2 = CENTLayoutFunction1.GetText(LAYOUT_FUNCTION_PARAMETER_2);

                    /*
                     * Apply the function
                     */
                    switch (intIdFunction)
                    {
                        // Replace()
                        case 1:
                            strText = strText.replace(strParameter1, strParameter2);
                            break;

                        // Mid()    
                        case 2:
            
                            intSize = strText.length();
                            intParameter1 = Integer.valueOf(strParameter1)-1;
                            intParameter2 = Integer.valueOf(strParameter2)-1;
                            
                            if (intParameter2 > intSize)
                            {
                                intParameter2 = intSize;
                            }                            
                            
                            strText = strText.substring(intParameter1, intParameter2);
                            break;

                        // Right()    
                        case 3:
                            strText = this.Right(strText, Integer.valueOf(strParameter1));
                            break;

                        // Left()    
                        case 4:
                            strText = this.Left(strText, Integer.valueOf(strParameter1));
                            break;
                            
                        // Unir()    
                        case 5:

                            /*
                             * Add the field that will receive the concatenation
                             */                                                        
                            j = 1;
                            CENTJoinField1 = new CENTData();
                            CENTJoinField1.SetText(j, GetFieldObject(intIdField, ListCENTCatalogTransaction1)); // Field Object
                            CENTJoinField1.SetInt(j, GetFieldType(intIdField, ListCENTCatalogTransaction1));    // Field Type
                            CENTJoinField1.SetInt(++j, intIdField);                                             // Field Id

                            /*
                             * Add the field composition
                             */
                            arrField = strParameter1.split(strParameter2);
                            
                            if (arrField != null)
                            {
                                for (i=0; i<=arrField.length-1; i++)
                                {
                                    /*
                                     * If the value is not found at catalogs, understand as a special char to concatenate
                                     */
                                    j++;
                                    CENTJoinField1.SetText(j, GetFieldObject(arrField[i], ListCENTCatalogTransaction1));
                                    CENTJoinField1.SetInt(j, GetFieldType(arrField[i], ListCENTCatalogTransaction1));
                                    
                                    if (CENTJoinField1.GetText(j).equals(""))
                                    {
                                        CENTJoinField1.SetText(j, arrField[i]);
                                        CENTJoinField1.SetInt(j, TYPE_TEXT);
                                    }
                                }
                            }
                            
                            ListCENTJoinField1.add(CENTJoinField1);
                            
                            strText = FIELD_UNIFIELD;

                            break;
                            
                            
                        default:
                    }
                }
            }       
            
            return strText;
        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strParameter1 = null;
            strParameter2 = null;
        }
    }    

    public String ApplyLayoutLookUp(int intIdLayout, int intIdField, String strText) throws Exception
    {        
        /*
         * General Declaration
         */        
        int intIdRefuse = 0;
        int intIdLookUp = 0;        
        boolean boolFound = false;
        
        try
        {            
            /*
             * First, try to find lookups related to layoyt/fields, them, try to translate the item
             */
            for (CENTData CENTLayoutLookUp1 : ListCENTLayoutLookUp1)
            {
                if (intIdLayout == CENTLayoutLookUp1.GetInt(GetFieldObject("id_layout", ListCENTCatalogLayoutLookup1)) && intIdField == CENTLayoutLookUp1.GetInt(GetFieldObject("id_field", ListCENTCatalogLayoutLookup1)))
                {
                    intIdLookUp = CENTLayoutLookUp1.GetInt(GetFieldObject("id_lookup", ListCENTCatalogLayoutLookup1));
                    intIdRefuse = CENTLayoutLookUp1.GetInt(GetFieldObject("id_refuse", ListCENTCatalogLayoutLookup1));
                    boolFound = false;
                    
                    for (CENTData CENTLookUpItem1 : ListCENTLayoutLookUpItem1)
                    {                    
                        if (intIdLookUp == CENTLookUpItem1.GetInt(GetFieldObject("id_lookup", ListCENTCatalogLayoutLookupItem1)))
                        {                    
                            if (strText.trim().equals(CENTLookUpItem1.GetText(GetFieldObject("code", ListCENTCatalogLayoutLookupItem1))))
                            {
                                strText = CENTLookUpItem1.GetText(GetFieldObject("description", ListCENTCatalogLayoutLookupItem1)).trim();
                                boolFound = true;
                                break;
                            }
                        }
                    }                    

                    /*
                     * If refuse, line wont be imported, otherwise, import the same value
                     */
                    if (boolFound == false)
                    {
                        if (intIdRefuse == Yes)
                        {
                            strText = FIELD_REFUSED;
                        }
                    }                                            
                }
            }
            

            
            return strText;
        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {

        }
    }        

    private List<CENTData> GetLayoutDefinition(String strSessionIdentifier, List<String> ListCode1, List<List<CENTData>> ListListCENTData1)
    {
        /*
         * General Declaration
         */        
        int i = 0;
        int j = 0;
        int x = ListCode1.size()-1;
        
        boolean blnFound = false;
        
        List<CENTData> ListCENTData1 = null;
        
        try
        {
            for (i=0; i<=x; i++)
            {
                blnFound = false;
                if (ListCode1.get(i).equals(strSessionIdentifier))
                {
                    blnFound = true;
                    break;
                }
            }

            if (blnFound == true)
            {
                ListCENTData1 = ListListCENTData1.get(i);
            }
            else
            {
                ListCENTData1 = new ArrayList<CENTData>();
            }
                        
            return ListCENTData1;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListCENTData1 = null;
        }        
    }        

    public CENTData GetFieldMapping(int intIdTransaction, int intIdField, List<CENTData> ListCENTDefinition1) throws Exception
    {        
        /*
         * General Declaration
         */
        String strFieldObject1 = "";
        String strFieldObject2 = "";
        
        try
        {
            
            strFieldObject1 = GetFieldObject("id_transaction", ListCENTCatalogDefinition1);
            strFieldObject2 = GetFieldObject("id_field", ListCENTCatalogDefinition1);
            
            /*
             * Check if the fields (from view generally) is mapped for current layout
             */
            for (CENTData CENTData1 : ListCENTDefinition1)
            {
                if (CENTData1.GetInt(strFieldObject1) == intIdTransaction && CENTData1.GetInt(strFieldObject2) == intIdField)
                {
                    return CENTData1;
                }
            }       
            
            return null;
        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strFieldObject1 = null;
            strFieldObject2 = null;
        }
    }
        
    public String FormatValueToExport(String strValue, CENTData CENTMapping1) throws Exception
    {        
        /*
         * General Declaration
         */
        int intIdFillSide = 0;
        int intIdType = 0;
        int intRepeat = 0;
        int intSizeMapped = 0;
        int intSizeValue = 0;
        
        String strChar = "";
        String strFillChar = "";    
        String strDelimiter = "";

        try
        {
            /*
             * Read the main configuration
             */            
            intIdType = CENTMapping1.GetInt(GetFieldObject("id_type", ListCENTCatalogDefinition1));
            intSizeMapped = CENTMapping1.GetInt(GetFieldObject("size", ListCENTCatalogDefinition1));
            intIdFillSide = CENTMapping1.GetInt(GetFieldObject("id_fill_side", ListCENTCatalogDefinition1)); // 1(Right) 2 (Left) see domain_25
            strFillChar = CENTMapping1.GetText(GetFieldObject("fill_char", ListCENTCatalogDefinition1));
            strDelimiter = ListCENTLayoutDefinition1.get(0).GetText(GetFieldObject("delimiter", ListCENTCatalogDefinition1));                    
            intSizeValue = strValue.length();            
            
            /*
             * Make sure it can fill the string with empty spaces
             */
            if (intIdFillSide != LAYOUT_ALIGN_NONE)
            {
                if (strFillChar.trim().equals(""))
                {
                    strFillChar = " ";
                }
            }
            
            /*
             * Apply the format (or not) according to the layout type
             */            
            switch (intIdType)
            {
                case LAYOUT_FORMAT_DELIMITED:
                    
                    strValue += strDelimiter;
                    break;
                    
                case LAYOUT_FORMAT_FIXED_LENGTH:
                    
                    /*
                     * Fit the string at the mapping limits
                     */
                    if (intSizeValue > intSizeMapped)
                    {
                        strValue = strValue.substring(0, intSizeMapped);
                    }

                    /*
                     * Fill the string according to the mapping
                     */
                    if (intSizeValue < intSizeMapped)
                    {                
                        /*
                         * First, prepare the string sequence to fill the difference
                         */                    
                        intRepeat = (intSizeMapped - intSizeValue);

                        for (int i=1; i<=intRepeat; i++)
                        {
                            strChar += strFillChar;
                        }                    

                        /*
                         * Then, concatenate the difference
                         */
                        switch (intIdFillSide)
                        {
                            case LAYOUT_ALIGN_RIGHT:
                                strValue += strChar;
                                break;
                                
                            case LAYOUT_ALIGN_LEFT:
                                strValue = strChar + strValue;
                                break;
                        }                        
                    }
                                        
                    break;
                    
                case LAYOUT_FORMAT_SEQUENTIAL:
                    
                    break;
            }
            
            return strValue;
        }    
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strChar = "";
            strFillChar = "";    
            strDelimiter = "";
        }
    }    
    
    private String GetHeaderToExport(List<CENTData> ListCENTView1, List<CENTData> ListCENTDefinition1) throws Exception
    {
        /*
         * General Declaration
         */
        int intFieldIdTrn = 0;
        int intFieldId = 0;
        String strHeader = "";
        CENTData CENTMapping1 = null;
        
        try
        {                
            for (CENTData CENTView1 : ListCENTView1)
            {                
                if (CENTView1.GetInt(FIELD_ID_COMMAND) <= SELECTABLE_FIELD) // Discard group 
                {   
                    intFieldIdTrn = CENTView1.GetInt(FIELD_ID_TRN);
                    intFieldId = CENTView1.GetInt(FIELD_ID);

                    CENTMapping1 = GetFieldMapping(intFieldIdTrn, intFieldId, ListCENTDefinition1);

                    if (CENTMapping1 != null)
                    {                        
                        strHeader += FormatValueToExport(this.Translate(ListCENTDictionary1, CENTView1.GetText(FIELD_LABEL)), CENTMapping1);
                    }
                }
            }
            
            /*
             * Return the header
             */
            return strHeader + lb;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strHeader = "";
            CENTMapping1 = null;       
            ListCENTView1 = null;
        }        
    }
    
    private String GetLineToExport(CENTData CENTData1, List<CENTData> ListCENTDefinition1) throws Exception
    {
        /*
         * General Declaration
         */
        int intFieldIdTrn = 0;
        int intFieldId = 0;
        int intFieldType = 0;
               
        String strValue = "";
        String strFieldName = "";
        String strFieldObject = "";        
        String strLine = "";
        
        CENTData CENTMapping1 = null;
        CBUSCore CBUSCore1 = null;
        
        try
        {                
            
            CBUSCore1 = new CBUSCore(this.GetConnection(), this.GetSession());

            strLine = "";                

            for (CENTData CENTView1 : ListCENTView1)
            {                                    
                if (CENTView1.GetInt(FIELD_ID_COMMAND) <= SELECTABLE_FIELD) // Discard group 
                {
                    intFieldIdTrn = CENTView1.GetInt(FIELD_ID_TRN);
                    intFieldId = CENTView1.GetInt(FIELD_ID);                        
                    intFieldType = CENTView1.GetInt(FIELD_TYPE);                        
                    strFieldName = CENTView1.GetText(FIELD_NAME);
                    strFieldObject = CENTView1.GetText(FIELD_OBJECT);

                    CENTMapping1 = GetFieldMapping(intFieldIdTrn, intFieldId, ListCENTDefinition1);

                    if (CENTMapping1 != null)
                    {                                                    
                        switch (intFieldType)
                        {
                            case TYPE_INT:

                                strValue = CBUSCore1.FormatInt(CENTData1.GetInt(strFieldObject));
                                strLine += FormatValueToExport(strValue, CENTMapping1);
                                break;

                            case TYPE_TEXT:

                                strValue = CBUSCore1.FormatString(this.Translate(ListCENTDictionary1, CENTData1.GetText(strFieldObject)));  
                                strLine += FormatValueToExport(strValue, CENTMapping1);                                
                                break;

                            case TYPE_DATE:

                                if (CENTData1.GetDate(strFieldObject) != null)
                                {
                                    strValue = CBUSCore1.FormatDate(CENTData1.GetDate(strFieldObject));
                                    strLine += FormatValueToExport(strValue, CENTMapping1);
                                }
                                break;

                            case TYPE_DOUBLE:

                                strValue = CBUSCore1.FormatNumber(CENTData1.GetDouble(strFieldObject), 2);
                                strLine += FormatValueToExport(strValue, CENTMapping1);                                
                                break;
                        }
                    }
                }
            }

            strLine += lb;

            
            /*
             * Return the header
             */
            return strLine;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strValue = "";
            strFieldName = "";
            strFieldObject = "";        
            strLine = "";

            CBUSCore1 = null;
            CENTMapping1 = null;            
            CENTData1 = null;
        }        
    }    

    private String GetSessionIdentifier(int intStartPosition, int intSize, String strLine) throws Exception
    {
        String strNextSession = "";
        
        if (intStartPosition != 0 && intSize != 0)
        {
            strNextSession = strLine.substring(intStartPosition -1, intSize);
        }
        
        return strNextSession;
    }
    
    public void DeleteFile(String strFileName) throws Exception
    {                        
        File File1 = new File(strFileName);
        File1.setWritable(true);
        File1.delete();
        File1 = null;
    }

    public void RenameFile(String strFileName1, String strFileName2) throws Exception
    {                        
        File File1 = new File(strFileName1);
        File File2 = new File(strFileName2);

        if(File1.renameTo(File2))
        {
            System.out.println("Rename succesful");
        }
        else
        {
            System.out.println("Rename failed");
        }
    }


    private String GetNewFileName(String strPath) throws Exception
    {
        /*
         * General Declaration
         */
        int intSize = 0;        
        String strFileName = "";
        String strFilePath = "";
        String strChar = "";
        String strStamp = "";
        String strNewFileName = "";        
        SimpleDateFormat SimpleDateFormat1 = new SimpleDateFormat("yyyyMMdd hhmmss");
        strStamp = "[OK][" + SimpleDateFormat1.format(new Date()) + "] "; // Space required        
     
        try
        {
            intSize = strPath.length();        
            
            /*
             * Extract file name and path
             */
            for (int i = intSize; i>=0; i--)
            {
                strChar = strPath.substring(i, intSize);

                if (strChar.contains("/"))
                {
                    strFileName = strPath.substring(i+1, intSize);
                    strFilePath = strPath.substring(0, i+1);
                    break;
                }
            }

            /*
             * Stamp file name
             */
            strNewFileName += strStamp + strFileName;

            /*
             * Add to path
             */
            strFilePath += strNewFileName;

            /*
             * Return it
             */        
            return strFilePath;
        
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strFileName = null;
            strFilePath = null;
            strChar = null;
            strStamp = null;
            strNewFileName = null;
            SimpleDateFormat1 = null;
            strStamp = null;
        }
    }

    
}
