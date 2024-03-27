package managementsystem.client;

import com.google.gson.annotations.SerializedName;

public class J_parts_modify_get
{
    @SerializedName("_id")
    private Id id;
    private String name;
    private int price;
    private int maxnum;
    public static String username;
    public static String role;

    public J_parts_modify_get(String id, String name, int price, int maxnum) {
        this.id = new Id(id);
        this.name = name;
        this.price = price;
        this.maxnum = maxnum;
    }

    public J_parts_modify_get(String name, int price, int maxnum) {
        this.name = name;
        this.price = price;
        this.maxnum = maxnum;
    }

    @Override
    public String toString()
    {
        return id+"\n"+name+"\n"+price+"\n"+maxnum+"\n\n";
    }

    public String getId() {
        return id.getOid();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxnum() {
        return maxnum;
    }
}