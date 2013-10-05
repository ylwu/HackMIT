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
    long interval = 500;
    long maxSwipeCooldown = 1000;
    long swipeCooldown;
    boolean decreaseQueue = false;
    int[] gesCount;
    LinkedList<Vector> lastHands;
    LinkedList<Vector> lastFingerA;
    LinkedList<Vector> lastFingerB;
    LinkedList<Float> fingerDis;
    
    int tempCount;
    

    public LeapInput(){
        // Create a sample listener and controller
        listener = new LeapListener();
        controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        if(controller.config().setFloat("Gesture.ScreenTap.MinForwardVelocity", 20.0f) &&
                controller.config().setFloat("Gesture.ScreenTap.HistorySeconds", .2f) &&
                controller.config().setFloat("Gesture.ScreenTap.MinDistance", 4.0f) &&
                controller.config().setFloat("Gesture.KeyTap.MinDownVelocity", 30.0f) &&
                controller.config().setFloat("Gesture.KeyTap.HistorySeconds", .3f) &&
                controller.config().setFloat("Gesture.KeyTap.MinDistance", 4.0f) &&
                controller.config().setFloat("Gesture.Swipe.MinLength", 100.0f) &&
                controller.config().setFloat("Gesture.Swipe.MinVelocity", 500f) &&
                controller.config().setFloat("Gesture.Circle.MinRadius", 10.0f) &&
                controller.config().setFloat("Gesture.Circle.MinArc", 4.5f))
            controller.config().save();
        frame = controller.frame();
        lastFrame = frame;
        gesCount = new int[4];
        lastHands = new LinkedList<Vector>();
        lastFingerA = new LinkedList<Vector>();
        lastFingerB = new LinkedList<Vector>();
        fingerDis = new LinkedList<Float>();
        swipeCooldown = 0;
        tempCount = 0;
    }
    
    public void processHand(){
        if (!frame.hands().empty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();
            if (fingers.count()>1) {
                // Calculate the hand's average finger tip position
                Vector avgPos = Vector.zero();
                for (Finger finger : fingers) {
                    avgPos = avgPos.plus(finger.tipPosition());
                }
                Finger fingerA = fingers.get(0);
                Finger fingerB = fingers.get(1);
                Vector fingerAPos = fingerA.tipPosition();
                Vector fingerBPos = fingerB.tipPosition();
                avgPos = avgPos.divide(fingers.count());
                System.out.println("Hand has " + fingers.count()
                                 + " fingers, average finger tip position: " + avgPos);
                if (lastHands.size()>10){
                    lastHands.remove();
                }
                lastHands.add(avgPos);
                checkHand();
                
                if (fingers.count()==2){
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
                        fingerDis.add(finDiff);
                    }
                    checkZoomer();
                }
            }

            // Get the hand's sphere radius and palm position
            System.out.println("Hand sphere radius: " + hand.sphereRadius()
                             + " mm, palm position: " + hand.palmPosition());

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
//        for (int i=0; i<numGestures;i++){
//            if (gestures.get(i).type()==Gesture.Type.TYPE_SCREEN_TAP){
//                ScreenTapGesture tap = new ScreenTapGesture(gestures.get(i));
//                onScreenTap(tap);
//            }else if (gestures.get(i).type()==Gesture.Type.TYPE_KEY_TAP){
//                KeyTapGesture tap = new KeyTapGesture(gestures.get(i));
//                onKeyTap(tap);
//            }else if (gestures.get(i).type()==Gesture.Type.TYPE_SWIPE){
//                SwipeGesture tap = new SwipeGesture(gestures.get(i));
//                if (tap.pointables().count()>3){
//                    System.out.println("tap: "+Integer.toString(tap.pointables().count()));
//                    onSwipe(tap);
//                }
//            }else if (gestures.get(i).type()==Gesture.Type.TYPE_CIRCLE){
//                CircleGesture tap = new CircleGesture(gestures.get(i));
//                onCircle(tap);
//            }
//        }
        
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
    
    public void updatePointer(){
        
    }
    
    public void checkHand(){
        if (lastHands.size()<=3) return;
        int mid = lastHands.size()/2;
        
    }
    
    public void checkZoomer(){
        if (lastFingerA.size()==0 || lastFingerB.size()==0 
                || fingerDis.size()==0){
            return;
        }
        float current;
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
        //processHand();
        processGestures();
        lastFrame = frame;

        if (swipeCooldown>0){
            swipeCooldown-=interval;
        }
        if (decreaseQueue){
            if (lastFingerA.size()>0){
                lastFingerA.remove();
            }
            if (lastFingerB.size()>0){
                lastFingerB.remove();
            }
        }
        decreaseQueue = !decreaseQueue;
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
