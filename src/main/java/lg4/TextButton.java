package lg4;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;

public class TextButton extends Button {

        public String text;
        public Color c = Color.black;
        public Font f = Window.f3;

        TextButton(Rectangle a, String t) {
            super(a);
            text = t;
        }

        TextButton(Rectangle a, String t, Color co) {
            this(a, t);
            c = co;
        }

        @Override
        public void paintButton(java.awt.Graphics g) {
            g.setColor(c); g.setFont(f);
            if (this.area.contains(Window.mx, Window.my)) {
                g.setFont(Window.f3b);
            } 
            g.drawString(text, this.area.x+10, this.area.y+area.height/2);
        }

    }