package business;

import entity.CENTData;
import entity.CENTException;
import java.io.File;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import persistence.CDALCore;


class CBUS extends project.Global
{
    /*
     * Main Constructor
     */    
    public CBUS(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        this.SetSession(CENTSession1);
        this.SetConnection(Connection1);
    }        
    
    protected CENTData GetSession() 
    {
        return CENTSession1;
    }    
    protected void SetSession(CENTData CENTSession1) 
    {
        this.CENTSession1 = CENTSession1;
    }
    
    protected Connection GetConnection()
    {
        return Connection1;
    }
    
    private void SetConnection(Connection Connection1) throws SQLException
    {
        if (this.GetConnection() == null)
        {
            this.Connection1 = Connection1;
        }
        else
        {
            if (this.GetConnection().isClosed())
            {
                this.Connection1 = Connection1;
            }            
        }
    }        

    public String Translate(List<CENTData> ListCENTDictionary1, String strItem) throws CENTException
    {
        /*
         * General Declaration
         */         
        String strCode;
        String strTranslated = "";

        try
        {
            strTranslated = strItem;
            
            /*
             * Find the item in the dictionary
             */                   
            for (CENTData CENTDictionary1 : ListCENTDictionary1)
            {
                strCode = CENTDictionary1.GetText(1).trim();
                
                if (strTranslated.equals(strCode))
                {
                    strTranslated = CENTDictionary1.GetText(2);
                    
                    break;
                }
            }
            
            /*
             * Return the module name
             */       
            return strTranslated;

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

        }
    }        
    
    public String Translate(String strLabel) throws CENTException
    {
        /*
         * General Declaration
         */              
        CDALCore CDALCore1 = null;

        try
        {
            /*
             * Create the Objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());

            /*
             * Get the module name
             */
            strLabel = CDALCore1.Translate(strLabel);
            
            /*
             * Return the module name
             */       
            return strLabel;

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

        
    public List<CENTData> GetCatalog(int intIdTransaction) throws CENTException
    {
        /*
         * General Declaration
         */                           
        List<CENTData> ListCENTData1 = null;
        CDALCore CDALCore1 = null;

        try
        {
            /*
             * Create the Objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());

            /*
             * Persist the record
             */
            CDALCore1.SetIdTransaction(this.GetIdTransaction());
            CDALCore1.SetIdView(this.GetIdView());
            
            ListCENTData1 = CDALCore1.GetCatalog(intIdTransaction);
                
            /*
             * Persist the record
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
            CDALCore1 = null;
        }
    }    
    
    public List<CENTData> GetView(int intIdView) throws CENTException
    {
        /*
         * General Declaration
         */                           
        List<CENTData> ListCENTData1 = null;
        CDALCore CDALCore1 = null;

        try
        {
            /*
             * Validate the view id
             */            
            if (intIdView == 0)
            {
                throw new CENTException("EXCEPTION_INVALID_VIEW_ID");
            }
            
            /*
             * Create the Objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());

            /*
             * Persist the record
             */
            CDALCore1.SetIdTransaction(this.GetIdTransaction());
            CDALCore1.SetIdView(this.GetIdView());            
            ListCENTData1 = CDALCore1.GetView(intIdView);
                
            /*
             * Persist the record
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
            CDALCore1 = null;
        }
    }       
    
    /*
     * Format functions
     */    
    public String FormatDate(Date datDate) throws Exception
    {   
        /*
         * General Declaration
         */
        String strMask = "";
        SimpleDateFormat SimpleDateFormat1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            strMask = this.GetSession().GetText(SESSION_MASK_DATE);
            SimpleDateFormat1 = new SimpleDateFormat(strMask);
            
            /*
             * Apply the mask and return string
             */            
            return SimpleDateFormat1.format(datDate);
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }          
        finally
        {
            SimpleDateFormat1 = null;            
        }
    }

    public String FormatDecimal(double dblValue) throws Exception
    {
        String strRet = "";
        DecimalFormat DecimalFormat1 = new DecimalFormat("0.0000");
    
        try
        {
            strRet = DecimalFormat1.format(Math.abs(dblValue));
            
            return strRet;
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            DecimalFormat1 = null;
        }
    }
    
    
    
    public String FormatNumber(double dblValue, int intDecimals) throws Exception
    {
        /*
         * General Declaration
         */
        DecimalFormat DecimalFormat1 = null;
        DecimalFormatSymbols DecimalFormatSymbols1 = null;
        
        try
        {
            /*
             * Create the objects
             */
            DecimalFormat1 = new DecimalFormat("#.########");
            DecimalFormatSymbols1 = DecimalFormat1.getDecimalFormatSymbols();
            
            
            if (this.GetSession().GetText(SESSION_DECIMAL_SIMBOL).trim().equals("."))
            {
                DecimalFormatSymbols1.setDecimalSeparator('.');
                DecimalFormatSymbols1.setGroupingSeparator(',');
            }
            else
            {
                DecimalFormatSymbols1.setDecimalSeparator(',');                
                DecimalFormatSymbols1.setGroupingSeparator('.');                
            }
            
            DecimalFormat1.setDecimalFormatSymbols(DecimalFormatSymbols1);
            //DecimalFormat1.setMinimumFractionDigits(intDecimals);
            //DecimalFormat1.setMaximumFractionDigits(intDecimals);
            DecimalFormat1.setRoundingMode(RoundingMode.HALF_UP);

            return DecimalFormat1.format(dblValue);
           
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }
    
    public String FormatInt(int intValue) throws Exception
    {   
        /*
         * General Declaration
         */
        
        try
        {
            /*
             * Create the objects
             */

            /*
             * Apply the mask and return string
             */            
            return String.valueOf(intValue);
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }          
        finally
        {

        }
    }    

    public String FormatString(String strValue) throws Exception
    {   
        /*
         * General Declaration
         */
        
        try
        {
            /*
             * Create the objects
             */

            /*
             * Apply the mask and return string
             */            
            return strValue.trim();
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }          
        finally
        {

        }
    }    

    public String GetTable(int intIdTrn) throws CENTException
    {
        /*
         * General Declaration
         */                           
        CDALCore CDALCore1 = null;
        String strTable = "";

        try
        {
            /*
             * Create the Objects
             */
            CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());
            
            /*
             * Get the table to be used to persist or query, it uses the transaction data_1
             */
            strTable = CDALCore1.GetTable(intIdTrn);

            /*
             * Return it
             */            
            return strTable;

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

    public CENTData FilterCriteria(int intIdTransaction, String strFieldObject, int intIdFieldType, int intIdOperator, Object strConditionValue) throws Exception
    {
        CENTData CENTData1 = new CENTData();
        
        CENTData1.SetInt(FIELD_ID_COMMAND, COMMAND_CONDITION_AND);
        CENTData1.SetInt(FIELD_ID_TRN, intIdTransaction);
        CENTData1.SetText(FIELD_OBJECT, strFieldObject);
        CENTData1.SetInt(FIELD_TYPE, intIdFieldType);
        CENTData1.SetInt(FIELD_ID_OPERATOR, intIdOperator);
        CENTData1.SetText(FIELD_CONDITION_VALUE, String.valueOf(strConditionValue));        
        CENTData1.SetInt(FIELD_ID_FK, 0); // enable join if different from zero
        
        return CENTData1;
    }
    
    public int GetLastIdByTransaction(int intIdTransaction) throws Exception
    {
        CDALCore CDALCore1 = new CDALCore(this.GetConnection(), this.GetSession());        
        return CDALCore1.GetLastIdByTransaction(intIdTransaction);
    }
    
    private double Round(double dblValue, int dblDecimals, int intCeilOrFloor) 
    {  
        double arredondado = dblValue;  
        
        arredondado *= (Math.pow(10, dblDecimals));  
       
        if (intCeilOrFloor == 0) 
        {  
            arredondado = Math.ceil(arredondado);             
        } 
        else 
        {  
            arredondado = Math.floor(arredondado);  
        }  
        
        arredondado /= (Math.pow(10, dblDecimals)); 
        
        return arredondado;  
    }         
    
    protected double StringToDouble(String strValue, String strMask) throws ParseException, Exception
    {
        /*
         * General Declaration
         */            
        double dblValue = 0;                                
        String strMaskDecimalFormat = "";
        
        try
        {
        
            if (strValue.trim().equals(""))
            {
                strValue = "0";
            }

            /*
             * Apply final mask
             */    
            strMaskDecimalFormat = strMask;
            strMaskDecimalFormat = strMaskDecimalFormat.replaceAll(",", ".");
            strMaskDecimalFormat = strMaskDecimalFormat.replaceAll("0", "#");
            DecimalFormat DecimalFormat1 = new DecimalFormat(strMaskDecimalFormat);

            /*
             * Generate the double
             */
            if (strMask.trim().contains(","))
            {
                strValue = strValue.replaceAll("\\.", "");
                strValue = strValue.replaceAll(",", ".");
            }

            if (strMask.trim().contains("."))
            {
                strValue = strValue.replaceAll(",", "");
            }

            dblValue = Double.parseDouble(strValue);        

            strValue = DecimalFormat1.format(dblValue).replaceAll(",", ".");

            dblValue = Double.parseDouble(strValue);

            return dblValue;
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PARSE_DOUBLE", Exception1.getMessage() + " : " + strValue + "->" + strMask);
        }
    }    
    
    
    public List<CENTData> GetListOfFilesInFolder(String strFolder) throws Exception
    {
        /*
         * General Declaration
         */
        CENTData CENTData1 = null;
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        
        /*
         * Get the files
         */        
        File[] ArrFiles = new File(strFolder).listFiles();
        
        /*
         * Return a list of files
         */                
        if (ArrFiles != null)
        {
            for (File File1 : ArrFiles)
            {
                if (File1.isFile())
                {
                    CENTData1 = new CENTData();

                    CENTData1.SetText(1, File1.getName());
                    CENTData1.SetText(2, File1.getAbsolutePath());

                    ListCENTData1.add(CENTData1);
                }
            }
        }
                
        return ListCENTData1;
        
    }

    public String GetPathFileToImport(String strFileName) throws Exception
    {
        
        /*
         * General Declarations
         */
        String strSlash = "";
        String strPath = "";
        
        /*
         * Detect operating system and apply correct path format
         */
        if (System.getProperty("os.name").toLowerCase().contains("win"))
        {
            strSlash = "\\";
        }
        else
        {
            strSlash = "/";            
        }
        
        /*
         * Prepare the path, note that the files are stored by company and profile
         */        
        strPath = this.GetPropertieValue("FILE_MANAGER") + this.GetSession().GetInt(SESSION_COMPANY) + strSlash + this.GetSession().GetInt(SESSION_AREA) + strSlash;
        
        if (!strPath.trim().equals(""))
        {
            strPath += strFileName;
        }        
        
        /*
         * Return it
         */
        return strPath;
    }

    
}
