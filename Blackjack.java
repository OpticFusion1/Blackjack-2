import java.util.*;

/**
 * TODO:
 * Prompt user to hit or stay.
 */

public class Blackjack{
  public enum Suit{ SPADES, HEARTS, CLUBS, DIAMONDS }
  public enum Value{ ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
    public int weight;
    private Value(int weight){
      this.weight = weight;
    }
  }
  public enum Action{ HIT("hit"), STAY("stay");
    public String action;
    private Action(String action){
      this.action = action;
    }
  }
  
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
  
  /*Global Variables*/
  private ArrayList<Card> _deck; //an ordered collection of Cards
  private ArrayList<Card> _discard; //Cards from previous hand go here
  private ArrayList<Card> _hand; //your Cards
  private ArrayList<Card> _dealer; //dealer's Cards
  private int _handCount; // your card total
  private int _dealerCount; //dealer's card total
  
  
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
    _handCount = 0;
    _dealerCount = 0;
  }
  
  public void shuffleDeck(){
    Collections.shuffle(_deck);
  }
  
  public void hitMe(){
    Card card = _deck.remove(0);
    if(card.value == Value.ACE && _handCount + 11 <= 21){
      card.value.weight = 11;
    }
    _hand.add(card);
    _handCount += card.value.weight;
    
    if(checkCards()){
      hitDealer();
    }
  }
  
  public void hitDealer(){
    Card dealerCard = _deck.remove(0);
    _dealer.add(dealerCard);
    _dealerCount += dealerCard.value.weight;
  }
  
  public boolean checkCards(){
    return true;
  }
  
  public void startRound(){
    this.shuffleDeck();
    this.hitMe();
    this.hitMe();
    this.printCount();
  }
  
  public void printCount(){
    System.out.println("Your count is: " + _handCount);
  }
  
  public static void welcomeMessage(){
    System.out.println("+----------------------------------------------+");
    System.out.println("|             LET'S PLAY BLACKJACK!            |");
    System.out.println("| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ |");
    System.out.println("| RULES:                                       |");
    System.out.println("| 1. Dealer draws to 16, and stands on all 17s |");
    System.out.println("| 2. Blackjack pays 3 to 2                     |");
    System.out.println("+----------------------------------------------+\n");
  }
  
  public static void main(String args[]){
    Scanner sc = new Scanner(System.in);
    boolean playAgain = false;
    welcomeMessage();
    
    do{
      Blackjack game = new Blackjack();
      game.startRound();
      //PROMPT FOR ACTION
      
    }while(playAgain);
  }
}
