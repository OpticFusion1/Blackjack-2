import java.util.*;

/**
 * TODO:
 * Implement checkCards, checkBust, checkWin etc in main()....
 */

public class Blackjack{
  public enum State{ HIT, STAY, BLACKJACK, WIN, LOSE, BUST, CONTINUE }
  public enum Suit{ SPADES, HEARTS, CLUBS, DIAMONDS }
  public enum Value{ ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
    public int weight;
    private Value(int weight){
      this.weight = weight;
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
  private State _roundState;
  
  /**
   * Initializes a game of blackjack.
   * This creates a full ordered deck of cards, 
   * your hand, the dealer's hand, and the discard pile.
   * It also initializes your count and the dealer's
   * count to zero.
   */
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
    _roundState = State.CONTINUE;
  }
  
  /**
   * Sets the current round's play state.
   * @param state the new play state
   */
  public void changeRoundState(State state){
    _roundState = state;
  }
  
  /**
   * Randomizes the order of the card deck
   */
  public void shuffleDeck(){
    Collections.shuffle(_deck);
  }
  
  /**
   * Adds a card to your hand (and possibly the dealer's).
   * Also appropriately updates your count (and possibly the dealer's)
   */
  public void hitMe(){
    Card card = _deck.remove(0);
    if(card.value == Value.ACE && _handCount + 11 <= 21){
      card.value.weight = 11;
    }
    _hand.add(card);
    _handCount += card.value.weight;
    
    debug();
  }
  
  /**
   * Adds a card to the dealer's hand.
   * Also appropriately updates the dealer's count.
   */
  public void hitDealer(){
    Card card = _deck.remove(0);
    if(card.value == Value.ACE && _dealerCount + 11 <= 21){
      card.value.weight = 11;
    }
    _dealer.add(card);
    _dealerCount += card.value.weight;
  }
  
  /**
   * Reduces ACE weights where necessary.
   * This method checks each hand for a bust,
   * and tries to reduce ACE weights appropriately.
   * If the user's hand is good, then the dealer's
   * hand is checked in the same manner.
   * @return false if bust, true otherwise
   */
  public boolean reduceWeights(){
    if(_handCount > 21){
      for(Card card : _hand){
        if(card.value == Value.ACE && card.value.weight == 11 && _handCount > 21){
          card.value.weight = 1;
          _handCount -= 10;
        }
      }
      if(_handCount > 21){
        return false;
      }
    }else if(_dealerCount > 21){
      for(Card card : _dealer){
        if(card.value == Value.ACE && card.value.weight == 11 && _dealerCount > 21){
          card.value.weight = 1;
          _dealerCount -= 10;
        }
      }
      if(_dealerCount > 21){
        return false;
      }
    }
    return true;
  }
  
  /**
   * Checks if either hand has bust, and changes state if so.
   * @return true if either hand has bust, false otherwise.
   */
  public boolean checkBust(){
    if(_handCount > 21){
      _roundState = State.BUST;
      return true;
    }else if(_dealerCount > 21){
      _roundState = State.WIN;
      return true;
    }else{
      return false;
    }
  }
  
  public State checkWin(){
    if(_dealerCount == 21 && _dealer.size() == 2){
      return State.LOSE;
    }else if(_handCount == 21 && _hand.size() == 2){
      return State.BLACKJACK;
    }else if(_handCount > 21){
      return State.BUST;
    }else if(_dealerCount > 21){
      return State.WIN;
    }else{
      return State.STAY;
    }
  }
  
  public void discard(){
    while(_hand.size() != 0){
      _discard.add(_hand.get(0));
      _hand.remove(0);
    }
    while(_dealer.size() != 0){
      _discard.add(_dealer.get(0));
      _dealer.remove(0);
    }
    assert _hand.size() == 0;
    assert _dealer.size() == 0;
    _handCount = 0;
    _dealerCount = 0;
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
  
  public void debug(){
    System.out.println("--- YOUR HAND ---");
    printCount();
    for(Card card : _hand){
      System.out.print(card.value + " of " + card.suit + ";  ");
    }
    System.out.println("\n\n--- DEALER'S HAND ---");
    System.out.println("Dealer's count is: " + _dealerCount);
    for(Card card : _dealer){
      System.out.print(card.value + " of " + card.suit + ";  ");
    }
    System.out.println("\n\n--- DISCARD PILE ---");
    for(Card card : _discard){
      System.out.print(card.value + " of " + card.suit + ";  ");
    }
    System.out.println("\n\n");
  }
  
  public static void main(String args[]){
    Scanner scanner = new Scanner(System.in);
    String response;
    State state;
    State cardCheck;
    boolean playAgain;
    welcomeMessage();
    
    Blackjack game = new Blackjack();
    game.shuffleDeck();
    do{
      playAgain = false;
      game.hitMe();
      game.hitDealer();
      cardCheck = game.checkWin();
      state = State.HIT;
      while(state != State.STAY){
        if(state == State.HIT){
          game.hitMe();
          game.printCount();
        }
        
        System.out.print("Would you like to hit? 'y' or 'n': ");
        response = scanner.next().toLowerCase();
        if(response.equals("y") || response.equals("yes")){
          state = State.HIT;
        }else if(response.equals("n") || response.equals("no")){
          state = State.STAY;
        }else{
          state = State.CONTINUE;
        }
      }
      
      System.out.print("Do you want to play another round? ");
      response = scanner.next().toLowerCase();
      if(response.equals("y") || response.equals("yes")){
        playAgain = true;
        game.discard();
      }
    }while(playAgain);
  }
}
