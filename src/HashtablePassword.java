public class HashtablePassword {
    private ProbingStrategy probingStrategy;
    private Entry[] entries;
    private float loadFactor;
    private int size = 0, used = 0;
    private Entry NIL = new Entry(null, null);

    public HashtablePassword(int capacity, float loadFactor, ProbingStrategy strategy){
        this.entries = new Entry[capacity];
        this.loadFactor = loadFactor;
        this.probingStrategy = strategy;
    }

    private int hash(Object key){
        return (key.hashCode() & 0x7FFFFFFF)% entries.length;
    }

    public void rehash(){
        Entry[] oldEntries = entries;
        entries = new Entry[2 * entries.length + 1];
        size = 0;
        used = 0;
        for (Entry entry : oldEntries){
            if(entry != null & entry != NIL){
                add(entry.key, entry.value);
            }
        }
    }

    public int add(Object key, Object value){
        if(used > loadFactor * entries.length){
            rehash();
        }
        int h = hash(key);
        for(int i = 0; i < entries.length; i++){
            int j = probingStrategy.nextProbe(h, i, entries.length);
            Entry entry = entries[j];
            if(entry == null){
                entries[j] = new Entry(key, value);
                size++;
                used++;
                return j;
            }
            if(entry == NIL){
                continue;
            }
            if(entry.key.equals(key)){
                entries[j].value = value;
                return j;
            }
        }
        return -1;
    }

    public Object get(Object key){
        int h = hash(key);
        for (int i = 0; i < entries.length; i++){
            int j = probingStrategy.nextProbe(h, i, entries.length);
            Entry entry = entries[j];
            if (entry == null) break;
            if (entry == NIL) continue;
            if (entry.key.equals(key)) return entry.value;
        }
        return null;
    }


    public Object remove(Object key){
        int h = hash(key);
        for (int i = 0; i < entries.length; i++){
            int j = probingStrategy.nextProbe(h,i,entries.length);
            Entry entry = entries[j];
            if (entry == null) break;
            if (entry == NIL) continue;
            if (entry.key.equals(key)){
                Object value = entry.value;
                entries[j] = NIL;
                size--;
                return value;
            }
        }
        return null;
    }
}
