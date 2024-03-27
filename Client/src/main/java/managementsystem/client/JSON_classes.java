package managementsystem.client;

import com.google.gson.annotations.SerializedName;

public class JSON_classes
{

}

class J_users_new
{
    private String username;
    private String password;
    private int level;
    private String name;

    public J_users_new(String username, String password, int level, String name)
    {
        this.username = username;
        this.password = password;
        this.level = level;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return username+", "+password+", "+level+", "+name;
    }
}

class J_login
{
    private String username;
    private String password;

    public J_login(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString()
    {
        return username+", "+password;
    }
}

class Id {
    @SerializedName("$oid")
    private String oid;
    public Id(String oid) {this.oid = oid;}
    public String getOid() {return oid;}
    public void setOid(String oid) {this.oid = oid;}
}