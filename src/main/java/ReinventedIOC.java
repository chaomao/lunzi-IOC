import parser.IParser;
import parser.result.Cookbook;
import parser.simple.SimpleParser;
import product.User;

import java.lang.reflect.InvocationTargetException;

public class ReinventedIOC {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Kitchen kitchen = setupKitchen();

        User user = (User) kitchen.lookUp("user");

        System.out.println(user.getBank().getAmount());
    }

    private static Kitchen setupKitchen() {
        Kitchen kitchen = new Kitchen();
        kitchen.setCookbook(Cookbook.createByRecipes(getParser().getRecipes()));
        return kitchen;
    }

    private static IParser getParser() {
        return new SimpleParser();
    }
}
