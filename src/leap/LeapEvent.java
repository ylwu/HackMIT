package leap;

import java.util.EventObject;

public class LeapEvent extends EventObject {
    public String message;
    public LeapEvent(Object source, String mess) {
        super(source);
        message = mess;
        // TODO Auto-generated constructor stub
    }

}
