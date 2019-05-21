import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Client {

    private static LinkedList<Karte> hand;
    public static void main(String[] args) {


        try {
            Socket client = new Socket("10.216.122.101", 4444);


            ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

            while (true) {

                hand = (LinkedList<Karte>) inFromServer.readObject();

                hand.stream().forEach(x -> System.out.println(x));

                Karte karte = (Karte) inFromServer.readObject();
                System.out.println("-------- "+ getTrumpf(karte)+"-"+getVal(karte));

                Karte ret = consolOut();

                if(ret.getVal() == 9){
                    wunsch(ret);
                }


                outToServer.writeObject(ret);
                hand = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    private static Karte consolOut(){
        int i;
        for ( i = 1; i < hand.size(); i++) {
            System.out.println(i+" --- "+ getTrumpf(hand.get(i))+"-"+getVal(hand.get(i)));
        }
        System.out.println(i+" --- Kann nicht");
        System.out.print("Welche Karte Spielen?  ");
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        if(a == i){
            return new Karte(0,'0');
        }

        return hand.get(a);
    }

    private static void wunsch(Karte k){
        System.out.print("Welchen Schlag wünschen sie");
        System.out.println("(E)ichel | (S)chell | (H)erz | (L)ab");
        Scanner scanner = new Scanner(System.in);
        k.setWünschen(scanner.next().toLowerCase().charAt(0));
    }

    private static String getTrumpf(Karte karte){
        String ret = "";

        switch (karte.getTrumpf()){
            case 'h':ret = "Herz";break;
            case 'l':ret = "Lab";break;
            case 's':ret = "Schell";break;
            case 'e':ret = "Oachel";break;
        }
        return ret;
    }


    private static String getVal(Karte karte){
        String ret = "";
        switch (karte.getVal()){
            case 6:ret = "Sechs";break;
            case 7:ret = "Sieben";break;
            case 8:ret = "Ocht";break;
            case 9:ret = "Nein";break;
            case 10:ret = "Zehn";break;
            case 11:ret = "Unter";break;
            case 12:ret = "Ober";break;
            case 13:ret = "Kini";break;
            case 14:ret = "Ass";break;
        }
        return ret;
    }
}