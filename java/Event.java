//
// EVENT.JAVA
//
// Describes a historical event via two public fields:
//  year        -- the year of the event (an integer)
//  description -- the text for the event (a String)
//

class Event {
    
    public int year;             // the year of the event
    public String description;   // the event description
    public Event[] next;  
    public Event nextEqual; //chain all the same year
    public int pillar; //pillar height
 
    
    // constructor
    public Event(int iyear, String idescription)
    {
	year = iyear;
	description = idescription;
	pillar = 0;
    }
    
    // print method
    public String toString()
    {
    	return String.valueOf(year) + " " + description;
    }

}
