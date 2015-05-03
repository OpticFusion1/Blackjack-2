public class Card{
  public enum Suit{ SPADES, HEARTS, CLUBS, DIAMONDS }
  public enum Value{ ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);
    public int weight;
    private Value(int weight){
      this.weight = weight;
    }
  }
  
  public Suit suit;
  public Value value;
  
  public Card(Value v, Suit s){
    this.suit = s;
    this.value = v;
  }
  
  @Override
  public boolean equals(Object o){
    if(o == this)
      return true;
    if(!(o instanceof Card))
      return false;
    
    Card c = (Card)o;
    if(c.value != this.value)
      return false;
    if(c.suit != this.suit)
      return false;
    
    return true;
  }
}