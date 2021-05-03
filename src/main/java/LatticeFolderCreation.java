import java.io.File;
import java.util.*;

public class LatticeFolderCreation {

    

    public  Set<Set<String>> generatePowerSet(Set<String> ints) {
        // convert set to a list
        List<String> S = new ArrayList<>(ints);

        // `N` stores the total number of subsets
        long N = (long) Math.pow(2, S.size());

        // Set to store subsets
        Set<Set<String>> result = new HashSet<>();

        // generate each subset one by one
        for (int i = 0; i < N; i++) {
            Set<String> set = new HashSet<>();

            // check every bit of `i`
            for (int j = 0; j < S.size(); j++) {
                // if j'th bit of `i` is set, add `S[j]` to the current set
                if ((i & (1 << j)) != 0) {
                    set.add(S.get(j));
                }
            }
            result.add(set);
        }

        return result;
    }
    public void generateLatticeNameFolder(Set<Set<String>> powerSet , String dbname)
    {
        Set<String> atr_name = new HashSet<>();
        Iterator<Set<String>> itr = powerSet.iterator();
        while(itr.hasNext())
        {
            String atr= "lattice";
            for(String name : itr.next())
            {
                atr=""+atr+name;
            }
            atr_name.add(atr);
            File file = new File("src/main/resources/"+atr_name);
            boolean bool = file.mkdir();
        }
    }
}
