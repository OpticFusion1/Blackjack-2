import java.util.*;

public class Blackjack{
  /*Global Variables*/
  public enum Suit{ SPADES, HEARTS, CLUBS, DIAMONDS }
  public enum Value{ ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
    private int weight;
    private Value(int weight){
      this.weight = weight;
    }
  }
  private ArrayList<Card> _deck;
  private ArrayList<Card> _discard;
  private ArrayList<Card> _hand;
  private ArrayList<Card> _dealer;
  private int _handCount;
  private int _dealerCount;
  
  /**
   * Card objects are used to determine game progress.
   * They each have a Suit and Value.
   */
  public class Card{
    public Suit suit;
    public Value value;
    public Card(Value v, Suit s){
      this.suit = s;
      this.value = v;
    }
  }
  
  public Blackjack(){
    _deck = new ArrayList<Card>();
    for (Suit s : Suit.values()){
      for (Value v : Value.values()){
        _deck.add(new Card(v, s));
      }
    }
    _discard = new ArrayList<Card>();
    _hand = new ArrayList<Card>();
    _dealer = new ArrayList<Card>();
  }
  
  public void shuffleDeck(){
    Collections.shuffle(_deck);
  }
  
  public static void welcomeMessage(){
    System.out.println("+----------------------------------------------+");
    System.out.println("|             LET'S PLAY BLACKJACK!            |");
    System.out.println("| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ |");
    System.out.println("| RULES:                                       |");
    System.out.println("| 1. Dealer draws to 16, and stands on all 17s |");
    System.out.println("| 2. Blackjack pays 3 to 2                     |");
    //System.out.println("| 3. Maximum bet is 200                        |");
    System.out.println("+----------------------------------------------+");
  }
  
  public static void main(String args[]){
    boolean playAgain = false;
    welcomeMessage();
    while(playAgain){
      Blackjack game = new Blackjack();
      game.shuffleDeck();
      
    }
  }
}