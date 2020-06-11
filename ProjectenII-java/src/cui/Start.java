package cui;

import domein.DomeinController;
import domein.InlogController;

public class Start {
    public static void main(String[] args) {

        InlogController dc = new InlogController();
        System.out.println(dc.login("789456bg", "P@ssword1"));
    }
}
