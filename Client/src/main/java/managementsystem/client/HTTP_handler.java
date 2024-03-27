package managementsystem.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HTTP_handler
{
    private String serverURL;
    private String path;
    private static String token = "";
    private HttpURLConnection connection;

    public static void setToken(String token)
    {
        HTTP_handler.token = token;
        System.out.println("Token set to: "+token);
    }


    public HTTP_handler(String serverURL,String path)
    {
        this.serverURL=serverURL;
        this.path=path;
    }

    //CSATLAKOZÁS
    public String startUp() throws Exception
    {
        System.out.println("\n//CONNECTION STARTED AT " + new Date() + "//");
        URL url = new URL(serverURL + path);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        if(!HTTP_handler.token.isEmpty())
        {
            cookieSend("Cookie","session=" + HTTP_handler.token,"");
        }

        return "Deed is done";
    }

    //LECSATLAKOZÁS
    public void stop()
    {
        connection.disconnect();
        System.out.println("//CONNECTION TERMINATED AT " + new Date() + "//");
    }

    public String getResponse()
    {
        try
        {
            return String.valueOf(connection.getResponseCode());
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }

    public void cookieSend(String cookieType, String cookieValue, String inputString)
    {
        try
        {
            connection.setRequestProperty(cookieType, cookieValue);
            if(!inputString.isEmpty())
            {
                connection.setDoOutput(true);
                try(OutputStream os = connection.getOutputStream())
                {
                    byte[] input = inputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public List<String> cookieRead(String readType)
    {
        //System.out.println(connection.getHeaderFields());
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(readType);
        if(readType.equals("debug"))
        {
            System.out.println("Cookies: ");
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet())
            {
                System.out.println("Key: "+entry.getKey());
                System.out.print("Value: ");
                for (String l : entry.getValue())
                {
                    System.out.print(l+"  ;;  ");
                }
                System.out.println("\n");
            }
            return cookiesHeader;
        }
        else
        {
            return cookiesHeader;
        }
    }

    //UGYAN AZ MINT A RETURN READ
    public String bodyRead()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }

    //UGYAN AZ MINT A BODY READ
    /*public String returnRead()
    {
        try
        {
            String result;
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while(result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();
            return result;
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }*/

    public String tokenCutter(List<String> tempList)
    {
        String Token = tempList.get(0);
        String[] tempArray = Token.split(";");
        Token = tempArray[0].substring(8);
        return Token;
    }
}