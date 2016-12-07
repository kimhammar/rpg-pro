package ejb;

import entities.Game;

import javax.ejb.Singleton;
import java.util.List;

/**
 * Created by kimha on 11/27/16.
 */
@Singleton
public class ManagerEJB {

    List<Game> games;


}
