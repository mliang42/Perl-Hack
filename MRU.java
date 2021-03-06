package PageReplace;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static org.junit.Assert.*;

public class MRU extends LRU { //most recently used

	public MRU(List<Buffer> lst) {
		super(lst);
	}

	public MRU(Integer size) {
		super(size);
	}


	@Override
	public Buffer insert(Buffer buf) {
		int contains = checkIfContains(buf);
		int index;
        Buffer temp;
		if (contains == -1) {
			index = findMRUIndex(buf);
            temp = lst.get(index);
			lst.set(index, buf);
		} else {
			pagehits++;
            temp = buf;
			index = contains;
		}

		int previous = relativelst.get(index);
        relativelst.set(index, 1);
        for(int i = 0; i < lst.size(); i++) {
            if (relativelst.get(i) <= previous && i != index) {
                relativelst.set(i, relativelst.get(i)  + 1 );
            }
        }
        
        totalhits++;
        return buf;
	}

	public int findMRUIndex(Buffer buf) {
		for(int i = 0; i < relativelst.size(); i++) {
            if (lst.get(i).get_val() == null) {
                return i;
            }
        }
        for(int i = 0; i < relativelst.size(); i++) {
        	if (relativelst.get(i) == 1) { //Most recently used
                return i;
            }
        }
        return -1;
	}

	public static void main(String[] args) {
		MRU cache = new MRU(10);
		for(int i = 0; i < 10; i++) {
			cache.insert(new Buffer(i));
			System.out.println(cache);
		}

		cache.insert(new Buffer("haha!"));
		System.out.println(cache);

		cache.insert(new Buffer(5));
		System.out.println(cache);

		for(int i = 0; i < 1000; i++) {
            cache.insert(new Buffer(ThreadLocalRandom.current().nextInt(0, 1000)));
            System.out.println(cache);
        } 

        cache.insert(new Buffer(null)); //null can be inserted but does not count as a cache hit. Inserting null essentially is the same thing as freeing a buffer
        System.out.println(cache);
	}
}