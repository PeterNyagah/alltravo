package startup.model;

/**
 * Created by Wolf Soft on 2/1/2017.
 */

public class ItemData1 {

    String text;

    public ItemData1(String text, Integer imageId){
        this.text=text;

    }

    public ItemData1(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }

}

