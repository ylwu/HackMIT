package leap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

public class LeapInput {
    
    Controller controller;
    Frame frame;
    Frame lastFrame;
    LeapListener listener;
    long interval = 200;
    // Cooldown set to keep pages from flipping back due to hand repositioning
    long maxSwipeCooldown = 1000;
    long swipeCooldown;
    // Cooldown set to keep zoom from bouncing back due to finger relaxing
    long maxZoomCooldown = 1000;
    long zoomInCooldown;
    long zoomOutCooldown;
    long maxScrollCooldown = 500;
    long scrollCooldown;
    // Frequency of queue cleaning
    int decreaseFreq = 2;
    int decreaseQueue = 1;
    int[] gesCount;
    // Allow zoom and drag when true
    boolean inOperation = false;
    float OPERATION_THRESHOLD = 30;
    float SCROLL_THRESHOLD = 10;
    float ZOOM_THRESHOLD = 10;
    LinkedList<Vector> lastHands;
    LinkedList<Vector> lastFingerA;
    LinkedList<Vector> lastFingerB;
    LinkedList<Float> fingerDis;
    
    boolean isDrawing = false;
    //Starting point of the extract zone
    Vector areaStart;
    Vector lastFinger;
    int selectCounter;
    int SELECT_HOLD_RANGE = 8;
    int MAX_SELECT_COUNTER = 15;
    float xMin = -250;
    float curXMin;
    float xMax = 250;
    float curXMax;
    float yMin = -350;
    float curYMin;
    float yMax = 350;
    float curYMax;
    int sidesCompleted;
    int faultTillNow;
    
    int tempCount;
    

    public LeapInput(){
        // Create a sample listener and controller
        listener = new LeapListener();
        controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        if(controller.config().setFloat("Gesture.Swipe.MinLength", 100.0f) &&
                controller.config().setFloat("Gesture.Swipe.MinVelocity", 500f))
            controller.config().save();
        frame = controller.frame();
        lastFrame = frame;
        gesCount = new int[4];
        lastHands = new LinkedList<Vector>();
        lastFingerA = new LinkedList<Vector>();
        lastFingerB = new LinkedList<Vector>();
        fingerDis = new LinkedList<Float>();
        swipeCooldown = 0;
        zoomInCooldown = 0;
        zoomOutCooldown = 0;
        scrollCooldown = 0;
        tempCount = 0;
        resetSelect();
    }
    
    // Takes care of toggling zooming/scrolling on and off
    public void processHand(){
        if (!frame.hands().empty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();
            Vector handPos = hand.palmPosition();
            if (lastHands.size()>10){
                lastHands.remove();
            }
            lastHands.add(handPos);
            checkHand();
            //System.out.println(fingers.count());
            
            // For page flipping, zooming and on-page scrolling
            if (fingers.count()==2 && inOperation && !isDrawing) {
                Finger fingerA = fingers.get(0);
                Finger fingerB = fingers.get(1);
                Vector fingerAPos = fingerA.tipPosition();
                Vector fingerBPos = fingerB.tipPosition();
                
                
                if (lastFingerA.size()>10){
                    lastFingerA.remove();
                }
                lastFingerA.add(fingerAPos);
                
                if (lastFingerB.size()>10){
                    lastFingerB.remove();
                }
                lastFingerB.add(fingerBPos);
                float finDiff = fingerAPos.distanceTo(fingerBPos);
                if (fingerDis.size()>10){
                    fingerDis.remove();
                }
                fingerDis.add(finDiff);
                checkZoomer();
            }
            // For specifying an area to extract
            else if (fingers.count()==1){
                Vector thisTip = fingers.get(0).tipPosition();
                if (isDrawing){
                    updateDrawing(thisTip);
                }else{
                    fireEvent("hover,"+Float.toString(thisTip.getX())+","+Float.toString(thisTip.getY()));
                }
                if (selectCounter==MAX_SELECT_COUNTER){
                    isDrawing = true;
                    areaStart = thisTip;
                    fireEvent("start,"+Float.toString(areaStart.getX())+","+Float.toString(areaStart.getY()));
                    selectCounter = 0;
                }
                if (lastFinger!=null){
                    if (thisTip.distanceTo(lastFinger)<SELECT_HOLD_RANGE){
                        selectCounter++;
                    }
                    lastFinger = thisTip;
                }else{
                    lastFinger = thisTip;
                }
            }

            // Get the hand's sphere radius and palm position
//            System.out.println("Hand sphere radius: " + hand.sphereRadius()
//                             + " mm, palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            //System.out.println("Hand pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
            //                 + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
            //                 + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
        }
    }
    
    public void processGestures(){
        if (isDrawing) return;
        GestureList gestures = lastFrame.isValid()?
                frame.gestures(lastFrame) :
                frame.gestures();
        //GestureList gestures = frame.gestures();
        int numGestures = gestures.count();
        List<Gesture> gesList = new ArrayList<Gesture>();
        //System.out.println(Integer.toString(numGestures));
        if (!frame.hands().isEmpty()){
            if (frame.hands().get(0).fingers().count()>2 &&
                numGestures>0 &&
                swipeCooldown<=0){
                for (int i=0;i<numGestures;i++){
                    if (gestures.get(i).type()==Gesture.Type.TYPE_SWIPE){
                        gesList.add(gestures.get(i));
                    }
                }
                processSwipe(gesList);
                swipeCooldown = maxSwipeCooldown;
            }
        }
        
    }
    
    public void processSwipe(List<Gesture> list){
        int vSize = list.size();
        Vector totStart = Vector.zero();
        Vector totEnd = Vector.zero();
        for (Gesture ges:list){
            SwipeGesture gs = new SwipeGesture(ges);
            //System.out.println("Start:"+gs.startPosition().toString());
            //System.out.println("Current:"+gs.position().toString());
            totStart = totStart.plus(gs.startPosition());
            totEnd = totEnd.plus(gs.position());
        }
        //System.out.println(totStart.toString());
        //System.out.println(totEnd.toString());
        Vector result = (totEnd.minus(totStart)).divide(vSize);
        fireEvent("swipe,"+Float.toString(result.getX())+","+Float.toString(result.getY()));
        System.out.println(result.toString());
    }
    
    
    // For drawing the extract area
    // -To start: hold 1 finger on a spot for 1.5 secs. Need to be in the operating state. 
    //    Disables operating state switch after starting
    // -To draw: drag finger to the bottom right corner
    // -To finish: hold on a spot for 1.5 secs.
    // -To abort: hold on the starting spot for 1.5 secs .
    public void updateDrawing(Vector fingerTip){
        if (selectCounter==MAX_SELECT_COUNTER){
            if (fingerTip.distanceTo(areaStart)>SELECT_HOLD_RANGE*2){
                fireEvent("finish,"+Float.toString(fingerTip.getX())+","+Float.toString(fingerTip.getY()));
            }else {
                fireEvent("abort,"+Float.toString(fingerTip.getX())+","+Float.toString(fingerTip.getY()));
            }
            resetSelect();
            return;
        }
        if (fingerTip.distanceTo(lastFinger)<SELECT_HOLD_RANGE){
            selectCounter++;
        }else{
            selectCounter = 0;
        }
        fireEvent("drag,"+Float.toString(fingerTip.getX())+","+Float.toString(fingerTip.getY()));
        lastFinger = fingerTip;
    }
    
    private void resetSelect(){
        isDrawing = false;
        selectCounter = 0;
        curXMin = xMax;
        curXMax = xMin;
        curYMin = yMax;
        curYMax = yMin;
        sidesCompleted = 0;
        faultTillNow = 0;
    }
    
    public void checkHand(){
        if (isDrawing) return;
        if (lastHands.size()<=3) return;
        //System.out.println("checking hand");
        int mid = lastHands.size()/2;
        Vector thisHand = lastHands.getLast();
        Vector lastHand = lastHands.get(lastHands.size()-2);
        Vector lastHand2 = lastHands.get(lastHands.size()-3);
        if (thisHand.minus(lastHand).getZ()<(-OPERATION_THRESHOLD) &&
                lastHand.minus(lastHand2).getZ()<(-0.5*OPERATION_THRESHOLD)){
            if (!inOperation) {
                System.out.println("Operation true");
                inOperation = true;
                zoomInCooldown = maxZoomCooldown;
                zoomOutCooldown = maxZoomCooldown;
                scrollCooldown = maxScrollCooldown;
                swipeCooldown = maxSwipeCooldown;
            }
        }else if (thisHand.minus(lastHand).getZ()>(OPERATION_THRESHOLD) &&
                lastHand.minus(lastHand2).getZ()>(0.5*OPERATION_THRESHOLD)){
            if (inOperation){
                System.out.println("Operation false");
                inOperation = false;
                zoomInCooldown = maxZoomCooldown;
                zoomOutCooldown = maxZoomCooldown;
                scrollCooldown = maxScrollCooldown;
                swipeCooldown = maxSwipeCooldown;
            }
        }
            
    }
    
    public void checkZoomer(){
        if (lastFingerA.size()<3 || lastFingerB.size()<3
                || fingerDis.size()<3){
            return;
        }
        //System.out.println("Checking zoomer");

        Float thisDis = fingerDis.getLast();
        Float lastDis = fingerDis.get(fingerDis.size()-2);
        Float lastDis2 = fingerDis.get(fingerDis.size()-3);
        if (thisDis-lastDis>ZOOM_THRESHOLD &&
                lastDis-lastDis2>ZOOM_THRESHOLD*0.3){
            if (zoomOutCooldown<=0){
                fireEvent("zoomOut,"+Float.toString(thisDis-lastDis));
                zoomOutCooldown = maxZoomCooldown/2;
                zoomInCooldown = maxZoomCooldown;
            }
        }else if (thisDis-lastDis<-ZOOM_THRESHOLD &&
                lastDis-lastDis2<-ZOOM_THRESHOLD*0.3){
            if (zoomInCooldown<=0){
                fireEvent("zoomIn,"+Float.toString(lastDis-thisDis));
                zoomInCooldown = maxZoomCooldown/2;
                zoomOutCooldown = maxZoomCooldown;
            }
        }
        
        
        Vector thisA = lastFingerA.getLast();
        Vector lastA = lastFingerA.get(lastFingerA.size()-2);
        Vector lastA2 = lastFingerA.get(lastFingerA.size()-3);
        
        Vector thisB = lastFingerB.getLast();
        Vector lastB = lastFingerB.get(lastFingerB.size()-2);
        Vector lastB2 = lastFingerB.get(lastFingerB.size()-3);
        
        Vector thisMid = thisA.plus(thisB).divide(2);
        Vector lastMid = lastA.plus(lastB).divide(2);
        Vector lastMid2 = lastA2.plus(lastB2).divide(2);
        Vector thisMove = thisMid.minus(lastMid);
        Vector lastMove = lastMid.minus(lastMid2);
        Vector weightedMove = thisMove.plus(lastMove.times(0.3f)).divide(1.3f);
        if (weightedMove.getX()*weightedMove.getX()+weightedMove.getY()*weightedMove.getY()>400 && scrollCooldown<=0){
            fireEvent("scroll,"+Float.toString(weightedMove.getX())+","+Float.toString(weightedMove.getY()));
        }
    }
    
    public void onScreenTap(ScreenTapGesture tap){
        System.out.println("Screen tap");
        
    }
    
    public void onKeyTap(KeyTapGesture tap){
        System.out.println("Key tap");
    }
    
    public void onSwipe(SwipeGesture tap){
        System.out.println("Swipe");
    }
    
    public void onCircle(CircleGesture tap){
        System.out.println("Circle");
    }
    
    public void update(){
        frame = controller.frame();
        //System.out.println(frame.toString());
        if (lastFrame == frame){
            System.out.println("same");
            return;
        }
        processHand();
        processGestures();
        lastFrame = frame;

        if (swipeCooldown>0){
            swipeCooldown-=interval;
        }
        if (zoomInCooldown>0){
            zoomInCooldown-=interval;
        }
        if (zoomOutCooldown>0){
            zoomOutCooldown-=interval;
        }
        if (scrollCooldown>0){
            scrollCooldown-=interval;
        }
        if (decreaseQueue%decreaseFreq==0){
            if (lastFingerA.size()>0){
                lastFingerA.remove();
            }
            if (lastFingerB.size()>0){
                lastFingerB.remove();
            }
        }
        decreaseQueue++;
    }
    
    public void start(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                update();
                //System.out.println(tempCount);
            }
        }, interval, interval);
    }
 
    private List _listeners = new ArrayList();
    public synchronized void addEventListener(LeapEventListener listener)  {
      _listeners.add(listener);
    }
    public synchronized void removeEventListener(LeapEventListener listener)   {
      _listeners.remove(listener);
    }
    // call this method whenever you want to notify
    //the event listeners of the particular event
    private synchronized void fireEvent(String message) {
        //System.out.println(message);
        LeapEvent event = new LeapEvent(this, message);
        Iterator i = _listeners.iterator();
        while(i.hasNext())  {
            ((LeapEventListener) i.next()).handleLeapEvent(event);
        }
    }
    
    public static void main(String[] args){
        LeapInput leap = new LeapInput();
        leap.start();
    }

}
