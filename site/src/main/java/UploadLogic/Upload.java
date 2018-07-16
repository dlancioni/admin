package UploadLogic;

import business.*;
import entity.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import project.Global;
 
@WebServlet("/UploadServlet")
@MultipartConfig(
                    fileSizeThreshold=1024*1024*10, // 2MB
                    maxFileSize=1024*1024*10,       // 2MB
                    maxRequestSize=1024*1024*10     // 2MB 
                )

public class Upload extends HttpServlet 
{     
    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException 
    {
        /*
         * General Declaration
         */
        int read = 0;
        int intIdCompany = 0;
        final byte[] bytes = new byte[1024];

        String strUrl = "page/main/filemanager.jsp";
        String strFileName = "";              
        
        Path Path1 = null;
        File File1 = null;
        Part Part1 = null;
        
        CENTData CENTSession1 = null;        
        CBUSCore CBUSCore1 = null;
        OutputStream OutputStream1 = null;
        InputStream InputStream1 = null;
        PrintWriter PrintWriter1 = null;
        Connection Connection1 = null;
        String strDestination = "";
        Global Global1 = new Global();
        
        try 
        {
            /*
             * Get the object with login information from the session
             */
            CENTSession1 = (CENTData) request.getSession().getAttribute("Login");
            Connection1 = (Connection) request.getSession().getAttribute("Connection");

            /*
             * Start common information
             */
            CBUSCore1 = new CBUSCore(Connection1, CENTSession1);                       
            intIdCompany = CENTSession1.GetInt(Global1.SESSION_COMPANY);
            strDestination = CBUSCore1.GetPathFileToImport("");
                    
            /*
             * Create the folder for specific clients if not exists
             */
            File1 = new File(strDestination);            
            
            if (!File1.exists())
            {
                Path1 = Paths.get(strDestination);
                Files.createDirectories(Path1);   
            }
            
            /*
             * Upload file 1
             */
            Part1 = request.getPart("file1");    

            if (Part1 != null)
            {
                strFileName = getFileName(Part1);
            }            
                    
            /*
             * if any of the files already exists, abort the process
             */
            if (FileExists(strDestination + strFileName))
            {
                throw new CENTException("EXCEPTION_FILE_ALREADY_EXISTS");
            }                

            if (!strFileName.trim().equals(""))
            {
                PrintWriter1 = response.getWriter();
                InputStream1 = Part1.getInputStream();
                OutputStream1 = new FileOutputStream(new File(strDestination + strFileName));

                while ((read = InputStream1.read(bytes)) != -1) 
                {
                    OutputStream1.write(bytes, 0, read);
                }
            }
                       
            /*
             * Upload success
             */
            response.sendRedirect(strUrl + "?message=MESSAGE_UPLOAD_SUCCESS");

        } 
        catch (CENTException CENTException1)
        {
            response.sendRedirect(strUrl + "?message=MESSAGE_UPLOAD_FAIL&error_code=" + CENTException1.GetCode() + "&error_message=" + CENTException1.GetMessage());
        }    
        catch (Exception Exception1)
        {
            response.sendRedirect(strUrl + "?message=MESSAGE_UPLOAD_FAIL&error_message=" + Exception1.getMessage());
        }
        finally 
        {
            if (OutputStream1 != null) 
            {
                OutputStream1.close();
            }
            
            if (InputStream1 != null) 
            {
                InputStream1.close();
            }
            
            if (PrintWriter1 != null) 
            {
                PrintWriter1.close();
            }
            
            /*
             * Destroy the objects
             */
            
        }
    }

    private String getFileName(final Part part) 
    {
        final String partHeader = part.getHeader("content-disposition");

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        
        return null;               
    }     
    
    private boolean FileExists(String strFile) throws Exception
    {
        File File1 = new File(strFile);
        
        if (File1.exists() && !File1.isDirectory()) 
            return true;
        else
            return false;
    }
    
}