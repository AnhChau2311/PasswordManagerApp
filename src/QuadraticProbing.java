public class QuadraticProbing implements ProbingStrategy{
    @Override
    public int nextProbe(int h, int i, int capacity){
        return (h+i*i)%capacity;
    }
}
