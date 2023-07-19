package lg4;

import java.awt.Color;
import java.awt.Rectangle;

public class TextButtonWithMessage extends TextButton {

    public String onHover;

    TextButtonWithMessage(Rectangle a, String t, Color co, String hoverMessage) {
        super(a, t, co);
        onHover = hoverMessage;
    }

    @Override
    public void paintButton(java.awt.Graphics g) {
        g.setFont(f);
        if (this.area.contains(Window.mx, Window.my)) {
            g.setColor(Color.black);
            g.drawString(onHover, Window.mx+5, Window.my-5);

            g.setFont(Window.f3b);
            
        } 
        g.setColor(c); 
        g.drawString(text, area.x+10, area.y+area.height/2);
    }
    
}
