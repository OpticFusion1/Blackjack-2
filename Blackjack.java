import java.util.*;

/**
 * TODO:
 * Add COMMENTS!
 * Detailed win/lose stats.
 * Add betting system.
 */

public class Blackjack{
  public enum State{ START, HIT, STAY, BLACKJACK, WIN, LOSE, BUST, CONTINUE }
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
    @Override
    public boolean equals(Object o){
      if(o == this)  return true;
      if(!(o instanceof Card))  return false;
      Card c = (Card)o;
      if(c.value != this.value) return false;
      if(c.suit != this.suit)   return false;
      return true;
    }
  }
  
  /*Global Variables*/
  private ArrayList<Card> _deck; //an ordered collection of Cards
  private ArrayList<Card> _discard; //Cards from previous hand go here
  private ArrayList<Card> _hand; //your Cards
  private ArrayList<Card> _dealer; //dealer's Cards
  private Card _hiddenCard;
  private int _handCount; // your card total
  private int _dealerCount; //dealer's card total
  private int _dealerShowing;
  
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
    _hiddenCard = null;
    _handCount = 0;
    _dealerCount = 0;
    _dealerShowing = 0;
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
    if(_dealer.size() == 1)
      _hiddenCard = card;
    _dealerCount += card.value.weight;
    _dealerShowing = _dealerCount - _hiddenCard.value.weight;
  }
  
  /**
   * Reduces ACE weights where necessary.
   * This method checks each hand for a bust,
   * and tries to reduce ACE weights appropriately.
   * If the user's hand is good, then the dealer's
   * hand is checked in the same manner.
   * @return false if bust, true otherwise
   */
  public boolean checkDealer(){
    if(_dealerCount > 21){
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
  
  public boolean checkMe(){
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
    }
    return true;
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
    if(_deck.size() < 26){
      while(_discard.size() != 0){
        _deck.add(_discard.get(0));
        _discard.remove(0);
      }
      assert _discard.size() == 0;
      shuffleDeck();
    }
  }
  
  public void printMe(){
    System.out.println("Dealer's showing " + _dealerShowing);
    for(Card card : _dealer){
      if(!card.equals(_hiddenCard))
        System.out.print(card.value + " of " + card.suit + ";  ");
    }
    System.out.println("\nYou're at " + _handCount);
    for(Card card : _hand){
      System.out.print(card.value + " of " + card.suit + ";  ");
    }
    System.out.println("\n");
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
    System.out.println();
  }
  
  //used for dealer, which thinks independently of you
  //returns WIN, LOSE, CONTINUE, or STAY
  public State updateDealer(State current){
    switch(current){
      case START: // change to CONTINUE
        return updateDealer(State.CONTINUE);
      case CONTINUE: // decide to HIT or STAY
        if(_dealerCount < 17 || _dealer.size() < 2){
          return updateDealer(State.HIT);
        }
        return State.STAY;
      case HIT: // take a card and return BLACKJACK, LOSE or CONTINUE
        hitDealer();
        if(_dealer.size() == 2 && _dealerCount == 21)
          return updateDealer(State.BLACKJACK);
        if(checkDealer()) // false if bust, true otherwise
          return State.CONTINUE;
        else
          return State.LOSE;
      case STAY: // check other state, return STAY, WIN, or LOSE
        return State.STAY;
      case BLACKJACK:
        return State.WIN;
      case LOSE: //do nothing
        return State.LOSE;
      case WIN: //do nothing
        return State.WIN;
      case BUST: //change state to
        return State.LOSE;
      default:
        return State.CONTINUE;
    }
  }
  
  //used by you, because your state is affected by the dealer's
  public State updateMe(State current, State dealer){
    switch(current){
      case START: // change to CONTINUE
        return updateMe(State.CONTINUE, dealer);
      case CONTINUE: // decide to HIT or STAY. if CONTINUE, and choose HIT, assume call hitMe() and change me to HIT
        if(_hand.size() < 2){
          hitMe();
          return updateMe(current, dealer);
        }
        return State.CONTINUE;
      case HIT: // take a card and return BUST or CONTINUE
        if(_hand.size() == 2 && _handCount == 21)
          return updateMe(State.BLACKJACK, dealer);
        if(checkMe()) // false if bust, true otherwise
          return State.CONTINUE;
        else
          return State.LOSE;
      case STAY: // check other state, return STAY, WIN, or LOSE
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
      case BUST:
        return State.LOSE;
      case BLACKJACK:
        return State.WIN;
      default:
        return State.CONTINUE;
    }
  }
  
  public static void main(String args[]){
    Scanner scanner = new Scanner(System.in);
    String response;
    State me = State.START;
    State dealer = State.START;
    welcomeMessage();
    
    Blackjack game = new Blackjack();
    game.shuffleDeck();
    
    while(me == State.START){
      dealer = game.updateDealer(dealer);
      me = game.updateMe(me, dealer);
      
      while(me == State.CONTINUE || me == State.STAY){
        dealer = game.updateDealer(dealer);
        me = game.updateMe(me, dealer);
        game.printMe();
        
        if(me == State.CONTINUE){
          System.out.print("Do you want to hit? 'yes' or 'no': ");
          response = scanner.next().toLowerCase();
          if(response.equals("y") || response.equals("yes")){
            game.hitMe();
            me = game.updateMe(State.HIT, dealer);
          }
          else{
            me = game.updateMe(State.STAY, dealer);
          }
        }
      }
      
      if(me == State.LOSE){
        System.out.println("\nYou lost!\n");
      }
      else if(me == State.WIN){
        System.out.println("\nYou won!\n");
      }
      game.discard();
      game.debug();
      
      System.out.print("Do you want to play another round? ");
      response = scanner.next().toLowerCase();
      if(response.equals("y") || response.equals("yes")){
        me = State.START;
        dealer = State.START;
      }
    }
  }
}
