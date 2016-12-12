//package ejb;
//
//import response.Creature;
//import response.CreaturePosition;
//
//import java.util.Random;
//
///**
// * Created by kimha on 12/1/16.
// */
////@Singleton
//public class Game {
//
//    private Random random = new Random();
//    private int width = 4;
//    private int height = 3;
//    private Creature[][] map;
//
//    public static void main(String[] args) {
//        Game gameEJB = new Game();
//        gameEJB.createNewMap();
//        Creature crat = new Creature();
//        crat.setName("Crat");
//        gameEJB.addCreature(crat);
//    }
//
//
//    public void startNewGame() {
//        map = new Creature[height][width];
//    }
//
//    public void addCreature(Creature c) {
//        int xPos = random.nextInt(width);
//        int yPos = random.nextInt(height);
//        CreaturePosition cp = new CreaturePosition(xPos,yPos);
//        c.setCreaturePosition(cp);
//        if (map[yPos][xPos] != null) {
//            System.out.println("DEAD");
//        }
//        System.out.println(c.getName() + " added to the game!");
//        System.out.println(c.getCreaturePosition());
//        map[yPos][xPos] = c;
//    }
//
//
//}
