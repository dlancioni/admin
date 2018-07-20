package project;

import entity.CENTData;
import entity.CENTException;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author David Lancioni
 */
public class Global 
{    
    public final String SOLUTION_NAME = "Recon1";
    
    /*
     * Available sgdbs to connection
     */    
    public final String SERVER_ORACLE = "ORACLE";
    public final String SERVER_SQL_SERVER = "MSSQL";    
    public final String SERVER_POSTGRE = "POSTGRE";    
    
    /*
     * Identifies the solution (entry manager or reconciliation)
     */
    public final int COMPANY_TYPE_ENTRY_MANAGER = 1;    
    public final int COMPANY_TYPE_RECONCILIATION = 2;

    /*
     * Identifies the solution (entry manager or reconciliation)
     */
    public final int DOMAIN_SYSTEM = 1;    
    public final int DOMAIN_USER = 2;    
    
    /*
     * Constants related to system fields
     */
    public final String SYSTEM_FIELD_ID = "_int_1";                                 // db field - cannot be used to save information in any table
    public final String SYSTEM_FIELD_ID_COMPANY = "_int_2";                         // db field - cannot be used to save information in any table
    public final String SYSTEM_FIELD_ID_AREA = "_int_3";                            // db field - cannot be used to save information in any table
    public final String SYSTEM_FIELD_ID_PROFILE = "_int_4";                         // db field - cannot be used to save information in any table    
    public final String SYSTEM_FIELD_ID_USER = "_int_5";                            // db field - cannot be used to save information in any table
    public final String SYSTEM_FIELD_ID_TRANSACTION = "_int_6";                     // db field - cannot be used to save information in any table    
    public final String SYSTEM_FIELD_ID_VIEW = "_int_7";    
    public final String SYSTEM_FIELD_PAGING_START = "_int_8";                       // Cannot be used becouse of filters
    public final String SYSTEM_FIELD_PAGING_END = "_int_9";                         // Cannot be used becouse of filters
    public final String SYSTEM_FIELD_ID_PROCESS_ETL = "_int_10";
    public final String SYSTEM_FIELD_ID_PROCESS = "_int_11";
    public final String SYSTEM_FIELD_RECORD_COUNT = "_int_12";     
    public final String SYSTEM_FIELD_ID_MATCH = "_int_13";     
    public final String SYSTEM_FIELD_ID_LAYOUT = "_int_14";     
    public final String SYSTEM_FIELD_ID_LAYOUT_LINE_STATE = "_int_15";
    public final String SYSTEM_FIELD_ID_MATCH_STATUS = "_int_16";
   
    /*
     * Control company type on system instance creation
     */
    public final String SYSTEM_FIELD_COMPANY_TYPE = "int_6"; // db field    
    
    /*
     * Constants related to system fields (Date)
     */
    public final String SYSTEM_FIELD_SYSTEM_DATE = "_date_1"; // db field
    
    /*
     * Match fields to cross with reconciliation area, it's used to avoid joins in big volume
     */        
    public final String SYSTEM_FIELD_DATA_SOURCE = "_text_1";    
    public final String SYSTEM_FIELD_MATCH_ID = "_int_9";
    public final String SYSTEM_FIELD_MATCH_ID_PROCESS = "_int_11";
    public final String SYSTEM_FIELD_MATCH_DATE = "_date_2";
    public final String SYSTEM_FIELD_MATCH_RECONCILE = "_text_2";
    public final String SYSTEM_FIELD_MATCH_STEP = "_text_3";
    public final String SYSTEM_FIELD_MATCH_RULE = "_text_4";
    public final String SYSTEM_FIELD_MATCH_ID_STATUS = "_int_12";
    public final String SYSTEM_FIELD_MATCH_FIELD = "_text_5";
    public final String SYSTEM_FIELD_MATCH_DIFF = "_text_6";
    public final String SYSTEM_FIELD_MATCH_SIDE = "_text_7";

    /*
     * Used to access login information in the session (CENTSession1)
     */
    public final int SYSTEM_DEFAULT_COMPANY = 1; // In some situation like the login, we need to read the catalog for non-informed or invalid company
    
    public final int SESSION_COMPANY = 1;
    public final int SESSION_AREA = 2;
    public final int SESSION_USER = 3;
    public final int SESSION_PROFILE = 4;    
    public final int SESSION_LANGUAGE = 5;    
    public final int SESSION_COUNTRY = 6;    
    public final int SESSION_MENU = 7;        
    public final int SESSION_FUNCTION = 8;
    public final int SESSION_MASK_DATE = 9;
    public final int SESSION_DECIMAL_SIMBOL = 10;
    public final int SESSION_DATE = 1; 
    
     /*
     * Constants used to facilitate the job with the catalogs
     */    
    public int PK_TRUE = 1;
    public int PK_FALSE = 1;            
    public int NULLABLE_TRUE = 1;
    public int NULLABLE_FALSE = 2;    
    public int UNIQUE_TRUE = 1;
    public int UNIQUE_FALSE = 1;    
    public String NO_DOMAIN = "null";
    public int NO_FK = 0;
    
    /*
     * Administrator information
     */    
    public final int ADMINISTRATOR_COMPANY = 1;
    public final int ADMINISTRATOR_AREA = 1;
    public final int ADMINISTRATOR_PROFILE = 1;
    public final int ADMINISTRATOR_USER = 1;
                
    /*
     * Profiles
     */
    public final int PROFILE_OWNER = 1;
    public final int PROFILE_ADMIN = 2;
    public final int PROFILE_MANAGER = 3;
    public final int PROFILE_ANALYST = 4;

    /*
     * User
     */
    public final int USER_OWNER = 1;
    public final int USER_ADMIN = 2;
    public final int USER_MANAGER = 3;
    public final int USER_ANALYST = 4;    
        
    /*
     * Areas
     */
    public final int AREA_IT = 1;
    public final int AREA_FX = 2; // CC
    public final int AREA_CC = 3; // FI

    /*
     * Break line, used to concatenate SQL queries
     */
    public String lb = System.getProperty("line.separator");    
    
    /*
     * Base table, need to define it to make sure it´s flexible regarding the tables
     */    
    public final int DO_NOT_USE_VIEW = -1;

    /*
     * Base table, need to define it to make sure it´s flexible regarding the tables
     */    
    public final int MATCH_AS_ROW = 1;    
    public final int MATCH_AS_COL = 2;    
    
    /*
     * System module
     */   
    public final int TRN_ALL = 0;
    public final int TRN_TABLE = 1;
    public final int TRN_MENU = 2;
    public final int TRN_TRANSACTION = 3;
    public final int TRN_CATALOG = 4;
    public final int TRN_FIELD_EVENT = 5;
    public final int TRN_VIEW = 6;
    public final int TRN_VIEW_DEF = 7;
    public final int TRN_DOMAIN = 8;
    public final int TRN_COUNTRY = 9;
    public final int TRN_LANGUAGE = 10;
    public final int TRN_DICTIONARY = 11;
    public final int TRN_SCHEDULE = 12;
    public final int TRN_SCHEDULE_JOB = 13;
    public final int TRN_QUEUE = 14;
    public final int TRN_COMPANY = 15;
    public final int TRN_AREA = 16;
    public final int TRN_USER = 17;    
    public final int TRN_PROFILE = 18;
    public final int TRN_PROFILE_TRANSACTION = 19;
    public final int TRN_TRANSACTION_FUNCTION = 20;
    public final int TRN_FUNCTION = 21;
    public final int TRN_LAYOUT = 22;
    public final int TRN_LAYOUT_SESSION = 23;
    public final int TRN_LAYOUT_SESSION_DEFINITION = 24;
    public final int TRN_LAYOUT_LAYOUT_LOOKUP = 25;
    public final int TRN_LAYOUT_FUNCTION = 26;
    public final int TRN_LAYOUT_LOOKUP = 27;
    public final int TRN_LAYOUT_LOOKUP_ITEM = 28;
    public final int TRN_RECONCILIATION = 29;
    public final int TRN_RECONCILIATION_STEP = 30;
    public final int TRN_RECONCILIATION_STEP_RULE = 31;
    public final int TRN_RECONCILIATION_STEP_RULE_FIELD = 32;    
    public final int TRN_MATCH = 33;
    public final int TRN_MATCH_ITEM = 34;
    public final int TRN_FILE_MANAGER = 35;    
    public final int TRN_LOGOUT = 36;    
    
    public final int TRN_COUNT = 36;
        
    /*
     * System functions
     */        
    public final int FUNCTION_NEW = 1;
    public final int FUNCTION_EDIT = 2;
    public final int FUNCTION_DELETE = 3;
    public final int FUNCTION_SAVE = 4;
    public final int FUNCTION_FILTER  = 5;
    public final int FUNCTION_IMPORT = 6;
    public final int FUNCTION_EXPORT = 7;
    public final int FUNCTION_RECONCILE = 8;
    public final int FUNCTION_MAP_VIEW = 9;
    public final int FUNCTION_DUPLICATE = 10;
    public final int FUNCTION_REPROCESS = 11;    
    
    public final int FUNCTION_COUNT = 11;        
    
    /*
     * Constants related to the setup
     */
    public final int DISPLAY_NONE = 0;
    public final int DISPLAY_GRID = 1;
    public final int DISPLAY_CHART_COLUMNS = 2;
    public final int DISPLAY_CHART_LINE = 3;
    public final int DISPLAY_CHART_BAR = 4;
    public final int DISPLAY_CHART_PIE = 5;
    public final int DISPLAY_CHART_DOUGHNUT = 6;
    public final int DISPLAY_CHART_SPLINE = 7;
    public final int DISPLAY_CHART_SPLINE_AREA = 8;    

    /*
     * Menu type
     */
    public final int MENU_TYPE_SYSTEM = 1;
    public final int MENU_TYPE_USER = 2;
    
    /*
     * Transactions type
     */
    public final int TRANSACTION_TYPE_SYSTEM = 1;
    public final int TRANSACTION_TYPE_USER = 2;    
    
    /*
     * Match process, used to control the update based on the key. Remember that values use field 1 (text_1, date_1, etc)
     */    
    public final int MATCH_FIELD_KEY_NAME = 2;
    public final int MATCH_FIELD_KEY_VALUE = 1;
    public final int MATCH_FIELD_KEY_TYPE = 2;
    

    public final String TEMPORARY_1 = "TMP1"; // Copy of reconciliation area
    public final String TEMPORARY_2 = "TMP2"; // Grouped data, used to page, etc
    public final String TEMPORARY_3 = "TMP3"; // Keep the matches
    public final String TEMPORARY_4 = "TMP4"; // Keep the matches item
    public final String TEMPORARY_5 = "TMP5"; // run the match process
    public final String TEMPORARY_6 = "TMP6"; // run the match process    
    public final String TEMPORARY_7 = "TMP7"; // Used to match data
    public final String TEMPORARY_8 = "TMP8"; // Used to match data
    public final String TEMPORARY_9 = "TMP9"; // Used to match data
    public final String TEMPORARY_10 = "TMP10"; // Used to match data    

    /*
     * SQL related constants - used to prepare query
     */
    public final int SELECTABLE_FIELD = 6; // Very important fields, used to identify the selecable field in all routines    

    /*
     * Domain_3 (Sql Commands)
     */    
    public final int COMMAND_SELECT_FIELD = 1;
    public final int COMMAND_SELECT_COUNT = 2;
    public final int COMMAND_SELECT_SUM = 3;
    public final int COMMAND_SELECT_MAX = 4;
    public final int COMMAND_SELECT_MIN = 5;
    public final int COMMAND_SELECT_AVG = 6;   
    public final int COMMAND_CONDITION_AND = 7;
    public final int COMMAND_CONDITION_OR = 8;        
    public final int COMMAND_GROUP_BY = 9;
    public final int COMMAND_ORDER_BY_ASC = 10;
    public final int COMMAND_ORDER_BY_DESC = 11;    
    

    /*
     * Domain 18 (Sql Operators)
     */
    public final int OPERATOR_EQUALS = 1;
    public final int OPERATOR_NOT_EQUALS = 2;    
    public final int OPERATOR_GREATER = 3;    
    public final int OPERATOR_GREATER_EQUALS = 4;    
    public final int OPERATOR_SMALLER = 5;    
    public final int OPERATOR_SMALLER_EQUALS = 6;        
    public final int OPERATOR_IN = 7;
    public final int OPERATOR_NOT_IN = 8;  
    public final int OPERATOR_LIKE = 9;  
    public final int OPERATOR_NOT_LIKE = 10;      
    

    /*
     * Not related to domains, constants only
     */    
    public final int SQL_PART_ALL = 1;
    public final int SQL_PART_FIELD_LIST_NO_ALIAS = 2;
    public final int SQL_PART_FIELD_LIST = 3;
    public final int SQL_PART_FROM = 4;
    public final int SQL_PART_JOIN = 5;
    public final int SQL_PART_WHERE = 6;
    public final int SQL_PART_AND = 7;
    public final int SQL_PART_GROUP_BY = 8;
    public final int SQL_PART_ORDER_BY = 9;
    
    /*
     * General boolean
     */    
    public final int Yes = 1;    
    public final int No = 2;
    
    /*
     * Generic Fields
     */    
    public final String SYSTEM_DATE = SYSTEM_FIELD_SYSTEM_DATE;
    
    /*
     * Used to define the cast to return the information
     */
    public final int TYPE_INT = 1;
    public final int TYPE_TEXT = 2;
    public final int TYPE_DATE = 3;
    public final int TYPE_DOUBLE = 4;
    public final int TYPE_BOOLEAN = 5;

    /*
     * Used to control the tables and field creation
     */        
    //public final int TABLE_COUNT = 50;
    public final int TABLE_FIELD_COUNT = 20;
        
    /*
     * Used when accessing the base catalog
     */    
    public final String FIELD_ID = "_int_1";
    public final String FIELD_ID_TRN = "int_2";
    public final String FIELD_LABEL = "text_1";            
    public final String FIELD_NAME = "text_2";        
    public final String FIELD_TYPE = "int_3";
    public final String FIELD_OBJECT = "text_3";
    public final String FIELD_SIZE = "int_4";
    public final String FIELD_NULLABLE = "boolean_1";
    public final String FIELD_ID_UNIQUE = "boolean_2";    
    public final String FIELD_ID_PK = "boolean_3";
    public final String FIELD_ID_FK = "int_5";
    public final String FIELD_DOMAIN_NAME = "text_4";    
    public final String FIELD_NOTE = "text_5";

    public final String FIELD_CUSTOM_QUERY = "text_8";
    public final String FIELD_ID_COMMAND = "int_7";
    public final String FIELD_ID_OPERATOR = "int_8";
    public final String FIELD_CONDITION_VALUE = "text_9";
    
    /*
     * System Events, most related in pages
     */
    public final int EVENT_LOAD = 1;
    public final int EVENT_RELOAD = 2;
    
    public final int EVENT_NEW = 3;
    public final int EVENT_EDIT = 4;
    public final int EVENT_INSERT = 5;
    public final int EVENT_UPDATE = 6;
    public final int EVENT_DELETE = 7;
    public final int EVENT_FILTER = 8;    
    public final int EVENT_IMPORT = 9;
    public final int EVENT_EXPORT = 10;
    public final int EVENT_RECONCILE = 11;
    public final int EVENT_SETUP = 12;
    public final int EVENT_DUPLICATE = 13;
    public final int EVENT_REPROCESS = 14;    
    public final int EVENT_LOGIN = 15;

    /*
     * Sometimes it's necessary to ignore lines
     */
    public final int LINE_STATE_REFUSE = 1;    
    public final int LINE_STATE_IMPORT = 2;

    /*
     * Generic type to work with boolean fields
     */
    public final int Null = 0;
    public final int True = 1;
    public final int False = 2;    
    
    /*
     * Tolerance Type
     */
    public final int TOLERANCE_TYPE_ABSOLUTE = 1;
    public final int TOLERANCE_TYPE_PERCENTUAL = 2;
    public final int TOLERANCE_TYPE_DATE = 3;
    
    
    /*
     * Domain 10
     */
    public final int MATCH_STATUS_ORFAN = 1;
    public final int MATCH_STATUS_PROPOSED = 2;
    public final int MATCH_STATUS_MATCHED = 3;      
    
    /*
     * Queue status (domain_24)
     */
    public final int QUEUE_STATUS_PENDING = 1;
    public final int QUEUE_STATUS_RUNNING = 2;
    public final int QUEUE_STATUS_SUCCESS = 3;
    public final int QUEUE_STATUS_FAIL = 4;
    
    /*
     * Schedule type
     */
    public final int SCHEDULE_TYPE_IMPORT_FILE = 1;
    public final int SCHEDULE_TYPE_RECONCILE = 2;    
    
    
    /*
     * System Language
     */
    public final String LANGUAGE_PORTUGUESE = "pt";
    public final String LANGUAGE_ENGLISH = "en";
    public final String LANGUAGE_SPANISH = "sp";

    /*
     * Default mask to persist
     */    
    public final String C_DEFAULT_MASK_TO_PERSIST_DATE = "yyyy-MM-dd";    
    public final String C_DEFAULT_MASK_TO_PERSIST_DOUBLE = "0.00000000";
    
    /*
     * Layout types
     */
    public final int LAYOUT_TYPE_TEXT_SIMPLE = 1;
    public final int LAYOUT_TYPE_TEXT_SESSION = 2;
    public final int LAYOUT_TYPE_CONECTION = 3;    
    
    public final int LAYOUT_FORMAT_FIXED_LENGTH = 1;    
    public final int LAYOUT_FORMAT_DELIMITED = 2;
    public final int LAYOUT_FORMAT_SEQUENTIAL = 3;
    
    public final int LAYOUT_SESSION_TYPE_RECORD = 1;
    public final int LAYOUT_SESSION_TYPE_HEADER = 2;
    public final int LAYOUT_SESSION_TYPE_TRAILER = 3;
    
    public final int LAYOUT_TYPE_TEXT_FILE = 1;
    public final int LAYOUT_TYPE_DATABASE_CONNECTION = 2;
    
    
    /*
     * View def (chart)
     */    
    public final int VIEW_DEF_CHART_COLUMN = 7;
    public final int VIEW_DEF_CHART_DESC = 1;
    public final int VIEW_DEF_CHART_VALUE = 2;
    public final int VIEW_DEF_CHART_MULTIPLE_SERIE = 3;    
    
    
    /*
     * Layout align
     */
    public final int LAYOUT_ALIGN_NONE = 0;
    public final int LAYOUT_ALIGN_RIGHT = 1;
    public final int LAYOUT_ALIGN_LEFT = 2;        
    public final String LAYOUT_ALIGN_CHAR_EMPTY = "";
    
    /*
     * General Declaration
     */
    public final int QUEUE_TYPE_IMPORT_FILE = 1;
    public final int QUEUE_TYPE_RECONCILIATION = 2;
    public final int QUEUE_TYPE_SCHEDULE = 3;
    
    /*
     * Reconcile 
     */
    public final int RECONCILE_1_1 = 1;
    public final int RECONCILE_1_M = 2;    
    public final int RECONCILE_M_M = 3;    

    public final int RESULT_ALL = 1;
    public final int RESULT_DIFFERENCE = 2;
    public final int RESULT_MATCH = 3;          
    
    public final int KEY_SEARCH = 1;
    public final int COMPARE_CRITERIA = 2; 
        
    public final int TOL_TYPE_NO = 0;    
    public final int TOL_TYPE_ABSOLUT = 1;    
    public final int TOL_TYPE_PERCENTAGE = 2;    
    public final double TOL_NO = Double.MIN_VALUE;    

    /*
     * Functions that the users can type to replace for values
     */    
    public final String USER_FUNCTION_TODAY = "hoje(), today(), hoy()"; // Get system date in filters
    
    /*
     * Tables
     */
    public final String PREFIX_TABLE_SYSTEM = "system_";
    public final String PREFIX_TABLE_USER = "user_";
    
    public final int TABLE_NONE = 0;
    public final int TABLE_TABLE = 1;
    public final int TABLE_MENU = 2;
    public final int TABLE_TRANSACTION = 3;
    public final int TABLE_CATALOG = 4;
    public final int TABLE_FIELD_EVENT = 5;
    public final int TABLE_VIEW = 6;
    public final int TABLE_VIEW_DEF = 7;
    public final int TABLE_DOMAIN = 8;
    public final int TABLE_COUNTRY = 9;
    public final int TABLE_LANGUAGE = 10;
    public final int TABLE_DICTIONARY = 11;
    public final int TABLE_SCHEDULE = 12;
    public final int TABLE_SCHEDULE_JOB = 13;
    public final int TABLE_QUEUE = 14;
    public final int TABLE_COMPANY = 15;
    public final int TABLE_AREA = 16;
    public final int TABLE_USER = 17;
    public final int TABLE_PROFILE = 18;
    public final int TABLE_PROFILE_TRANSACTION = 19;
    public final int TABLE_TRANSACTION_FUNCTION = 20;
    public final int TABLE_FUNCTION = 21;
    public final int TABLE_LAYOUT = 22;
    public final int TABLE_LAYOUT_SESSION = 23;
    public final int TABLE_LAYOUT_SESSION_DEFINITION = 24;
    public final int TABLE_LAYOUT_LAYOUT_LOOKUP = 25;
    public final int TABLE_LAYOUT_FUNCTION = 26;
    public final int TABLE_LAYOUT_LOOKUP = 27;
    public final int TABLE_LAYOUT_LOOKUP_ITEM = 28;
    public final int TABLE_RECONCILE = 29;
    public final int TABLE_RECONCILE_STEP = 30;
    public final int TABLE_RECONCILE_STEP_RULE = 31;
    public final int TABLE_RECONCILE_STEP_RULE_FIELD = 32;
    public final int TABLE_MATCH = 33;
    public final int TABLE_MATCH_ITEM = 34;
    
    public final int TABLE_USER_1 = 37;
    public final int TABLE_USER_2 = 38;
    public final int TABLE_USER_3 = 39;
    public final int TABLE_USER_4 = 40;    
    public final int TABLE_USER_5 = 41;    
    public final int TABLE_USER_6 = 42;
    public final int TABLE_USER_7 = 43;
    public final int TABLE_USER_8 = 44;
    public final int TABLE_USER_9 = 45;    
    public final int TABLE_USER_10 = 46;    
    
    /*
     * Menus
     */    
    public final int MENU_SETUP = 1;
        public final int MENU_ACCESS_CONTROL = 2;
        public final int MENU_TRANSACTION = 3;
        public final int MENU_DICTIONARY = 4;
        public final int MENU_SCHEDULE = 5;
        
    public final int MENU_ETL = 6;
        public final int MENU_ETL_LAYOUT = 7;
        public final int MENU_ETL_TRANSLATION = 8;
        
    public final int MENU_RECONCILIATION = 9;
        public final int MENU_RECONCILIATION_SETUP = 10;
        
    public final int MENU_RA = 11;
    
    public final int MENU_ENTRY = 12;
        public final int MENU_ENTITY = 13;

    public final int MENU_SESSION = 14;
    
    /*
     * Methods used to convert STRINGS
     */
    public String StringToString(String strValue)
    {
        if (strValue == null)
            strValue = "";
        
        return strValue.trim();
    }
    
    public int StringToInteger(String strValue) throws CENTException
    {
        try
        {
            if (strValue == null)
                strValue = "";
            
            if (strValue.trim().equals(""))
                return 0;
            else        
                return Integer.parseInt(strValue);
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_INTEGER", Exception1.getMessage());
        }
        
    }    

    /*
     * Methods used to convert DATES
     */    
    public String DateToString(Date datDate) throws CENTException
    {
        /*
         * General Declaration
         */
        String strMask = "";
        DateFormat DateFormat1 = null;
        String strLanguage = "";
        
        try
        {
            /*
             * Define the format according to the language
             */
            strLanguage = this.CENTSession1.GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals(LANGUAGE_PORTUGUESE))
                strMask = "dd/MM/yyyy";
            if (strLanguage .equals(LANGUAGE_ENGLISH))
                strMask = "MM/dd/yyyy";                    
            if (strLanguage .equals(LANGUAGE_SPANISH))
                strMask = "dd/MM/yyyy";            
            
            DateFormat1 = new SimpleDateFormat(strMask);
            DateFormat1.setLenient(false); // throw exception when invalid date is typed
            
            /*
             * Parse the date
             */                        
            return DateFormat1.format(datDate);
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DATE", Exception1.getMessage());
        }
    }  

    public String DateToString(Date datDate, String strMask) throws CENTException
    {
        /*
         * General Declaration
         */
        String strDate = "";
        DateFormat DateFormat1 = null;
        
        try
        {
            /*
             * Define the format according to the language
             */            
            DateFormat1 = new SimpleDateFormat(strMask);
            DateFormat1.setLenient(false); // throw exception when invalid date is typed
            
            /*
             * Parse the date
             */            
            strDate = DateFormat1.format(datDate);  
            
            return strDate;  
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DATE", Exception1.getMessage());
        }
    }            
    
    public Date StringToDate(String strValue) throws CENTException
    {
        /*
         * General Declaration
         */
        Date datDate = null;
        String strMask = "";
        DateFormat DateFormat1 = null;
        String strLanguage = "";
        
        try
        {           
            /*
             * Define the format according to the language
             */
            strLanguage = this.CENTSession1.GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals(LANGUAGE_PORTUGUESE))
                strMask = "dd/MM/yyyy";
            if (strLanguage .equals(LANGUAGE_ENGLISH))
                strMask = "MM/dd/yyyy";                    
            if (strLanguage .equals(LANGUAGE_SPANISH))
                strMask = "dd/MM/yyyy";            
         
            /*
             * Function to return current date
             */
            if (strValue.equalsIgnoreCase("date()"))
            {
                strValue = DateToString(new Date(), strMask);            
            }

            /*
             * Convert string to date
             */            
            DateFormat1 = new SimpleDateFormat(strMask);
            DateFormat1.setLenient(false); // throw exception when invalid date is typed
            
            if (USER_FUNCTION_TODAY.contains(strValue.toLowerCase().trim()))
            {
                datDate = this.CENTSession1.GetDate(SESSION_DATE);
            }
            else
            {
                datDate = (Date) DateFormat1.parse(strValue);                
            }            
            
            return datDate;                
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DATE", Exception1.getMessage());
        }
    }

    public Date StringToDate(String strValue, String strMask) throws CENTException
    {
        /*
         * General Declaration
         */
        Date datDate = null;
        DateFormat DateFormat1 = null;
        
        try
        {
            if (strValue.trim().equals(""))
            {
                /*
                 * Define the format according to the language
                 */            
                strValue = "19000101";
                strMask = "yyyyMMdd";
                DateFormat1 = new SimpleDateFormat(strMask);
                DateFormat1.setLenient(false); // throw exception when invalid date is typed
                
                /*
                 * Parse the date
                 */            
                datDate = (Date) DateFormat1.parse(strValue);
            }
            else
            {
            
                /*
                 * Define the format according to the language
                 */            
                DateFormat1 = new SimpleDateFormat(strMask);
                DateFormat1.setLenient(false); // throw exception when invalid date is typed

                /*
                 * Parse the date
                 */            
                datDate = (Date) DateFormat1.parse(strValue);  
            }
            
            return datDate;                
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DATE", Exception1.getMessage() + " : " + strValue + "->" + strMask);
        }
    }            
    
    /*
     * Methods used to convert DOUBLE
     */    
    public double StringToDouble(String strValue) throws CENTException
    {
        
        int i = 0;
        int intSize = 0;
        
        String strMask = "";
        String strAux = "";
        String strInteger = "";
        String strDecimal = "";
        String strLanguage = "";
        Double dblRet = 0.0;        
        
        try
        {        
            /*
             * Validate
             */            
            if (strValue.trim().equals(""))
            {
                return 0;
            }            
            
            /*
             * Define the format according to the language
             */
            strLanguage = CENTSession1.GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals(LANGUAGE_PORTUGUESE))
                strMask = ",";
            if (strLanguage.equals(LANGUAGE_ENGLISH))
                strMask = ".";                    
            if (strLanguage.equals(LANGUAGE_SPANISH))
                strMask = ",";                        
            

            /*
             * Identify the decimal point position
             */
            intSize = strValue.length();
            
            for (i=intSize; i>0; i--)
            {                
                strAux = strValue.substring(i-1, i).trim();

                if (strAux.equals(strMask))
                {
                    break;
                }
            }
            
            /*
             * Apply rules
             */            
            if (i > 0)
            {    
                strInteger = strValue.substring(0, i - 1);
                strDecimal = strValue.substring(i, intSize);            
                strInteger = strInteger.replace(",", "").replace(".", "");
                strValue = strInteger + "." + strDecimal;                
            }
            else
            {
                strValue = strValue.replace(".", "");
                strValue = strValue.replace(",", "");
            }
            
            /*
             * Parse the value
             */            
            dblRet = Double.parseDouble(strValue);
            
            /*
             * Return it
             */            
            return dblRet;
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DATE");
        }
    }
    
    public String DoubleToString(double dblValue, String strMask) throws Exception
    {
        /*
         * General Declaration
         */            
        String strValue = "";
        String strMaskDecimalFormat = "";
        
        /*
         * Apply final mask
         */
        strMaskDecimalFormat = strMask;
        strMaskDecimalFormat = strMaskDecimalFormat.replaceAll(",", ".");
        strMaskDecimalFormat = strMaskDecimalFormat.replaceAll("0", "#");
        DecimalFormat DecimalFormat1 = new DecimalFormat(strMaskDecimalFormat);

        strValue = DecimalFormat1.format(dblValue).replaceAll(",", ".");

        return strValue;
    }     
    
    /*
     * Methods used to convert INTEGER AND BOOLEAN
     */        
    public int StringToBoolean(String strValue)
    {
        if (strValue.trim().equals(""))
            return 0;
        else        
            return Integer.parseInt(strValue);
    }    

    /*
     * Used to remove the last char at the string
     */    
    public String RemoveCharOnTheLeft(String strValue, int intNumberOfChar)
    {
        if (strValue.length() > 0)
            return strValue.substring(0, strValue.length() - intNumberOfChar);
        else
            return "";
    }
    
    public String RemoveCharOnTheRight(String strValue, int intNumberOfChar)
    {
        if (strValue.length() > 0)
            return strValue.substring(intNumberOfChar, strValue.length());
        else
            return "";
    }    
    
    /*
     * Properties used to manipulate view and catalog
     */
    private int id_transaction;
    private int id_view;
    private int id_event;    
    
    
    public int GetIdView() 
    {
        return id_view;
    }

    public void SetIdView(int intIdview) 
    {
        this.id_view = intIdview;
    }        
    
    public int GetIdTransaction() 
    {
        return id_transaction;
    }

    public void SetIdTransaction(int intIdTransaction) 
    {
        this.id_transaction = intIdTransaction;
    }    
            
    public int GetIdEvent() 
    {
        return id_event;
    }

    public void SetIdEvent(int id_Event) 
    {
        this.id_event = id_Event;
    }
    
    /*
     * Methods used to maniputate the catalogs
     */
    public String GetFieldObject(String strField, List<CENTData> ListCENTCatalog1) throws Exception
    {
        /*
         * General Declaration
         */      
        String strFieldObject = "";

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetText(FIELD_NAME).trim().equals(strField.trim()))
                {
                    strFieldObject = CENTData1.GetText(FIELD_OBJECT).trim();
                    break;
                }                
            }

            return strFieldObject.trim();

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strFieldObject = null;
            ListCENTCatalog1 = null;
        }
    }        
    
    public String GetFieldObject(int intIdField, List<CENTData> ListCENTCatalog1) throws Exception
    {    
        /*
         * General Declaration
         */      
        String strFieldObject = "";

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetInt(FIELD_ID) == intIdField)
                {
                    strFieldObject = CENTData1.GetText(FIELD_OBJECT).trim();
                    break;
                }                
            }

            return strFieldObject;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strFieldObject = null;
            ListCENTCatalog1 = null;
        }
    }

    public String GetFieldLabel(int intIdField, List<CENTData> ListCENTCatalog1) throws Exception
    {    
        /*
         * General Declaration
         */      
        String strFieldObject = "";

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetInt(FIELD_ID) == intIdField)
                {
                    strFieldObject = CENTData1.GetText(FIELD_LABEL).trim();
                    break;
                }                
            }

            return strFieldObject;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strFieldObject = null;
            ListCENTCatalog1 = null;
        }
    }    
    
    public int GetFieldType(int intIdField, List<CENTData> ListCENTCatalog1) throws Exception
    {    
        /*
         * General Declaration
         */      
        int intFieldType = 0;

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetInt(FIELD_ID) == intIdField)
                {
                    intFieldType = CENTData1.GetInt(FIELD_TYPE);
                    break;
                }                
            }

            return intFieldType;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListCENTCatalog1 = null;
        }
    }

    public int GetFieldId(String strField, List<CENTData> ListCENTCatalog1) throws Exception
    {    
        /*
         * General Declaration
         */      
        int intFieldId = 0;

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetText(FIELD_LABEL).trim().equals(strField.trim()) || CENTData1.GetText(FIELD_OBJECT).trim().equals(strField.trim()) || CENTData1.GetText(FIELD_NAME).trim().equals(strField.trim()))
                {
                    intFieldId = CENTData1.GetInt(FIELD_ID);
                    break;
                }                
            }

            return intFieldId;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListCENTCatalog1 = null;
        }
    }
        
    public int GetFieldType(String strField, List<CENTData> ListCENTCatalog1) throws Exception
    {    
        /*
         * General Declaration
         */      
        int intFieldType = 0;

        try
        {    
            for (CENTData CENTData1 : ListCENTCatalog1)
            {
                if (CENTData1.GetText(FIELD_OBJECT).trim().equals(strField.trim()) || CENTData1.GetText(FIELD_NAME).trim().equals(strField.trim()))
                {
                    intFieldType = CENTData1.GetInt(FIELD_TYPE);
                    break;
                }                
            }

            return intFieldType;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListCENTCatalog1 = null;
        }
    }

    /*
     * General methods
     */    
    public boolean isPermissioned(String strFunction, List<CENTData> ListCENTPermission1) throws Exception
    {    
        /*
         * General Declaration
         */      

        try
        {    
            for (CENTData CENTData1 : ListCENTPermission1)
            {
                if (CENTData1.GetText(1).equals(strFunction))
                {
                    return true;
                }                
            }

            return false;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            ListCENTPermission1 = null;
        }
    }
    
    public String Left(String strValue, int intSize) throws Exception
    {
        /*
         * Left(0123456789, 5) returns 01234 (0 to position)
         */
        String strRet = "";
        
        if (intSize <= 0)
        {
            strRet = strValue.trim();
        }    
        else if (intSize > strValue.length())
        {
            strRet = strValue.trim();
        }
        else
        {
            strRet = strValue.substring(0, intSize);
        }
        
        return strRet.trim();
    }
    
    public String Right(String strValue, int intPosition) throws Exception
    {
        /*
         * Right(0123456789, 5) returns 56789 (Position to Length)
         */
        int intLen = 0;
        String strRet = "";
        
        if (intPosition <= 0)
        {
           strRet = strValue;
        }   
        else if (intPosition > strValue.length())
        {
           strRet = strValue;
        }   
        else 
        {
           intLen = strValue.length();
           strRet = strValue.substring((intLen-intPosition), intLen);
        }
        
        return strRet.trim();
    }        
    
    public boolean isMappeableField(String strFieldObject)
    {
        boolean blnRet = true;        
        
        if (strFieldObject.contains("_int_"))
        {
            blnRet = false;
        }
        
        if (strFieldObject.contains("_text_"))
        {
            blnRet = false;
        }        
        
        if (strFieldObject.contains("_date_"))
        {
            blnRet = false;
        }                
        
        if (strFieldObject.contains("_double_"))
        {
            blnRet = false;
        }                
        
        if (strFieldObject.contains("_boolean_"))
        {
            blnRet = false;
        }                
        
        return blnRet;
        
    }    

    public boolean isSystemField(String strFieldObject)
    {
        boolean blnRet = false;        
        
        if (strFieldObject.equals(SYSTEM_FIELD_ID))
        {
            return false;
        }        
        
        if (strFieldObject.contains("_int_"))
        {
            blnRet = true;
        }
        
        if (strFieldObject.contains("_text_"))
        {
            blnRet = true;
        }        
        
        if (strFieldObject.contains("_date_"))
        {
            blnRet = true;
        }                
        
        if (strFieldObject.contains("_double_"))
        {
            blnRet = true;
        }                
        
        if (strFieldObject.contains("_boolean_"))
        {
            blnRet = true;
        }                
        
        return blnRet;
        
    }    


    
    public String GetPropertieValue(String strPropertieName) throws Exception
    {

        String strValue = "";
        Properties Properties1 = new Properties();
        
        try 
        {
            /*
             * Load a properties file from class path, inside static method
             */           
            Properties1.load(this.getClass().getResourceAsStream("/config/core.properties"));
            
            /*
             * Get the property value and print it out
             */            
            strValue = Properties1.getProperty(strPropertieName);
            
            /*
             * Return it
             */                        
            return strValue;
        }               
        catch (Exception Exception1) 
        {
            throw Exception1;
        }
        finally
        {
            Properties1 = null;
            strValue = null;
        }
    }

    
    
    public void Log(String strMessage) throws IOException, Exception 
    {  
        /*
         * General Declaration
         */
        String strLogFile = "";
        Logger Logger1 = Logger.getLogger("Logger1");  
        FileHandler FileHandler1;  

        try 
        {  
            /*
             * Generate the log file
             */
           strLogFile = this.GetPropertieValue("LOG_FOLDER") + this.getClass().getName() + ".txt";
            
            /*
             * Create it
             */           
            //FileHandler1 = new FileHandler(strLogFile);  
            //Logger1.addHandler(FileHandler1);
            //SimpleFormatter formatter = new SimpleFormatter();  
            //FileHandler1.setFormatter(formatter);  

            /*
             * Write log info
             */
            //Logger1.info(strMessage);  

        } 
        catch (SecurityException e) 
        {  
            e.printStackTrace();  
        } 
        catch (IOException e) 
        {  
            e.printStackTrace();  
        }  
        finally
        {
            strLogFile = null;
            Logger1 = null;  
            FileHandler1 = null;              
        }

    }    

    
    /*
     * Methods related to the session or public information
     */
    protected CENTData CENTSession1;

    /*
     * Get the connection according to defined database 
     */    
    protected Connection Connection1 = null;        
    
    public void SendMail(String strTo, String strSubject, String strBody) throws Exception, CENTException
    {
        /*
         * General Declaration
         */
        Session Session1 = null;
        MimeMessage MimeMessage1 = null;
        Properties Properties1 = null;        

        /*
         * E-mail connection information
         */
        String strActivated = this.GetPropertieValue("MAIL_ACTIVATED");
        String strServer = this.GetPropertieValue("MAIL_SERVER");
        String strPort = this.GetPropertieValue("MAIL_PORT");
        String strFrom = this.GetPropertieValue("MAIL_FROM");
        final String strUsername = this.GetPropertieValue("MAIL_USERNAME");
        final String strPassword = this.GetPropertieValue("MAIL_PASSWORD");
     
        
        if (!strActivated.equals("1"))
        {
            System.out.println("Mail deactivated");
            return;
        }        
        
        /*
         * Destination
         */
        if (strTo.trim().equals(""))
        {
            throw new CENTException("MAIL_MISSING_TO");
        }

        /*
         * Mail server
         */        
        if (strServer.trim().equals(""))
        {
            throw new CENTException("MAIL_MISSING_SERVER");
        }

        /*
         * Mail port
         */        
        if (strPort.trim().equals(""))
        {
            throw new CENTException("MAIL_MISSING_PORT");
        }
        
        /*
         * Username (mail)
         */        
        if (strUsername.trim().equals(""))
        {
            throw new CENTException("MAIL_MISSING_USERNAME");
        }
        
        /*
         * Password
         */
        if (strPassword.trim().equals(""))
        {
            throw new CENTException("MAIL_MISSING_PASSWORD");
        }        
        
        /*
         * Get system properties
         */
        Properties1 = System.getProperties();

        /*
         * Setup mail server
         */
        Properties1.put("mail.smtp.port", strPort);
        Properties1.put("mail.smtp.host", strServer);
        Properties1.put("mail.smtp.auth", "true");
        Properties1.put("mail.smtp.starttls.enable", true);        

        /*
         * Get the default Session object.
         */
        //Session1 = Session.getDefaultInstance(Properties1, new javax.mail.Authenticator() 
        Session1 = Session.getInstance(Properties1, new javax.mail.Authenticator() 
        {  
          @Override
          protected PasswordAuthentication getPasswordAuthentication() 
          {  
              return new PasswordAuthentication(strUsername, strPassword);
          }  
        });        

        try
        {
            MimeMessage1 = new MimeMessage(Session1);

            MimeMessage1.setFrom(new InternetAddress(strFrom));

            MimeMessage1.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));

            MimeMessage1.setSubject(strSubject);

            MimeMessage1.setContent(strBody, "text/html");

            Transport.send(MimeMessage1);

            System.out.println("Sent message successfully....");

        }
        catch (MessagingException mex) 
        {
           mex.printStackTrace();
        }
        finally
        {
            Session1 = null;
            Properties1 = null;
            MimeMessage1 = null;
            
            strPort = null;
            strFrom = null;
            strServer = null;
        }
    }        
    
}
