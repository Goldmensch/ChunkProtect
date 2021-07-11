import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Blocks {

    @Test
    public void test() {
        Set<Material> set = Arrays.stream(Material.values())
                .filter(material -> material.isBlock())
                .filter(material -> material.isInteractable())
                .collect(Collectors.toSet());

        for (var c : set) {
            System.out.println(c + ",");
        }
    }
}
