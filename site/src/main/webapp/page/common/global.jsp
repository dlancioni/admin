<%@page import="project.Global"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.List"%>

<%@page import="business.CBUSCore"%>
<%@page import="entity.CENTException"%>
<%@page import="entity.CENTData"%>
<%@page import="java.util.Date"%>


<%!           
    /*
     * Boolean Datatype constants
     */    
    public static final int Null = 0;
    public static final int True = 1;
    public static final int False = 2;
    
    
    /*
     * Get the tag value based on the name
     */     
    public String GetValueToChart(CENTData CENTData1, String strFieldName)
    {
        String strRet = "";

        try
        {            
            if (strFieldName.contains("int_"))
            {
                strRet = String.valueOf(CENTData1.GetInt(strFieldName));
            }
            
            if (strFieldName.contains("text_"))
            {
                strRet = String.valueOf(CENTData1.GetText(strFieldName));
            }

            if (strFieldName.contains("double_"))
            {
                strRet = String.valueOf(CENTData1.GetDouble(strFieldName));
            }
            
            if (strFieldName.contains("date_"))
            {
                strRet = String.valueOf(CENTData1.GetDate(strFieldName));
            }                        
            
            return strRet;
            
        }
        catch (Exception Exception1)
        {
            return "";
        }
        finally
        {
            CENTData1 = null;
        }
    }    
    
    
    /*
     * Get the tag value based on the name
     */     
    public String GetEventFunction(CBUSCore CBUSCore1, int intIdTrn, String strFieldName, int intIdAction)
    {
        int intIdFunction = 0;
        int intIdEvent = 0;
        
        String strRet = "";
        String strEvent = "";
        String strFunction = "";
        String strParam1 = "";
        String strParam2 = "";
        String strParam3 = "";
        String strParam4 = "";
        String strParam5 = "";
        
        List<CENTData> ListCENTData1 = null;
        Global Global1 = null;
        CENTData CENTData1 = null;

        try
        {        
            /*
             * Create the objects
             */
            ListCENTData1 = new ArrayList<CENTData>();
            Global1 = new Global();
            CENTData1 = new CENTData();
            
            /*
             * Get the events related to the field
             */            
            CENTData1.SetInt(2, intIdTrn);
            CENTData1.SetText(1, strFieldName);
            CENTData1.SetInt(3, intIdAction);            
            CBUSCore1.SetIdTransaction(Global1.TRN_FIELD_EVENT);
            CBUSCore1.SetIdView(-1); // Access catalog            
            ListCENTData1 = CBUSCore1.GetList(CENTData1);
            
            if (!ListCENTData1.isEmpty())
            {        
                if (ListCENTData1.get(0).GetText("text_2") != null)
                    strParam1 = ListCENTData1.get(0).GetText("text_2"); 
                else
                    strParam1 = "";                

                if (ListCENTData1.get(0).GetText("text_3") != null)
                    strParam2 = ListCENTData1.get(0).GetText("text_3"); 
                else
                    strParam2 = "";            

                if (ListCENTData1.get(0).GetText("text_4") != null)
                    strParam3 = ListCENTData1.get(0).GetText("text_4"); 
                else
                    strParam3 = "";            

                if (ListCENTData1.get(0).GetText("text_5") != null)
                    strParam4 = ListCENTData1.get(0).GetText("text_5"); 
                else
                    strParam4 = "";            

                if (ListCENTData1.get(0).GetText("text_6") != null)
                    strParam5 = ListCENTData1.get(0).GetText("text_6");
                else
                    strParam5 = "";

                intIdEvent = ListCENTData1.get(0).GetInt("int_4");
                intIdFunction = ListCENTData1.get(0).GetInt("int_5");
                        
                /*
                 * Get the events related to the field
                 */      
                CENTData1 = new CENTData();
                CENTData1.SetText(1, "domain_13");            
                CENTData1.SetInt(2, intIdEvent);
                
                CBUSCore1.SetIdTransaction(Global1.TRN_DOMAIN);
                CBUSCore1.SetIdView(-1); // Access catalog
                
                ListCENTData1 = CBUSCore1.GetList(CENTData1);                
                
                if (!ListCENTData1.isEmpty())
                {        
                    strEvent = ListCENTData1.get(0).GetText("text_2");
                    
                    /*
                     * Get the function related to the field
                     */      
                    CENTData1 = new CENTData();
                    CENTData1.SetText(1, "domain_14");
                    CENTData1.SetInt(2, intIdFunction);

                    CBUSCore1.SetIdTransaction(Global1.TRN_DOMAIN);
                    CBUSCore1.SetIdView(-1); // Access catalog

                    ListCENTData1 = CBUSCore1.GetList(CENTData1);                

                    if (!ListCENTData1.isEmpty())
                    {        
                        strFunction = ListCENTData1.get(0).GetText("text_2");
                    }                    
                }

                // Example of final result: "onChange='JavaScript:SetDropDown(C_EVENT_INSERT, id_session, this.selectedIndex, 18);'";
                strRet = " " + strEvent + "='JavaScript:" + strFunction + "(" + intIdAction + ", " + strParam1 + ", " + strParam2 + ", " + strParam3 + ");'";                
            }            
            
            return strRet;

        }
        catch (Exception Exception1)
        {
            return "";
        }
        finally
        {
            ListCENTData1 = null;
            Global1 = null;
            CENTData1 = null;
        }
    }    
    
    /*
     * Util function used to manipulate data
     */ 
    public boolean IsUserAuthorized(String strTransaction, String strFunction)
    {
        return true;
    }   
     
    /*
     * SEt the page title
     */      
    public String SetTitle(int intType, String strName, String strDescription1, String strDescription2)
    {
        // Type 1: One level module (Trade, Profile, Transaction)
        // Type 2: Two level Module (Entity/Account, Entity/Contact)

        String strTitle = "";

        strTitle += "<h3><b>" + strName + "</b></h3>";

        strTitle += "<p>";

        if (!strDescription1.trim().equals(""))
        {
            strTitle += "<h5>" + strDescription1 + "</h5>";   
        } 

        return strTitle;
    }

    /*
     * Get the tag value based on the name
     */     
    public String GetValueFromParamTag(String strParam, String strTagName)
    {
        String strRet = "";

        try
        {
            String strTag1 = "[" + strTagName.trim() + "]";
            String strTag2 = "[/" + strTagName.trim() + "]";

            int intPos1 = 0;
            int intPos2 = 0;

            intPos1 = strParam.indexOf(strTag1) + strTag1.length();
            intPos2 = strParam.indexOf(strTag2);

            strRet = strParam.substring(intPos1, intPos2);

            return strRet;

        }
        catch (Exception Exception1)
        {
            return "";
        }
    }

    /*
     * Message template when event executes successfuly
     */
    public String MessageSuccess(String strMessage)
    {

        String strParam = "";

        strParam = "";
        strParam += "[PostBackStatus]";
            strParam += "[Status]" + "SUCCESS" + "[/Status]";
            strParam += "[Code][/Code]";
            strParam += "[Message]" + strMessage + "[/Message]";
            strParam += "[Field][/Field]";
        strParam += "[/PostBackStatus]";

        return strParam.trim();
    }

    /*
     * Message template used when we face an business exception
     */    
    public String MessageException(CENTException CENTException1)
    {    
        String strParam = "";

        strParam = "";
        strParam += "[PostBackStatus]";
            strParam += "[Status]" + "EXCEPTION" + "[/Status]";
            strParam += "[Code]" + CENTException1.GetCode() + "[/Code]";
            strParam += "[Message]" + CENTException1.GetMessage() + "[/Message]";
            strParam += "[Field]" + CENTException1.GetFieldName() + "[/Field]";
        strParam += "[/PostBackStatus]";

        return strParam.trim();
    }

    /*
     * Message template used when we face an exception
     */        
    public String MessageException(Exception Exception1)
    {    
        String strParam = "";

        strParam = "";
        strParam += "[PostBackStatus]";
            strParam += "[Status]" + "EXCEPTION" + "[/Status]";
            strParam += "[Code]" + "EXCEPTION_UI_GENERAL" + "[/Code]";
            strParam += "[Message]" + Exception1.getMessage() + "[/Message]";
            strParam += "[Field][/Field]";
        strParam += "[/PostBackStatus]";

        return strParam.trim();
    }
    
    /*
     * Make the zebra effect
     */ 
    public String TRColor(int intId)
    {
        if ((intId % 2) == 0)
            return "active";
        else
            return "";        
    }        
 
    
    public String GetData(CBUSCore CBUSCore1, int intValue, int intIdTrn)
    {        
        /*
         * General Declaration
         */
        CENTData CENTData1 = null;
        List<CENTData> ListCENTData1 = null;
   
        try
        {
            /*
             * Get the events related to the field
             */      
            CENTData1 = new CENTData();
            CENTData1.SetInt(1, intValue);

            CBUSCore1.SetIdTransaction(intIdTrn);
            CBUSCore1.SetIdView(-1); // Access catalog

            ListCENTData1 = CBUSCore1.GetList(CENTData1);
            
            if (ListCENTData1.size() > 0)
            {
                return ListCENTData1.get(0).GetText(1);
            }
            else
            {
                return "";
            }
        }
        catch (Exception Exception1)
        {
            return "";
        }
    }
    
    
    private String GetLink(String strPageName, int intIdTransaction, int intIdTransactionType, String strTransactionName)
    {
        String strLink = "";
        
        switch (strPageName.trim())
        {
            case "filemanager":
                strLink = strPageName + ".jsp?id_transaction=";
                strLink += intIdTransaction;
                strLink += "&type_transaction=" + intIdTransactionType;
                strLink += "&name_transaction=" + strTransactionName;
                break;

            case "logout":

                //strLink = "/app1/page/main/logout.jsp";
                strLink = "logout.jsp";

                break;

            default:
                strLink = strPageName + "_list.jsp?id_transaction=";
                strLink += intIdTransaction;
                strLink += "&type_transaction=" + intIdTransactionType;
                strLink += "&name_transaction=" + strTransactionName;
        }

        
        return strLink;
    }
   
    private int isNullInt(Object Object1)
    {
        if (Object1 == null)
            return 0;
        else
            return Integer.parseInt(Object1.toString());

    }
    
 %>