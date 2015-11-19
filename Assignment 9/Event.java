    public class Event implements Comparable<Event> {
        private int schedtime;
        private Cons act;
        public Event(Cons action, int time) {
            schedtime = time;
            act = action; }
        public int time() { return schedtime; }
        public Cons action() { return act; }
        // return -1 to sort this account before x, else 1
        public int compareTo(Event x) {
            return ( this.schedtime - x.schedtime ); }
    }

