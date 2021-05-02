package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs;

import java.util.Scanner;

/* Class PatriciaTrie Test */
public class PatriciaTrieTest {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    /* Creating object of PatriciaTrie */
    PatriciaTrie pt = new PatriciaTrie();
    System.out.println("Patricia Trie Test\n");

    char ch;
    /* Perform trie operations */
    do {
      System.out.println("\nPatricia Trie Operations\n");
      System.out.println("1. insert ");
      System.out.println("2. search");
      System.out.println("3. check empty");
      System.out.println("4. make empty");

      int choice = scan.nextInt();
      switch (choice) {
        case 1:
          System.out.println("Enter key element to insert");
          pt.insert(scan.nextInt());
          break;
        case 2:
          System.out.println("Enter key element to search");
          System.out.println("Search result : " + pt.search(scan.nextInt()));
          break;
        case 3:
          System.out.println("Empty Status : " + pt.isEmpty());
          break;
        case 4:
          System.out.println("Patricia Trie cleared");
          pt.makeEmpty();
          break;
        default:
          System.out.println("Wrong Entry \n ");
          break;
      }

      System.out.println("\nDo you want to continue (Type y or n) \n");
      ch = scan.next().charAt(0);
    } while (ch == 'Y' || ch == 'y');
  }
}
