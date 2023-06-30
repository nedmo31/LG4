package lg4;

import java.awt.Rectangle;

public abstract class Button {
    
    /**
     * The clickable area of this button
     */
    Rectangle area;

    // This is basically abstract, but we're gonna define it 
    // for each button
    public void clickAction() {}

    public abstract void paintButton(java.awt.Graphics g);

    public Button(Rectangle a) {
        area = a;
    }

}
