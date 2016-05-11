//
// EVENTLIST.JAVA
// Skeleton code for your EventList collection type.
//
import java.util.*;

class EventList {
    
    Random randseq;
    
    public static Event head,tail,max;  
    public static int highest; //keep track the highest pillar

	////////////////////////////////////////////////////////////////////
    // Here's a suitable geometric random number generator for choosing
    // pillar heights.  We use Java's ability to generate random booleans
    // to simulate coin flips.
    ////////////////////////////////////////////////////////////////////
    
    int randomHeight()
    {
	int v = 1;
	while (randseq.nextBoolean()) { v++; }
	return v;
    }
    
    //
    // Constructor
    //
    public EventList()
    {
    randseq = new Random(58243); // You may seed the PRNG however you like.
    head = new Event(Integer.MIN_VALUE,"-oo");
	tail = new Event(Integer.MAX_VALUE,"+oo");	
	head.pillar = 1;
	tail.pillar = 1;
	head.next = new Event[1];
	for(int i = 0; i < 1; i++){
		head.next[i] = tail;
	}
	highest = 0;//Initialize head and tail,highest
	max = head;
    }
 
    @Override
	public String toString() {
    	String total = "";
    	for(Event i = head.next[0]; i != tail; i = i.next[0])
    		for(Event j = i; j!=null; j = j.nextEqual)
    			total+=j.toString()+"\n";	
    	//use a chain to store the events with the same year, 
    	//the end of the chain point to null xz
		return total;
	}

	//
    // Add an Event to the list.
    //
    public void insertSame(Event e,Event base){
    	if(base.nextEqual==null){
    		base.nextEqual = e;
    	}//if it's the first element except the base, add it to the end of the chain
    	//and it's nextEqual is null
    	else{
    		e.nextEqual = base.nextEqual;
    		base.nextEqual = e;
    	}//if it's not the 1st element, add it to the start of the chain
    	//between the base and the first
    } 
    
    public void doubleHeadTailHeight(int t){
    	int h;        
    	for(h =2*head.pillar; h<t;h=2*h);
    	Event[] headTemp = new Event[head.pillar];
    	
    	for(int i = 0; i < head.pillar; i++)
    		headTemp[i] = head.next[i];		
    	
    	head.next = new Event[h];
    	tail.next = new Event[h];
    	
    	for(int i = 0; i<head.pillar;i++)
    		head.next[i] = headTemp[i];	
    	//don't need to change other links because the pointers to head and tail don't change
    	for(int i = head.pillar; i <h; i++)
    		head.next[i]=tail;
   
    	head.pillar = h;
    	tail.pillar = h;
    }
    
    public void insert(Event e)
    {
    	Event equal = find(e.year);//use year to find,if find result is not null
    	if(equal!=null){
    		insertSame(e,equal);//chain it to the base, and end
    	}
    	else{
    	if(e.year>max.year)//max always points to the biggest node except the tail in the list
    		max=e;
    	int t = randomHeight(); //pillar height
    	if(t>head.pillar)
    		doubleHeadTailHeight(t);//double the head and tail's pillar height
    	e.pillar = t;
    	e.next = new Event[t];
    	
    	int l = Math.max(0, highest-1);//if highest=0(no node in the list,l=0
    	//if highest>0, l= highest-1
    	Event x = head; //start from the head
    	Event y;
    	while(l>=0){
    		y = x.next[l];
    		if(y.year<e.year)//y is not big enough,move x=y
    			x = y;  
    		else{
    			if(l < e.pillar){  //y is too big,l--,and link properly   
    				x.next[l] = e;
    				e.next[l] = y;
    				}	
    			l--; 
    			}    
    	}	
    	if(e.pillar>highest){ //exceed the original highest, link head and tail at this part
			for(int i = highest; i < e.pillar;i++){
				head.next[i] = e;
				e.next[i] = tail;
			}
			highest = e.pillar;	
    	}
    	}
    	}	
    
    public Event find(int year){
    	if(highest==0)//empty list, return null
    		return null;
    	int l = highest - 1;
    	Event x = head;//start from the head
    	Event y;
    	while(l>=0){
			y = x.next[l];
			if(y.year == year)//find, return
				return y;
    		else
    			if(y.year<year)//y is not big enough, move x to y
    				x=y;
    			else//y is too big, l--
    				l--;
			}	
    	return null;//l=0, and can't find it, return null
    }
    
    public Event findApproximate(int year){
    	if(highest==0)
    		return null;//empty return null
    	else{
    	int l = highest - 1;
    	Event x = head;//start from the head
    	Event y;
    	while(l>=0){
        	y = x.next[l];
        	if(y.year == year)
        		return y;//y.year=year
        	else
        		if(y.year<year)
        			x=y;
        		else
        			l--;
        	}
    		return x;//can't find the exact one,return the prev(year)
    	}//the return.year is always<=year
    }
    public Event findBiggerApproximate(int year){
    	if(highest==0)
    		return null;//empty return null
    	else{
    	int l = highest - 1;
    	Event x = head;//start from the head
    	Event y;
    	while(l>=0){
        	y = x.next[l];
        	if(y.year == year)
        		return y;//y.year=year
        	else
        		if(y.year<year)
        			x=y;
        		else
        			l--;
        	}
    		return x.next[0];//can't find the exact one,return the succ(year)
    	}//the return.year is always>=year
    }
  
    //
    // Remove all Events in the list with the specified year.
    //
    
    public void remove(int year)
    {
    	Event target = find(year);//find year.
    	if(target!=null){
    	int l = target.pillar;//start from l level
    	Event x = head;
    	Event y;
    	while(l>=0){
    		y = x.next[l];
    		if(y.year<year)
    			x = y;
    		else
    			if(y.year==year)//if we find y,remove y from level l
    				x.next[l]=y.next[l];
    			else
    				l--;
    	}
    	}
    }
    
    //
    // Find all events with greatest year <= input year
    //
    public Event [] returnChain(Event x){
    	List<Event> returnlist = new ArrayList<Event>();
    	for(Event i = x; i !=null; i = i.nextEqual)
    		returnlist.add(i);
    	Event[] chain = new Event[returnlist.size()];
    	returnlist.toArray(chain);//use arraylist to store the chain
    	return chain;
    }
    
    public Event [] findMostRecent(int year)
    {
    	//year is no less than the smallest in the list
    	if(year>=head.next[0].year){
    		Event x = findApproximate(year);//x.year<=year
    		return returnChain(x);
    	}
    	else//return null, because too small
    		return null;
    }
    
    
    //
    // Find all Events within the specific range of years (inclusive).
    //
    public Event [] findRange(int first, int last)
    {
    	List<Event> rangelist = new ArrayList<Event>();
    	if(first>max.year||last<head.next[0].year)
    		return null;//first is bigger than the biggest, or last is smaller than the smallest
    	else{
    		Event fevent = findBiggerApproximate(first);//fevent<=first
    		Event levent = findApproximate(last);//levent<=last
    		if(fevent==levent.next[0])
    			return null;
    			//return null;//list has 1699,1700,1702,want to find from 1701 to 1701
    		//fevent=1702, levent=1700, so null,find(1700,1700),fevent=levent=1700
    		for(Event i = fevent; i!=levent.next[0]; i=i.next[0])//from fevent to levent,
    			//and fevent!=levent
    			for(Event j = i; j !=null; j = j.nextEqual)//add the chain
    				rangelist.add(j);  			
    		Event[] range = new Event[rangelist.size()];
    		rangelist.toArray(range);	
    		return range;				
    		}
    	
    }
}
