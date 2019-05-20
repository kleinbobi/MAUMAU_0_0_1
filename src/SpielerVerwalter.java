import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SpielerVerwalter {

    private List<Karte> hand = new LinkedList<Karte>();

    private Socket socket;
    ObjectInputStream inFromServer;
    ObjectOutputStream outToServer;

    public SpielerVerwalter(Socket socket) {
        this.socket = socket;

        try {
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void  setHand(List<Karte> hand) {

        this.hand = hand;



    }
    public void  setHand() {
        hand.stream().forEach(x -> System.out.println(x));
        try {
            outToServer.reset();
            outToServer.writeObject(this.hand);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void  setKarte(Karte karte) {

        try {

            outToServer.writeObject(karte);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public Karte getReturn(){
        try {
            Karte ret = (Karte) inFromServer.readObject();

           return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeKarte(Karte karte){
        for (Karte k2: hand
             ) {
            if(karte.getVal() == k2.getVal() && k2.getTrumpf() == karte.getTrumpf()){
                hand.remove(k2);
                System.out.println("Gefunden");
                break;
            }
        }


        this.hand = hand;
    }

    public void addKarte(Karte k){
        hand.add(k);
    }

    public List<Karte> getHand() {
        return hand;
    }
}
