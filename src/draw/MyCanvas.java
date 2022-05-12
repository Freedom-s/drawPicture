package draw;

import java.awt.*;

/**
 * @author freedom-sjh
 * @data 2022/5/11-15:54
 */
public class MyCanvas extends Canvas{
    private Image image = null;
    public void setImage(Image image){
        this.image = image;
    }
    public void paint(Graphics g){
        g.drawImage(image,0,0,null);

    }
    //重写这个更新 方法可以解决 屏幕闪烁的问题
    public void update(Graphics g){
        paint(g);//调用上面的方法
    }
}
