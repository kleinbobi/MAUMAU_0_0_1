import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server {


    private static int SPIELERANZAHL = 1;

    private static SpielerVerwalter spierler[] = new SpielerVerwalter[SPIELERANZAHL];

    private static Stack<Karte> set;

    private static Stack<Karte> playedset ;

    public static Karte[] karten = new Karte[36];



    public static void main(String[] args) {


        for (int i = 6; i < 15; i++) {
            karten[i-6]= new Karte(i,'h');
        }
        for (int i = 6; i < 15; i++) {
            karten[i-6+9]= new Karte(i,'l');
        }
        for (int i = 6; i < 15; i++) {
            karten[i-6+9+9]= new Karte(i,'s');
        }
        for (int i = 6; i < 15; i++) {
            karten[i-6+9+9+9]= new Karte(i,'e');
        }

        createSet();
        spielersuche();
        theGame();
    }

    private static void spielersuche(){

        int kon = 0;


            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(4444);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not listen on port: 2556.");
                System.exit(-1);
            }
        while (kon < SPIELERANZAHL){
            try {
                spierler[kon] = new SpielerVerwalter(serverSocket.accept());

                kon++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




        for (int i = 0; i < SPIELERANZAHL; i++) {
            List<Karte> hand = new LinkedList<>();
            for (int j = 0; j < 5; j++) {
                hand.add(set.pop());
            }
            spierler[i].setHand(hand);
        }



    }



    private static void createSet(){
       // List<Karte> ret = new LinkedList<>();
        ArrayList<Integer> integerList = new ArrayList<Integer>();
        Random random = new Random();

        Stack<Karte> ret = new Stack();

        for (int i = 0; i < karten.length; i++) {
            int a = random.nextInt(karten.length);
            if(!integerList.contains(a)){

                integerList.add(a);
                ret.push(karten[a]);
            }else {
                i--;
            }



        }
        ret.stream().forEach((x)-> System.out.println(x));

        set = ret;
    }

    private static void theGame(){
        playedset = new Stack<>();
        int ober = 0;
        Karte zufohr = set.pop();

        System.out.println("GAme started");
        int spieler = 0;
        boolean weiter = true;
        while (true) {
            for (int i = 0; i < SPIELERANZAHL; i++) {
                if(!weiter){
                    i = spieler;
                    weiter = true;
                }

                spierler[i].setHand();
                spierler[i].setKarte(zufohr);

                Karte antwort = spierler[i].getReturn();

                System.out.println(antwort.toString());


                if (zufohr.getTrumpf() == antwort.getTrumpf() || zufohr.getVal() == antwort.getVal() || antwort.getVal() == 0) {
                    if (antwort.getVal() == 0) {
                        spierler[i].addKarte(set.pop());
                        weiter = false;
                        spieler = i;
                    }else {
                        zufohr = antwort;
                        spierler[i].removeKarte(antwort);
                        playedset.add(antwort);
                        if (antwort.getVal() == 14) {
                            i++;
                        }
                        if (antwort.getVal() == 9) {
                            zufohr = new Karte(9, antwort.getWünschen());
                        }
                        if (antwort.getVal() == 12) {
                            ober += 2;
                        } else {
                            if (ober != 0) {
                                for (int j = 0; j < ober; j++) {
                                    spierler[i].addKarte(set.pop());
                                }
                                ober = 0;
                            }
                        }
                    }
                    System.out.println("OK");
                    if(spierler[i].getHand().size() == 0){
                        System.exit(0);
                    }
                } else{
                    i--;
                }
                if (set.empty()) {
                    createSet();
                    sortKatemvonHand();
                }

            }
        }
    }


    public static void sortKatemvonHand(){
        for (int i = 0; i < SPIELERANZAHL; i++) {
            List<Karte> hand = spierler[i].getHand();
            Iterator<Karte> it = set.iterator();
            while (it.hasNext()){
                Karte k = it.next();
                for (int j = 0; j < hand.size(); j++) {
                    Karte kk = hand.get(j);
                    if(k.getVal() == kk.getVal() && k.getTrumpf() == kk.getTrumpf() ){
                        set.remove(k);
                    }
                }
            }
        }
    }
}