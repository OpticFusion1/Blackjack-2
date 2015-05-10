import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * TODO:
 * Add betting system.
 * Add 'help' message.
 * Add SPLIT feature.
 */

/**
 * A program for playing blackjack. Compiling and executing this
 * program will begin a new game through the console.
 * 
 * @author Paul Lohmuller
 */
public class Blackjack{
  /**
   * The different states of play in this blackjack game.
   * Each player decision and consequence is implemented
   * as a State.
   */
  public enum State{ START, HIT, STAY, BLACKJACK, WIN, LOSE, BUST, CONTINUE }
  
  /**
   * The four card suits. These are used for deck generation
   * and card comparison.
   */
  public enum Suit{ SPADES, HEARTS, CLUBS, DIAMONDS }
  
  /**
   * The thirteen card ranks. Each has a weight value for scoring.
   * These are used for deck generation and card comparison. Rank
   * weights can be modified (e.g. when an Ace with weight 11 drops
   * to an Ace with weight 1 to prevent a bust).
   */
  public enum Rank{ ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
  
    public int weight;
    /**
     * Create a Rank instance and set the corresponding weight.
     * @param weight the scoring value of the Rank.
     */
    private Rank(int weight){
      this.weight = weight;
    }
  }
  
  /**
   * Card objects are used to determine game progress.
   */
  public class Card{
    public Suit suit;
    public Rank rank;
    /**
     * Create a Card with suit and rank.
     * @param r the card rank.
     * @param s the card suit.
     */
    public Card(Rank r, Suit s){
      this.suit = s;
      this.rank = r;
    }
    /**
     * Overridden comparison method.
     * Compares cards by rank, then suit.
     * @return true if equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o){
      if(o == this)  return true;
      if(!(o instanceof Card))  return false;
      Card c = (Card)o;
      if(c.rank != this.rank) return false;
      if(c.suit != this.suit)   return false;
      return true;
    }
  }
  
  /**
   * VARIABLES
   */
  private ArrayList<Card> _deck; //an ordered collection of cards
  private ArrayList<Card> _discard; //cards from previous hands go here
  private ArrayList<Card> _hand; //your cards
  private ArrayList<Card> _dealer; //dealer's cards
  private Card _hiddenCard; //the dealer's first card
  private int _handCount; //your card total
  private int _dealerCount; //dealer's card total
  private int _dealerShowing; //the dealer's face-up card total
  
  /**
   * This creates an ordered deck of 52 cards, 
   * your hand, the dealer's hand, and the discard pile.
   * It also initializes your score and the dealer's
   * score to zero.
   */
  public Blackjack(){
    _deck = new ArrayList<Card>();
    for (Suit s : Suit.values()){
      for (Rank v : Rank.values()){
        _deck.add(new Card(v, s));
      }
    }
    _discard = new ArrayList<Card>();
    _hand = new ArrayList<Card>();
    _dealer = new ArrayList<Card>();
    _hiddenCard = null;
    _handCount = 0;
    _dealerCount = 0;
    _dealerShowing = 0;
  }
  
  /**
   * Randomizes the order of the card deck.
   */
  public void shuffleDeck(){
    Collections.shuffle(_deck);
  }
  
  /**
   * Adds a card to your hand.
   * Also accordingly updates your count.
   */
  public void hitMe(){
    Card card = _deck.remove(0);
    if(card.rank == Rank.ACE && _handCount + 11 <= 21){
      card.rank.weight = 11;
    }
    _hand.add(card);
    _handCount += card.rank.weight;
  }
  
  /**
   * Adds a card to the dealer's hand.
   * If the card is the first card, it is also the face-down card.
   * The dealer's score and showing score are accordingly updated.
   */
  public void hitDealer(){
    Card card = _deck.remove(0);
    if(card.rank == Rank.ACE && _dealerCount + 11 <= 21){
      //this makes Aces hard if possible, as default is soft
      card.rank.weight = 11;
    }
    _dealer.add(card);
    if(_dealer.size() == 1) //if first card added, it is face-down
      _hiddenCard = card;
    _dealerCount += card.rank.weight;
    _dealerShowing = _dealerCount - _hiddenCard.rank.weight;
  }
  
  /**
   * Reduces dealer's Ace weights where necessary.
   * This method checks the dealer's hand for a bust,
   * and makes any hard Aces into soft Aces.
   * @return false if bust, true otherwise
   */
  public boolean checkDealer(){
    if(_dealerCount > 21){
      for(Card card : _dealer){
        if(card.rank == Rank.ACE && card.rank.weight == 11 && _dealerCount > 21){
          card.rank.weight = 1;
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
   * Reduces your Ace weights where necessary.
   * This method checks your hand for a bust,
   * and makes any hard Aces into soft Aces.
   * @return false if bust, true otherwise
   */
  public boolean checkMe(){
    if(_handCount > 21){
      for(Card card : _hand){
        if(card.rank == Rank.ACE && card.rank.weight == 11 && _handCount > 21){
          card.rank.weight = 1;
          _handCount -= 10;
        }
      }
      if(_handCount > 21){
        return false;
      }
    }
    return true;
  }
  
  /**
   * Puts all cards from your hand and the dealer's
   * hand into the discard pile. If more than half
   * the deck has been discarded, this shuffles
   * the discard pile back into the deck.
   */
  public void discard(){
    while(_hand.size() != 0){
      _discard.add(_hand.get(0));
      _hand.remove(0);
    }
    while(_dealer.size() != 0){
      _discard.add(_dealer.get(0));
      _dealer.remove(0);
    }
    _handCount = 0;
    _dealerCount = 0;
    if(_deck.size() < 26){
      while(_discard.size() != 0){
        _deck.add(_discard.get(0));
        _discard.remove(0);
      }
      shuffleDeck();
    }
  }
  
  /**
   * Prints both scores and a table update. Only prints
   * the dealer's face-up cards and score.
   */
  public void printMe(){
    System.out.println("Dealer's showing " + _dealerShowing);
    for(Card card : _dealer){
      //don't print the face-down card!
      if(!card.equals(_hiddenCard))
        System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println("\n\nYou're at " + _handCount);
    for(Card card : _hand){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println("\n");
  }
  
  /**
   * A simple welcome text with house rules.
   */
  public static void welcomeMessage(){
    System.out.println("+----------------------------------------------+");
    System.out.println("|             LET'S PLAY BLACKJACK!            |");
    System.out.println("| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ |");
    System.out.println("| RULES:                                       |");
    System.out.println("| 1. Dealer draws to 16, and stands on all 17s |");
    //System.out.println("| 2. Blackjack pays 3 to 2                     |");
    System.out.println("+----------------------------------------------+\n");
  }
  
  /**
   * Prints the contents of your hand, your score, 
   * the dealer's hand, the dealer's score, and the
   * discard pile.
   */
  public void debug(){
    System.out.println("| DEBUG");
    System.out.println("| --- YOUR HAND ---");
    System.out.println("| Your count is: " + _handCount);
    System.out.print("| ");
    for(Card card : _hand){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println("\n|\n| --- DEALER'S HAND ---");
    System.out.println("| Dealer's count is: " + _dealerCount);
    System.out.print("| ");
    for(Card card : _dealer){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println("\n|\n|  --- DISCARD PILE ---");
    System.out.print("| ");
    for(Card card : _discard){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println();
  }
  
  /**
   * Prints the final results of the hand. First
   * it prints the dealer's score and hand, and then
   * it prints your score and hand. It then states
   * who won.
   */
  public void printResults(){
    System.out.println("Final results:");
    
    //Print the scores and cards
    System.out.println("Dealer's at " + _dealerCount);
    for(Card card : _dealer){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println("\n\nYou're at " + _handCount);
    for(Card card : _hand){
      System.out.print(card.rank + " of " + card.suit + ";  ");
    }
    System.out.println();
    
    //Decide who won the game and print a unique message for each scenario
    if(_handCount > 21){
      System.out.println("You lose! You went over 21. That's called a bust. Hint: You don't want to do this.");
    }else if(_dealerCount > 21){
      System.out.println("You win! The dealer went over 21. Point your finger at the screen and laugh at it. You've earned it.");
    }else if(_dealerCount == 21){
      System.out.print("You lose! ");
      if(_dealer.size() == 2){
        System.out.println("Dealer has blackjack. The system must be rigged.");
      }else{
        System.out.println("Dealer has 21. Better luck next time...");
      }
    }else if(_handCount == 21){
      System.out.print("You win! ");
      if(_hand.size() == 2){
        System.out.println("Blackjack! Way to go! Maybe you should quit while you're ahead.");
      }else{
        System.out.println("You have 21. The dealer didn't. You should be proud of yourself.");
      }
    }else if(_handCount <= _dealerCount){
      System.out.println("You lose! You have less than the dealer. That's life.");
    }else{
      System.out.println("You win! You have more than the dealer. A small victory.");
    }
  }
  
  /**
   * Advances the dealer's state and evaluates the dealer's hand.
   * This method handles the dealer's state with a switch statement,
   * and applies the appropriate action in each case. Returns the
   * new dealer state which can be the same.
   * @param current the dealer's current state
   * @return the dealer's new state, which is either WIN, LOSE, CONTINUE, or STAY.
   */
  public State updateDealer(State current){
    switch(current){
      case START: //change to CONTINUE
        return updateDealer(State.CONTINUE);
        
      case CONTINUE: //decide to HIT or STAY
        if(_dealerCount < 17 || _dealer.size() < 2)
          return updateDealer(State.HIT);
        return State.STAY;
        
      case HIT: //take a card and return WIN, LOSE or CONTINUE
        hitDealer();
        if(_dealer.size() == 2 && _dealerCount == 21)
          return updateDealer(State.BLACKJACK);
        if(checkDealer()) // false if bust, true otherwise
          return State.CONTINUE;
        else
          return State.LOSE;
        
      case STAY: //do nothing
        return State.STAY;
        
      case BLACKJACK: //change state to WIN
        return State.WIN;
        
      case LOSE: //do nothing
        return State.LOSE;
        
      case WIN: //do nothing
        return State.WIN;
        
      case BUST: //change state to LOSE
        return State.LOSE;
        
      default:
        return State.CONTINUE;
    }
  }
  
  /**
   * Advances your state and evaluates your hand.
   * This method handles your state with a switch statement,
   * and applies the appropriate action in each case. Returns
   * your new state which can be the same.
   * @param current your current state
   * @param dealer the dealer's current state
   * @return your new state, which is either WIN, LOSE, CONTINUE, or STAY.
   */
  public State updateMe(State current, State dealer){
    switch(current){
      case START: //update to CONTINUE
        return updateMe(State.CONTINUE, dealer);
        
      case CONTINUE:
        if(_hand.size() < 2){
          //this makes sure there are two cards in your hand when you start
          hitMe();
          return updateMe(current, dealer);
        }
        return State.CONTINUE;
        
      case HIT: // take a card and return WIN, LOSE, or CONTINUE
        hitMe();
        if(_hand.size() == 2 && _handCount == 21)
          return updateMe(State.BLACKJACK, dealer);
        if(checkMe()) // false if bust, true otherwise
          return State.CONTINUE;
        else
          return State.LOSE;
        
      case STAY: // check dealer's state, return STAY, WIN, or LOSE
        if(dealer == State.WIN)
          return State.LOSE;
        else if(dealer == State.LOSE)
          return State.WIN;
        else if(dealer == State.STAY)
          return _handCount > _dealerCount ? State.WIN : State.LOSE;
        else
          return State.STAY;
        
      case LOSE: //do nothing
        return State.LOSE;
        
      case WIN: //do nothing
        return State.WIN;
        
      case BUST: //change to LOSE
        return State.LOSE;
        
      case BLACKJACK: //change to WIN
        return State.WIN;
        
      default:
        return State.CONTINUE;
    }
  }
  
  /**
   * Play a game of blackjack. This creates a new Blackjack object,
   * your play state, and the dealer's play state. It handles all
   * user interaction through System.in and System.out.
   */
  public static void main(String args[]){
    Scanner scanner = new Scanner(System.in);
    String response; //user input
    State me = State.START;
    State dealer = State.START;
    welcomeMessage();
    
    Blackjack game = new Blackjack();
    game.shuffleDeck();
    
    while(me == State.START){
      //This is the beginning of each hand.
      dealer = game.updateDealer(dealer);
      me = game.updateMe(me, dealer);
      
      while(me == State.CONTINUE || me == State.STAY){
        dealer = game.updateDealer(dealer);
        me = game.updateMe(me, dealer);
        
        if(me == State.CONTINUE){
          game.printMe();
          //prompt user to HIT or STAY
          System.out.print("Do you want to hit? 'yes' or 'no': ");
          response = scanner.next().toLowerCase();
          if(response.equals("y") || response.equals("yes")){
            me = game.updateMe(State.HIT, dealer);
          }else{
            me = game.updateMe(State.STAY, dealer);
          }
        }
        //The hand is done
      }
      
      //print the outcome
      game.printResults();
      game.discard();
      //game.debug();
      
      //prompt for another hand, or quit
      System.out.print("Do you want to play another hand? ");
      response = scanner.next().toLowerCase();
      if(response.equals("y") || response.equals("yes")){
        me = State.START;
        dealer = State.START;
      }
    }
  }
}
