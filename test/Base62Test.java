import org.junit.Assert;
import org.junit.Test;
import services.Base62;

public class Base62Test {

    @Test
    public void testStringFromInt() throws Exception {
        int n = 0;
        String str = "6JaY2";
        char[] chars = str.toCharArray();
        n += Base62.CHARACTERS.indexOf(chars[0]) * (int) Math.pow(62, 4);
        n += Base62.CHARACTERS.indexOf(chars[1]) * (int) Math.pow(62, 3);
        n += Base62.CHARACTERS.indexOf(chars[2]) * (int) Math.pow(62, 2);
        n += Base62.CHARACTERS.indexOf(chars[3]) * (int) Math.pow(62, 1);
        n += Base62.CHARACTERS.indexOf(chars[4]) * (int) Math.pow(62, 0);
        Assert.assertEquals(str, Base62.fromBase10(n));
    }

    @Test
    public void testIntegerFromString() throws Exception {
        Assert.assertEquals(125, Base62.toBase10("cb"));
    }

    @Test
    public void testFromZero() {
        Assert.assertEquals("a", Base62.fromBase10(0));
        Assert.assertEquals("b", Base62.fromBase10(1));
    }
}
